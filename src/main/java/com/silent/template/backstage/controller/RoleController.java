package com.silent.template.backstage.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.silent.framework.base.utils.StringUtils;
import com.silent.framework.base.utils.UuidUtils;
import com.silent.template.backstage.bean.po.PagePo;
import com.silent.template.backstage.bean.po.RolePagePo;
import com.silent.template.backstage.bean.po.RolePo;
import com.silent.template.backstage.bean.vo.PageNodeVo;
import com.silent.template.backstage.bean.vo.View;
import com.silent.template.backstage.constants.AuthConstants;
import com.silent.template.backstage.constants.AuthI18NCode;
import com.silent.template.backstage.service.PageService;
import com.silent.template.backstage.service.RolePageService;
import com.silent.template.backstage.service.RoleService;

/**
 * 权限角色相关处理Controller
 * @author TanJin
 * @date 2016-3-24
 */
@Controller
@RequestMapping("/admin/authority/role")
public class RoleController {
	private final static String INDEX = "admin/role/index.html";
	private final static String EDIT = "admin/role/edit.html";
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private PageService pageService;
	@Autowired
	private RolePageService rolePageService;
	
	@RequestMapping("/index")
	protected View index(HttpServletRequest request) throws Exception {
		List<RolePo> list = roleService.queryAll();
		request.setAttribute(AuthConstants.LIST, list);
		return new View(INDEX);
	}
	
	@RequestMapping("/add")
	protected View add(HttpServletRequest request) throws Exception {
		return new View(EDIT);
	}
	
	@RequestMapping("/edit/{id}")
	protected View edit(@PathVariable("id") String id, HttpServletRequest request) throws Exception {
		List<PageNodeVo> nodes = getPageTreeNodes(id);
		RolePo rolePo = roleService.queryRoleById(id);
		request.setAttribute("nodes", nodes);
		request.setAttribute(AuthConstants.BEAN, rolePo);
		return new View(EDIT);
	}
	
	@RequestMapping(value={"/add/save","/edit/save"})
	@ResponseBody
	private ResponseBaseBean save(@Validated RolePo rolePo, HttpServletRequest request) throws Exception {
		RequestContext context = new RequestContext(request);
		roleService.updateOrInsert(rolePo);

		ResponseBaseBean responseBean = new ResponseBaseBean();
		responseBean.buildSuccess(context.getMessage(AuthI18NCode.COMMON_SAVE_SUCCESS));
		return responseBean;
	}
	
	/**
	 * 获取页面树JSON字符串信息
	 * @throws IOException 
	 * @date 2016-3-28
	 */
	private List<PageNodeVo> getPageTreeNodes(String id) throws SQLException, IOException {
		Map<String, RolePagePo> map = pageService.queryRolePageMap(id);
		List<PagePo> pageList = pageService.queryAll();
		Iterator<PagePo> iterator = pageList.iterator();
		List<PageNodeVo> treeNodes = new ArrayList<PageNodeVo>();
		while(iterator.hasNext()) {
			PagePo pagePo = iterator.next();
			PageNodeVo node = pageService.buildTreeNode(pagePo);
			if(map.containsKey(pagePo.getId())){
				node.setChecked(true);
			}
			treeNodes.add(node);
			iterator.remove();
		}
		return treeNodes;
	}
	
	@RequestMapping("/del")
	@ResponseBody
	private ResponseBaseBean delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		ResponseBaseBean responseBean = new ResponseBaseBean();
		if(StringUtils.isEmpty(id)) {
			responseBean.buildError();
			return responseBean;
		}
		boolean isSuccess = roleService.deleteRole(id);
		responseBean.buildCode(isSuccess);
		return responseBean;
	}
	
	/**
	 * 保存角色及页面对应关系
	 * @date 2016-3-24
	 */
	@RequestMapping("/edit/rolepage")
	@ResponseBody
	private ResponseBaseBean saveRolePage(HttpServletRequest request) throws SQLException, IOException {
		RequestContext context = new RequestContext(request);
		String roleId = request.getParameter("roleId");
		String[] pageIds = request.getParameter("pageIds").split(",");
		
		ResponseBaseBean responseBean = new ResponseBaseBean();
		if(null == pageIds || pageIds.length <= 0) {
			responseBean.buildError(context.getMessage(AuthI18NCode.ERROR_ROLE_PAGE_EMPTY));
			return responseBean;
		}
		
		Map<String, RolePagePo> map = pageService.queryRolePageMap(roleId);
		for (String pageId : pageIds) {
			if (StringUtils.isEmpty(pageId)) {
				continue;
			}
			RolePagePo rolePagePo = rolePageService.queryByRoleIdPageId(roleId, pageId);
			if(null == rolePagePo) {
				rolePagePo = new RolePagePo();
				rolePagePo.setId(UuidUtils.create());
				rolePagePo.setRole_id(roleId);
				rolePagePo.setPage_id(pageId);
				rolePagePo.setTime(System.currentTimeMillis());
				rolePageService.insert(rolePagePo);
			}
			map.remove(pageId);
		}
		
		// 删除剩余的页面
		for (RolePagePo po : map.values()) {
			rolePageService.deleteById(po.getId());
		}
		
		responseBean.buildSuccess(context.getMessage(AuthI18NCode.COMMON_SAVE_SUCCESS));
		return responseBean;
	}
}
