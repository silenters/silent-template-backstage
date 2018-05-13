package com.silent.template.backstage.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 权限相关操作工具类
 * @author TanJin
 * @date 2016-3-24
 */
public class AuthUtils {
	protected static Logger logger = LoggerFactory.getLogger(AuthUtils.class);
	
	private static final String pageSuffix = ".html";		// 页面后缀
	public static final String loginUrl = "login";						// 登录请求	
	
	/**
	 * 获取请求页面路径
	 * @param HttpServletRequest
	 * @return
	 */
	public static String getRequestPage(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		int index = requestUri.indexOf(contextPath);
		String requestPage = requestUri.substring(index + contextPath.length() + 1);
		if("/".equals(requestPage.substring(0, 1))) {
			requestPage = requestPage.substring(1, requestPage.length());
		}
		return requestPage;
	}
	
	/**
	 * 是否为静态页面
	 * @param HttpServletRequest
	 * @return
	 */
	public static boolean isStaticPage(HttpServletRequest request) {
		return pageSuffix.equals(AuthUtils.getRequestPageSuffix(request));
	};
	
	/**
	 * 是否为登录请求，true是，false不是
	 * @param 
	 * @return
	 */
	public static boolean isLoginRequest(HttpServletRequest request) {
		String requestPage = getRequestPage(request);
		if(requestPage.endsWith(loginUrl)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取请求页面路径后缀
	 * @param HttpServletRequest
	 * @return
	 */
	private static String getRequestPageSuffix(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		int index = requestUri.lastIndexOf(".");
		if(index > 0) {
			return requestUri.substring(index, requestUri.length());
		}
		return "";
	}
	
}
