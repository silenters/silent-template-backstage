package com.silent.template.backstage.i18n;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.silent.framework.base.configurer.ResourceContext;
import com.silent.framework.base.utils.StringUtils;

/**
 * 自定义的国际化信息类
 * @author xuliang
 */
public class I18NMessageSource extends ResourceBundleMessageSource {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public I18NMessageSource() {
		/**
		 * 自定义ClassLoader
		 * 因为ClassLoader只能获取classpath里面的文件，没找到获取文件系统的方法
		 * 所以这里对他进行了复写
		 */
		setBundleClassLoader(new ClassLoader(super.getBundleClassLoader()) {
			@Override
			public URL getResource(String name) {
				URL url = ResourceContext.findResource(name);
				if(url == null) {
					url = super.findResource(name);
				}
				logger.debug("[i18n] [name:{}] [url:{}]", name, url == null ? "null" : url.toString());
				return url;
			}
		});
	}
	
	/**
	 * 设置国际化文件的前缀名
	 */
	@Override
	public void setBasenames(String... basenames) {
		List<String> list = new ArrayList<String>();
		if (basenames != null) {
			for (String name : basenames) {
				if(StringUtils.isEmpty(name)) {
					continue;
				}
				String[] array = name.split(",");
				for(String temp : array) {
					list.add(temp);
				}
			}
		}
		logger.info("[i18n] [setBasenames] [{}]", list);
		super.setBasenames(list.toArray(basenames));
	}
}