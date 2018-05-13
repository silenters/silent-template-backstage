package com.silent.template.backstage.listener;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.silent.framework.base.configurer.ResourceContext;
import com.silent.template.backstage.constants.AuthGlobalParam;
import com.silent.template.backstage.properties.GlobalProperties;

/**
 * 初始化监听器
 */
public class InitListener implements ServletContextListener  {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GlobalProperties globalProperties;
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		// 获取首页访问URL，并放入全局变量中
		event.getServletContext().setAttribute(AuthGlobalParam.BACKSTAGE_INDEX, globalProperties.getAuthBackstageIndexUrl());
		// 静态资源访问CDH地址
		event.getServletContext().setAttribute(AuthGlobalParam.TEMPLATE_URL_CDN, globalProperties.getTemplateUrlCdn());
		// 项目上下文路径
		event.getServletContext().setAttribute(AuthGlobalParam.PROJECT_CONTEXT_PATH, event.getServletContext().getContextPath() + "/");
		
		logger.info("[InitListener] [template_url_cdn:{}]", globalProperties.getTemplateUrlCdn());
		logger.info("[InitListener] [project_context_path:{}]", event.getServletContext().getAttribute(AuthGlobalParam.PROJECT_CONTEXT_PATH));
		logger.info("[InitListener] [backstage_index:{}]", globalProperties.getAuthBackstageIndexUrl());
		
		// 获取页面配置项并加载到上下文
		Properties properties = ResourceContext.loadResourceConfigProperties(AuthGlobalParam.getConfigFiles());
		Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<Object, Object> entry = iterator.next();
			if(((String)entry.getKey()).startsWith(AuthGlobalParam.PAGE_ATTRIBUTE_PREFIX)) {
				String param = ((String)entry.getKey());
				param = param.substring(param.indexOf(AuthGlobalParam.PAGE_ATTRIBUTE_PREFIX)+AuthGlobalParam.PAGE_ATTRIBUTE_PREFIX.length(), param.length());
				String name = param.replace(".", "_");
				event.getServletContext().setAttribute(name, entry.getValue());
				logger.info("[InitListener] [page.attribute] [{}:{}]", name, entry.getValue());
			}
		}
	}
}