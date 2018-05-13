package com.silent.template.backstage.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContext;

import com.silent.framework.base.bean.ResponseBaseBean;
import com.silent.framework.base.bean.ResponseBean;
import com.silent.framework.base.utils.StringUtils;
import com.silent.framework.base.utils.UserUtils;
import com.silent.template.backstage.bean.po.RolePo;
import com.silent.template.backstage.bean.po.UserPo;
import com.silent.template.backstage.bean.po.UserRolePo;
import com.silent.template.backstage.bean.vo.View;
import com.silent.template.backstage.constants.AuthConstants;
import com.silent.template.backstage.constants.AuthI18NCode;
import com.silent.template.backstage.service.RoleService;
import com.silent.template.backstage.service.UserService;

/**
 * 管理员用户管理
 * @author TanJin
 * @date 2016-3-22
 */
@Controller
@RequestMapping("/admin/authority/user")
public class UserController {
	private static final String INDEX = "admin/user/manage/index.html";
	private static final String EDIT = "admin/user/manage/edit.html";
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	
	@RequestMapping("/index")
	private View index(HttpServletRequest request) {
		List<UserPo> list = userService.queryAllUserList();
		request.setAttribute(AuthConstants.LIST, list);
		return new View(INDEX);
	}
	
	@RequestMapping("/add")
	private View add(HttpServletRequest request, HttpServletRequest response) {
		return new View(EDIT);
	}
	
	/**
	 * 跳转到编辑页面操作
	 */
	@RequestMapping("/edit/{id}")
	private View edit(@PathVariable("id") String id, HttpServletRequest request) {
		UserPo userBean = userService.queryById(id);
		Map<String, UserRolePo> map = roleService.queryLoginUserRoleMap(id);
		List<RolePo> userRoleList = roleService.queryAll();
		
		request.setAttribute(AuthConstants.LIST, userRoleList);
		request.setAttribute(AuthConstants.MAP, map);
		request.setAttribute(AuthConstants.BEAN, userBean);
		return new View(EDIT);
	}
	
	/**
	 * 保存编辑信息
	 * @throws Exception 
	 */
	@RequestMapping(value={"/add/save","/edit/save"})
	@ResponseBody
	public ResponseBaseBean save(@Validated UserPo userPo, HttpServletRequest request) throws Exception {
		RequestContext context = new RequestContext(request);
		ResponseBaseBean responseBean = new ResponseBaseBean();
		if(StringUtils.isEmpty(userPo.getId())) {
			// 修改密码
			userPo.changePassword(userPo.getPassword());
			// 查询用户是否已经注册
			UserPo dbBean = userService.queryByUsername(userPo.getUsername());
			if(null != dbBean) {
				responseBean.buildError(context.getMessage(AuthI18NCode.ERROR_USER_ALREADY_REGISTER));
				return responseBean;
			}
			
		} else {
			UserPo dbUser = userService.queryById(userPo.getId());
			userPo.setSalt(dbUser.getSalt());
			if(null != dbUser && !dbUser.getPassword().equals(userPo.getPassword())) {
				//用户修改了密码
				userPo.changePassword(userPo.getPassword());
			}
		}
		
		//保存用户信息
		boolean isSuccess = userService.updateOrInsert(userPo);
		if(isSuccess) {
			responseBean.buildSuccess(context.getMessage(AuthI18NCode.COMMON_SAVE_SUCCESS));
		} else {
			responseBean.buildError(context.getMessage(AuthI18NCode.COMMON_SAVE_ERROR));
		}
		return responseBean;
	}
	
	@RequestMapping("/del")
	@ResponseBody
	private ResponseBaseBean delete(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		ResponseBaseBean responseBean = new ResponseBaseBean();
		if(StringUtils.isEmpty(id)) {
			responseBean.buildError();
			return responseBean;
		}
		boolean isSuccess = userService.deleteUser(id);
		responseBean.buildCode(isSuccess);
		return responseBean;
	}
	
	@RequestMapping("/edit/resetpass")
	@ResponseBody
	private ResponseBean<String> resetpass(HttpServletRequest request) {
		String id = request.getParameter("id");
		ResponseBean<String> responseBean = new ResponseBean<String>();
		RequestContext context = new RequestContext(request);
		
		UserPo userPo = userService.queryById(id);
		if(null == userPo) {
			responseBean.buildError();
			responseBean.setMsg(context.getMessage(AuthI18NCode.ERROR_QUERY_DATA_ERROR));
			return responseBean;
		}
		
		String salt = UserUtils.createRandSalt();
		String newPass = UserUtils.createDefaultPassword(userPo.getUsername(), salt);
		userPo.setPassword(newPass);
		userPo.setSalt(salt);
		boolean isSuccess = userService.updateUser(userPo);
		if(isSuccess) {
			responseBean.buildSuccess(userPo.getPassword(), context.getMessage(AuthI18NCode.COMMON_USER_PASSWORD_RESET_SUCCESS));
		} else {
			responseBean.buildError(context.getMessage(AuthI18NCode.ERROR_USER_PASSWORD_RESET_ERROR));
		}
		return responseBean;
	}
	
	/**
	 * 保存管理员登陆账号与权限角色对应关系
	 * @date 2016-3-29
	 */
	@RequestMapping("/edit/saverole")
	@ResponseBody
	private ResponseBaseBean saveUserRoleId(HttpServletRequest request) {
		RequestContext context = new RequestContext(request);
		String userId = request.getParameter("user_id");
		String roles = request.getParameter("roles");
		boolean isSave = roleService.saveUserRoleId(userId, roles);
		ResponseBaseBean responseBean = new ResponseBaseBean();
		if(isSave) {
			responseBean.buildSuccess(context.getMessage(AuthI18NCode.COMMON_SAVE_SUCCESS));
		} else {
			responseBean.buildError(context.getMessage(AuthI18NCode.COMMON_SAVE_ERROR));
		}
		return responseBean;
	}

}