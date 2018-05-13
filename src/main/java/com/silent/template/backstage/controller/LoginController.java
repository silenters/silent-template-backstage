package com.silent.template.backstage.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContext;

import com.silent.framework.base.utils.StringUtils;
import com.silent.template.backstage.bean.po.PagePo;
import com.silent.template.backstage.bean.po.UserPo;
import com.silent.template.backstage.bean.vo.View;
import com.silent.template.backstage.constants.AuthConstants;
import com.silent.template.backstage.constants.AuthI18NCode;
import com.silent.template.backstage.properties.GlobalProperties;
import com.silent.template.backstage.service.PageService;
import com.silent.template.backstage.service.UserService;

/**
 * 登录及退出登录操作Controller
 * @author TanJin
 * @date 2016-3-24
 */
@Controller
@RequestMapping("/")
public class LoginController {

	private static final String LOGIN = "login";
	private static final String LOGOUT_SUCCESS_VIEW = "redirect:login";
	
	@Autowired
	private UserService userService;
	@Autowired
	private PageService pageService;
	@Autowired
	private GlobalProperties globalProperties;
	
	@RequestMapping("/login")
	public View login(HttpServletRequest request) throws Exception {
		String method = request.getParameter(AuthConstants.COMMON_OPERATION);
		if("login".equals(method)) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			request.setAttribute("username", username);
			UserPo userPo = check(username, password, request);
			if(null != userPo) {
				// 设置用户访问权限
				setAuthority(userPo.getId(), request);
				// 将用户信息放入Session
				request.getSession().setAttribute(AuthConstants.SESSION_USER_NAME, userPo);
				// 获取首页访问地址
				String indexPage = globalProperties.getAuthBackstageIndexUrl();
				return new View("redirect:"+indexPage, true);
			} 
		}
		return new View(StringUtils.isNotEmpty(globalProperties.getUserLoginPage()) ? globalProperties.getUserLoginPage() : LOGIN, true);
	}
	
	@RequestMapping("/logout")
	public View logout(HttpServletRequest request) {
		request.getSession().removeAttribute(AuthConstants.SESSION_USER_NAME);
		return new View(LOGOUT_SUCCESS_VIEW, true);
	}
	
	/**
	 * 登录校验
	 * @throws Exception 
	 * 
	 * @date 2016-3-22
	 */
	private UserPo check(String username, String password, HttpServletRequest request) throws Exception {
		RequestContext context = new RequestContext(request);
 		if(StringUtils.isEmpty(username)) {
			request.setAttribute(AuthConstants.MSG, context.getMessage(AuthI18NCode.ERROR_LOGIN_NAME_IS_EMPTY));
			return null;
		}
		if(StringUtils.isEmpty(password)) {
			request.setAttribute(AuthConstants.MSG, context.getMessage(AuthI18NCode.ERROR_PASSWORD_IS_EMPTY));
			return null;
		}
		
		// 获取用户信息
		UserPo userPo = userService.queryByUsername(username);
		
		if(null == userPo) {
			request.setAttribute(AuthConstants.MSG, context.getMessage(AuthI18NCode.ERROR_USER_NOT_EXIST));
			return null;
		}

		if(!userPo.checkPassword(password)) {
			request.setAttribute(AuthConstants.MSG, context.getMessage(AuthI18NCode.ERROR_USER_PASSWORD_ERROR));
			return null;
		}
		
		if (!userPo.isEnabled()) {
			request.setAttribute(AuthConstants.MSG, context.getMessage(AuthI18NCode.ERROR_USER_NOT_ENABLE));
			return null;
		}
	
		return userPo;
	}
	
	/**
	 * 设置权限信息
	 * @date 2016-3-22
	 */
	private void setAuthority(String userId, HttpServletRequest request) throws IOException {
		List<PagePo> pageList = pageService.queryUserSortPage(userId);
		Set<String> userAuthPageUrlSet = new HashSet<String>();
		Set<String> userAuthPageIdSet = new HashSet<String>();
		buildPageSet(pageList, userAuthPageUrlSet, userAuthPageIdSet);
		
		List<PagePo> list = pageService.queryAll();
		Map<String, PagePo> pageUrlMap = new HashMap<String, PagePo>();
		Map<String, PagePo> pageIdMap = new HashMap<String, PagePo>();
		
		for(PagePo pagePo : list) {
			if(StringUtils.isNotEmpty(pagePo.getUrl())) {
				List<PagePo> childPageList = new ArrayList<PagePo>();
				for(PagePo childPage : list) {
					if(StringUtils.isNotEmpty(childPage.getParent_id()) && childPage.getParent_id().equals(pagePo.getId())) {
						childPage.setParentPageUrl(pagePo.getUrl());
						childPageList.add(childPage);
					}
				}
				pagePo.setChildList(childPageList);
				pageUrlMap.put(pagePo.getUrl(), pagePo);
			}
			pageIdMap.put(pagePo.getId(), pagePo);
		}
		
		//可见的页面
		removeUnVisible(pageList);
		
		request.getSession().setAttribute(AuthConstants.PAGE_LIST, pageList);
		request.getSession().setAttribute(AuthConstants.PAGE_URL_MAP, pageUrlMap);
		request.getSession().setAttribute(AuthConstants.PAGE_ID_MAP, pageIdMap);
		request.getSession().setAttribute(AuthConstants.PAGE_URL_AUTH_SET, userAuthPageUrlSet);
		request.getSession().setAttribute(AuthConstants.PAGE_ID_AUTH_SET, userAuthPageIdSet);
	}
	
	/**
	 * 删除不可见的页面
	 * @date 2016-3-25
	 */
	private void removeUnVisible(List<PagePo> list) {
		Iterator<PagePo> iterator = list.iterator();
		while(iterator.hasNext()) {
			PagePo pagePo = iterator.next();
			if(pagePo.getVisible() == 0) {
				iterator.remove();
				continue;
			}
			if(null != pagePo.getChildList()) {
				removeUnVisible(pagePo.getChildList());
				if(pagePo.getChildList().size()==0) {
					iterator.remove();
				}
			}
		}
	}
	
	/**
	 * 构建用户可访问页面URL Set、ID Set
	 * @param userAuthPageUrlSet	用户可访问页面URL集合
	 * @param userAuthPageIdSet		用户可访问页面ID集合
	 * @date 2016-3-25
	 */
	private void buildPageSet(List<PagePo> list, Set<String> userAuthPageUrlSet, Set<String> userAuthPageIdSet) {
		for(PagePo pagePo : list) {
			if(null != pagePo.getChildList()) {
				buildPageSet(pagePo.getChildList(), userAuthPageUrlSet, userAuthPageIdSet);
			}
			if(StringUtils.isEmpty(pagePo.getUrl())) {
				continue;
			}
			userAuthPageUrlSet.add(pagePo.getUrl());
			userAuthPageIdSet.add(pagePo.getId());
		}
	}
}
