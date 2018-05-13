package com.silent.template.backstage.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.silent.framework.base.utils.StringUtils;
import com.silent.template.backstage.bean.po.PagePo;
import com.silent.template.backstage.bean.vo.View;
import com.silent.template.backstage.constants.AuthConstants;
import com.silent.template.backstage.utils.AuthUtils;

/**
 * 所有请求处理拦截器
 * @author TanJin
 * @date 2017-7-19
 */
public class RequestInterceptor implements HandlerInterceptor {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception) throws Exception {
	}
	
	/**
	 * 处理器处理之前调用
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		return true;
	}

	/**
	 * 处理器处理之后调用
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object object, ModelAndView mv) throws Exception {
		if(null != mv) {
			View view = (View) mv.getModelMap().get(AuthConstants.VIEW);
			if(null != view) {
				mv.setViewName(view.isFullPage() ? view.getPage() : AuthConstants.PAGE_INDEX);
				PagePo currentPagePo = getCurrentPagePo(request);
				if(StringUtils.isEmpty(view.getTitle()) && null != currentPagePo) {
					view.setTitle(currentPagePo.getName());
				}
				mv.getModelMap().addAttribute(AuthConstants.VIEW, view);
				request.setAttribute(AuthConstants.CURRENT_PAGE, currentPagePo);
				
				if(null != currentPagePo) {
					Map<String, Boolean> currentPageMap = new HashMap<String, Boolean>();
					currentPageMap.put(currentPagePo.getId(), Boolean.TRUE);
					Map<String, PagePo> map = (Map<String, PagePo>)request.getSession().getAttribute(AuthConstants.PAGE_ID_MAP);
					PagePo pagePo = map.get(currentPagePo.getParent_id());
					
					while(null != pagePo) {
						currentPageMap.put(pagePo.getId(), Boolean.TRUE);
						pagePo = map.get(pagePo.getParent_id());
					}
					request.setAttribute(AuthConstants.CURRENT_PAGE_MAP, currentPageMap);
					
					// 获取当前页面的子页面URL Map
					Map<String, String> childPageMap = getPageUrlMap(request, currentPagePo);
					request.setAttribute(AuthConstants.CHILD_PAGE_MAP, childPageMap);
				}
			}
		}
	}
	
	/**
	 * 获取当前页面的子页面页面Type 与 URL对应 Map
	 * @date 2016-1-8
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getPageUrlMap(HttpServletRequest request, PagePo currentPagePo) {
		Set<String> pageIdSet = (Set<String>) request.getSession().getAttribute(AuthConstants.PAGE_ID_AUTH_SET);
		Map<String, PagePo> PageIdMap = (Map<String, PagePo>) request.getSession().getAttribute(AuthConstants.PAGE_ID_MAP);
		
		PagePo PagePo = PageIdMap.get(currentPagePo.getId());
		Map<String, String> childPageMap = new HashMap<String, String>();
		
		if(null != PagePo) {
			List<PagePo> childPageList = PagePo.getChildList();
			if(null != childPageList && childPageList.size() > 0) {
				for(PagePo page : PagePo.getChildList()) {
					if(pageIdSet.contains(page.getId()) && StringUtils.isNotEmpty(page.getUrl())) {
						setChildPageMap(childPageMap, page.getUrl());
					}
				}
			}
		}
		
		logger.debug("[RequestInterceptor] [child page map size:{}]", childPageMap.size());
		return childPageMap;
	}

	/**
	 * 获取当前页面对象
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PagePo getCurrentPagePo(HttpServletRequest request) {
		String requestPage = AuthUtils.getRequestPage(request);
		Map<String, PagePo> map = (Map<String, PagePo>)request.getSession().getAttribute(AuthConstants.PAGE_URL_MAP);
		if(null != map) {
			for(String key : map.keySet()) {
				if(requestPage.indexOf(key) >= 0) {
					return map.get(key);
				}
			}
		}
		return null;
	}
	
	/**
	 * 设置子页面Map
	 * @date 2016-3-25
	 */
	private void setChildPageMap(Map<String, String> childPageMap, String pageUrl) {
		int index = pageUrl.lastIndexOf("/");
		if(index > 0) {
			String type = pageUrl.substring(index + 1, pageUrl.length());
			if(StringUtils.isNotEmpty(type)) {
				childPageMap.put(type, pageUrl);
			}
		}
	}
}
