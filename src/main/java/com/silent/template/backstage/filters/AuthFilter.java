package com.silent.template.backstage.filters;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.silent.framework.base.bean.BaseCode;
import com.silent.framework.base.utils.StringUtils;
import com.silent.framework.base.utils.WebUtils;
import com.silent.template.backstage.constants.AuthConstants;
import com.silent.template.backstage.properties.GlobalProperties;
import com.silent.template.backstage.utils.AuthUtils;

/**
 * 权限验证
 * @author TanJin
 * @date 2016-3-24
 */
public class AuthFilter implements Filter {
	protected static Logger logger = LoggerFactory.getLogger(AuthFilter.class);
	
	private String loginUrl = "login";
	private String indexUrl = "index";
	private Set<String> authNoNeedLoginUrlSet = new HashSet<String>();			// 模板工程中不需要登陆页面URL
	private Set<String> authCommonUrlSet = new HashSet<String>();				// 模板工程中公共页面URL（所有人都能访问页面URL）
	private Set<String> userNoNeedLoginUrlSet = new HashSet<String>();			// 用户自定义的不需要登录页面URL
	private Set<String> userCommonUrlSet = new HashSet<String>();				// 用户自定义的公共页面URL
	private Set<String> staticResourceNamespaceList = new HashSet<String>();	// 静态资源目录列表
	
	@Autowired
	private GlobalProperties globalProperties;
	
	@Override
	public void destroy() {
		authNoNeedLoginUrlSet.clear();
		authCommonUrlSet.clear();
		userNoNeedLoginUrlSet.clear();
		userCommonUrlSet.clear();
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		if(needLogin(request)) {
			String url = request.getServletContext().getContextPath() + "/" + loginUrl;
			if(WebUtils.isAjax(request)) {
				response.addHeader("AuthUrl", url);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AuthConstants.CODE, BaseCode.LOGIN_TIMEOUT);
				jsonObject.put("AuthUrl", url);
				response.setContentType("application/json; charset=utf-8");
				response.getWriter().write(jsonObject.toJSONString());
				return;
			}
			response.sendRedirect(url);
			return;
		}
		chain.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// 模板工程默认无需登录URL
		String authNotNeedLoginUrls = globalProperties.getAuthNoNeedLoginUrls();
		// 模板工程默认公共URL
		String authCommonUrls = globalProperties.getAuthCommonUrls();
		// 用户自定义无需登录URL
		String userNoNeedLoginUrls = globalProperties.getAuthUserNoNeedLoginUrls();
		// 用户自定义公共URL
		String userCommonUrls = globalProperties.getAuthUserCommonUrls();
		// 默认静态资源保存目录
		String defaultStaticResourceNamespace = globalProperties.getDefaultStaticResourceNamespace();
		// 用户自定义静态资源保存目录
		String userStaticResourceNamespaces = globalProperties.getUserStaticResourceNamespaces();
		
		setNoFilterUrls(authNotNeedLoginUrls, authNoNeedLoginUrlSet);
		setNoFilterUrls(authCommonUrls, authCommonUrlSet);
		setNoFilterUrls(userNoNeedLoginUrls, userNoNeedLoginUrlSet);
		setNoFilterUrls(userCommonUrls, userCommonUrlSet);
		setNoFilterUrls(defaultStaticResourceNamespace, staticResourceNamespaceList);
		setNoFilterUrls(userStaticResourceNamespaces, staticResourceNamespaceList);
		
		// 将首页加入公共访问页面
		String indexPage = globalProperties.getAuthBackstageIndexUrl();
		if(StringUtils.isNotEmpty(indexPage)) {
			authCommonUrlSet.add(indexPage);
		}
		
		authNoNeedLoginUrlSet.add(AuthUtils.loginUrl);
		logger.info("[AuthFilter] [authNoNeedLoginUrlSet:{}] [authCommonUrlSet:{}] [userNoNeedLoginUrlSet:{}] [userCommonUrlSet:{}] [staticResourceNamespaceList:{}]", 
				authNoNeedLoginUrlSet, authCommonUrlSet, userNoNeedLoginUrlSet, userCommonUrlSet, staticResourceNamespaceList);
	}
	
	/**
	 * 判断是否需要登录
	 * @param request
	 * @return
	 */
	private boolean needLogin(HttpServletRequest request) {
		String requestPage = AuthUtils.getRequestPage(request);
		if(checkSwagger(requestPage, request)){
			// 排除swagger的所有请求
			return false;
		}
		if(isStaticResource(requestPage)) {
			// 静态资源过滤
			return false;
		}
		if(requestPage.endsWith(loginUrl)) {
			// 登录页面
			return false;
		}
		if(!isNeedLogin(requestPage, authNoNeedLoginUrlSet)) {
			// 模板工程中不需要登录的页面
			return false;
		}
		if(!isNeedLogin(requestPage, userNoNeedLoginUrlSet)) {
			// 用户自定义不需要登录的页面
			return false;
		}
		if(request.getSession().getAttribute(AuthConstants.SESSION_USER_NAME) == null) {
			// 没有登录
			LoggerFactory.getLogger(getClass()).info("need login url:{}", requestPage);
			return true;
		}
		if(requestPage.equals(indexUrl)) {
			// 首页
			return false;
		}
		if(!isNeedLogin(requestPage, authCommonUrlSet)) {
			// 模板工程中公共页面，无需用户页面权限验证
			return false;
		}
		if(!isNeedLogin(requestPage, userCommonUrlSet)) {
			// 用户自定义公共页面，无需用户页面权限验证
			return false;
		}
		@SuppressWarnings("unchecked")
		Set<String> set = (Set<String>)request.getSession().getAttribute(AuthConstants.PAGE_URL_AUTH_SET);
		if(set == null) {
			// 没有权限
			return true;
		}
		return isNeedLogin(requestPage, set);
	}
	
	/**
	 * 校验Swagger请求
	 * @param requestPage
	 * @return
	 */
	private boolean checkSwagger(String requestPage, HttpServletRequest request) {
		if(requestPage.indexOf("swagger") != -1 || requestPage.indexOf("api-docs") != -1){
			if(request.getSession().getAttribute(AuthConstants.SESSION_USER_NAME) == null) {
				// 没有登录
				LoggerFactory.getLogger(getClass()).info("swagger request, need login url:{}", requestPage);
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 判断当前请求页面是否在用户权限列表中，若在则不需要登录，否则需要登录验证
	 * @param 
	 * @return
	 */
	private boolean isNeedLogin(String requestPage, Set<String> authPageSet) {
		for(String authPage : authPageSet) {
			if(requestPage.startsWith(authPage)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断是否为静态资源，若是则返回true，否则返回false
	 * @param 
	 * @return
	 */
	private boolean isStaticResource(String requestPage) {
		int index = requestPage.indexOf("/");
		if(index <= 0) {
			return false;
		}
		String firstDir = requestPage.substring(0, index);
		return staticResourceNamespaceList.contains(firstDir);
	}
	
	/**
	 * 设置URL
	 * @param 
	 * @return
	 */
	private void setNoFilterUrls(String noFilterUrls, Set<String> localSet) {
		if(StringUtils.isNotEmpty(noFilterUrls)) {
			String[] array = noFilterUrls.split(",");
			for(String url : array) {
				localSet.add(url);
			}
		}
	}
	
}