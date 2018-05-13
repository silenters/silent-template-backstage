package com.silent.template.backstage.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.silent.template.backstage.constants.AuthGlobalParam;

/**
 * 读取自定义配置文件信息，
 * 包括模板工程中的默认配置文件：classpath:default.properties;
 * 用户自定义Jar包中保存的配置文件：classpath:config.properties;
 * 用户自定义外部文件系统保存的配置文件：file:config/config.properties
 * 
 * @author TanJin
 * @date 2018年3月6日
 */
@ConfigurationProperties()
@PropertySource(value= {
		AuthGlobalParam.DEFAULT_PROPERTIES, 
		AuthGlobalParam.CONFIG_PROPERTIES_IN_JAR, 
		AuthGlobalParam.CONFIG_PROPERTIES_FROM_LOCAL
	}, ignoreResourceNotFound=true)
@Component
public class GlobalProperties {
	
	@Value("${project.base.path}")
	private String projectBasePath;				// 项目根路径前缀
	
	@Value("${template.url.cdn}")
	private String templateUrlCdn;				// CDN地址
	
	@Value("${auth.no.need.login.urls}")
	private String authNoNeedLoginUrls;			// 不需要登录过滤权限的URL
	
	@Value("${auth.common.urls}")
	private String authCommonUrls;				// 公共页面，谁都能访问，不过滤权限，但需要登录
	
	@Value("${user.no.need.login.urls}")
	private String authUserNoNeedLoginUrls;		// 用户自定义的无需登录页面
	
	@Value("${user.common.urls}")
	private String authUserCommonUrls;			// 用户自定义的公共页面
	
	@Value("${default.static.resource.namespace}")
	private String defaultStaticResourceNamespace;		// 默认CSS、JS等静态资源文件保存目录
	
	@Value("${user.static.resource.namespaces}")
	private String userStaticResourceNamespaces;		// 用户自定义静态资源CSS\JS等保存目录(可配置多个，以逗号分隔)
	
	@Value("${user.login.page}")
	private String userLoginPage;						// 用户自定义登录界面
	
	@Value("${auth.backstage.index.url}")
	private String authBackstageIndexUrl;		// 用户定义的后台首页，默认使用模板工程中的首页，同时首页自动加入公共访问页面
	
	@Value("${template.i18n.name}")
	private String i18nFileName;				// 国际化文件前缀名称
	
	public String getTemplateUrlCdn() {
		return templateUrlCdn;
	}

	public void setTemplateUrlCdn(String templateUrlCdn) {
		this.templateUrlCdn = templateUrlCdn;
	}

	public String getAuthNoNeedLoginUrls() {
		return authNoNeedLoginUrls;
	}

	public void setAuthNoNeedLoginUrls(String authNoNeedLoginUrls) {
		this.authNoNeedLoginUrls = authNoNeedLoginUrls;
	}

	public String getAuthCommonUrls() {
		return authCommonUrls;
	}

	public void setAuthCommonUrls(String authCommonUrls) {
		this.authCommonUrls = authCommonUrls;
	}

	public String getAuthBackstageIndexUrl() {
		return authBackstageIndexUrl;
	}

	public void setAuthBackstageIndexUrl(String authBackstageIndexUrl) {
		this.authBackstageIndexUrl = authBackstageIndexUrl;
	}

	public String getI18nFileName() {
		return i18nFileName;
	}

	public void setI18nFileName(String i18nFileName) {
		this.i18nFileName = i18nFileName;
	}

	public String getAuthUserNoNeedLoginUrls() {
		return authUserNoNeedLoginUrls;
	}

	public void setAuthUserNoNeedLoginUrls(String authUserNoNeedLoginUrls) {
		this.authUserNoNeedLoginUrls = authUserNoNeedLoginUrls;
	}

	public String getAuthUserCommonUrls() {
		return authUserCommonUrls;
	}

	public void setAuthUserCommonUrls(String authUserCommonUrls) {
		this.authUserCommonUrls = authUserCommonUrls;
	}
	
	public String getDefaultStaticResourceNamespace() {
		return defaultStaticResourceNamespace;
	}

	public void setDefaultStaticResourceNamespace(String defaultStaticResourceNamespace) {
		this.defaultStaticResourceNamespace = defaultStaticResourceNamespace;
	}

	public String getUserStaticResourceNamespaces() {
		return userStaticResourceNamespaces;
	}

	public void setUserStaticResourceNamespaces(String userStaticResourceNamespaces) {
		this.userStaticResourceNamespaces = userStaticResourceNamespaces;
	}
	
	public String getProjectBasePath() {
		return projectBasePath;
	}

	public void setProjectBasePath(String projectBasePath) {
		this.projectBasePath = projectBasePath;
	}
	
	public String getUserLoginPage() {
		return userLoginPage;
	}

	public void setUserLoginPage(String userLoginPage) {
		this.userLoginPage = userLoginPage;
	}

	@Override
	public String toString() {
		return "GlobalProperties [projectBasePath=" + projectBasePath + ", templateUrlCdn=" + templateUrlCdn
				+ ", authNoNeedLoginUrls=" + authNoNeedLoginUrls + ", authCommonUrls=" + authCommonUrls
				+ ", authUserNoNeedLoginUrls=" + authUserNoNeedLoginUrls + ", authUserCommonUrls=" + authUserCommonUrls
				+ ", defaultStaticResourceNamespace=" + defaultStaticResourceNamespace
				+ ", userStaticResourceNamespaces=" + userStaticResourceNamespaces + ", userLoginPage=" + userLoginPage
				+ ", authBackstageIndexUrl=" + authBackstageIndexUrl + ", i18nFileName=" + i18nFileName + "]";
	}
	
}
