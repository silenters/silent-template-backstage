package com.silent.template.backstage.constants;

/**
 * 公共参数
 * @date 2016-3-25
 */
public class AuthGlobalParam {
	
	/**
	 * 默认用户自定义配置文件：classpath:config/default.properties
	 */
	public static final String DEFAULT_PROPERTIES = "classpath:config/default.properties";
	
	/**
	 * JAR包中的用户自定义配置文件：classpath:config/config.properties
	 */
	public static final String CONFIG_PROPERTIES_IN_JAR = "classpath:config/config.properties";
	
	/**
	 * 文件系统中的用户自定义配置文件：file:config/config.properties
	 */
	public static final String CONFIG_PROPERTIES_FROM_LOCAL = "file:config/config.properties";
	
	/**
	 * 模板CDN链接key，该值会保存在上下文中，供前端css js寻址
	 */
	public static final String TEMPLATE_URL_CDN = "template_url_cdn";
	
	/**
	 * 项目上下文路径
	 */
	public static final String PROJECT_CONTEXT_PATH = "project_context_path"; 
	
	/**
	 * 后台首页访问路径
	 */
	public static final String BACKSTAGE_INDEX = "backstage_index";
	
	/**
	 * 页面参数前缀
	 */
	public static final String PAGE_ATTRIBUTE_PREFIX = "page.attribute.";
	
	/**
	 * 获取要加载的用户配置文件
	 * @param 
	 * @return
	 */
	public static String[] getConfigFiles() {
		return new String[] {
				DEFAULT_PROPERTIES, CONFIG_PROPERTIES_IN_JAR, CONFIG_PROPERTIES_FROM_LOCAL
		};
	}
	
	
}