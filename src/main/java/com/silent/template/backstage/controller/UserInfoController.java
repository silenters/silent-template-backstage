package com.silent.template.backstage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContext;

import com.silent.framework.base.bean.ResponseBaseBean;
import com.silent.framework.base.utils.StringUtils;
import com.silent.framework.base.utils.UserUtils;
import com.silent.template.backstage.bean.po.UserPo;
import com.silent.template.backstage.bean.vo.View;
import com.silent.template.backstage.constants.AuthConstants;
import com.silent.template.backstage.constants.AuthI18NCode;
import com.silent.template.backstage.service.UserService;

/**
 * 用户个人信息管理
 * @author TanJin
 * @date 2016-3-22
 */
@Controller
@RequestMapping("/admin/user/info")
public class UserInfoController {
	private static final String EDIT = "admin/user/info/edit.html";
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/index")
	private View index(HttpServletRequest request) throws Exception {
		RequestContext context = new RequestContext(request);
		UserPo bean = (UserPo) request.getSession().getAttribute(AuthConstants.SESSION_USER_NAME);
		request.setAttribute(AuthConstants.BEAN, bean);
		return new View(EDIT, context.getMessage(AuthI18NCode.COMMON_INDEX));
	}
	
	@RequestMapping("/resetpass")
	@ResponseBody
	private ResponseBaseBean resetpass(HttpServletRequest request) throws Exception {
		RequestContext context = new RequestContext(request);
		String id = request.getParameter("id");
		String oldPass = request.getParameter("password");
		String newPass = request.getParameter("new_password");
		String confirmPass = request.getParameter("confirm_password");
		
		ResponseBaseBean responseBean = new ResponseBaseBean();
		String msg = check(oldPass, newPass, confirmPass, context);
		if(StringUtils.isNotEmpty(msg)) {
			responseBean.buildError(msg);
			return responseBean;
		}
		
		UserPo userBean = userService.queryById(id);
		UserPo sessionBean = (UserPo) request.getSession().getAttribute(AuthConstants.SESSION_USER_NAME);
		
		if(null == userBean) {
			responseBean.buildError(context.getMessage(AuthI18NCode.ERROR_QUERY_DATA_ERROR));
			return responseBean;
		}
		if(!sessionBean.getId().equals(userBean.getId())) {
			//不是该用户
			responseBean.buildError(context.getMessage(AuthI18NCode.ERROR_USER_NOT_YOURSELF));
			return responseBean;
		}
		
		String encryptPass = UserUtils.encodePassword(oldPass, userBean.getSalt());
		if(!encryptPass.equals(userBean.getPassword())) {
			responseBean.buildError(context.getMessage(AuthI18NCode.ERROR_USER_OLD_PASSWORD_ERROR));
			return responseBean;
		}
		
		userBean.changePassword(newPass);
		boolean isSuccess = userService.updateOrInsert(userBean);
		if(isSuccess) {
			responseBean.buildSuccess(context.getMessage(AuthI18NCode.COMMON_UPDATE_SUCCESS));
		} else {
			responseBean.buildError(context.getMessage(AuthI18NCode.COMMON_UPDATE_ERROR));
		}
		return responseBean;
	}
	
	/**
	 * 校验字段
	 * @param 
	 * @return
	 */
	private String check(String oldPass, String newPass, String confirmPass, RequestContext context) {
		if(StringUtils.isEmpty(oldPass)) {
			return context.getMessage(AuthI18NCode.ERROR_USER_OLD_PASSWORD_NOT_EMPTY);
		}
		if(StringUtils.isEmpty(newPass)) {
			return context.getMessage(AuthI18NCode.ERROR_USER_NEW_PASSWORD_NOT_EMPTY);
		}
		if(StringUtils.isEmpty(confirmPass)) {
			return context.getMessage(AuthI18NCode.ERROR_USER_CONFIRM_PASSWORD_NOT_EMPTY);
		}
		if(!confirmPass.equals(newPass)) {
			return context.getMessage(AuthI18NCode.ERROR_USER_CONFIRM_PASSWORD_NOT_CONSISTENT);
		}
		return null;
	}

}