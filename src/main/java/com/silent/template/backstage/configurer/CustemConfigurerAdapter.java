package com.silent.template.backstage.configurer;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.silent.framework.base.utils.StringUtils;
import com.silent.template.backstage.filters.AuthFilter;
import com.silent.template.backstage.i18n.I18NMessageSource;
import com.silent.template.backstage.interceptor.RequestInterceptor;
import com.silent.template.backstage.listener.InitListener;
import com.silent.template.backstage.properties.GlobalProperties;

/**
 * 自定义配置项类，该类中和存入拦截器、过滤器等配置项信息
 * @author TanJin
 */
@SuppressWarnings("deprecation")
@Configuration
public class CustemConfigurerAdapter extends WebMvcConfigurerAdapter {
	
	@Autowired
	private GlobalProperties globalProperties;
	
	/**
	 * 注册拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 将请求拦截器与Path请求路径对应起来并进行注册
		registry.addInterceptor(requestInterceptor()).addPathPatterns("/**");
	}
	
	/**
	 * 请求拦截器
	 * @param 
	 * @return
	 */
	@Bean
	public RequestInterceptor requestInterceptor() {
		return new RequestInterceptor();
	}
	
	/**
	 * 注册过滤器
	 * @param 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<AuthFilter> authFilterRegistrationBean() {
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<AuthFilter>();
        registrationBean.setName("authFilter");
        registrationBean.setFilter(authFilter());
        registrationBean.setOrder(1);
        List<String> urlList = new ArrayList<String>();
        urlList.add("/*");
        registrationBean.setUrlPatterns(urlList);
        return registrationBean;
	}
	
	/**
	 * 权限过滤器
	 * @param 
	 * @return
	 */
	@Bean
	public AuthFilter authFilter() {
		return new AuthFilter();
	}
	
	/**
	 * 注册初始化操作监听器
	 * @param 
	 * @return
	 */
	@Bean
	public ServletListenerRegistrationBean<InitListener> servletListenerRegistrationBean(){
		ServletListenerRegistrationBean<InitListener> listener = new ServletListenerRegistrationBean<InitListener>();
		listener.setListener(initListener());
		return listener;
	}
	
	/**
	 * 初始化监听器
	 * @param 
	 * @return
	 */
	@Bean
	public InitListener initListener() {
		return new InitListener();
	}
	
	/**
	 * 国际化
	 * @param 
	 * @return
	 */
	@Bean
	public MessageSource messageSource() {
		I18NMessageSource messageSource = new I18NMessageSource();
		String[] basenames = null;
		String i18nName = globalProperties.getI18nFileName();
		if(StringUtils.isEmpty(i18nName)) {
			basenames = new String[]{"config/default-messages"};
		} else {
			basenames = new String[]{i18nName, "config/default-messages"};
		}
		messageSource.setBasenames(basenames);
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}
	
	/**
	 * 国际化
	 * @param 
	 * @return
	 */
	@Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }
	
	/**
	 * 国际化
	 * @param 
	 * @return
	 */
	@Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        return new LocaleChangeInterceptor();
    }
	
}
