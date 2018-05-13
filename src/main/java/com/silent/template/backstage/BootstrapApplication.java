package com.silent.template.backstage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

/**
 * Spring Boot启动类
 * @author TanJin
 * @date 2018年1月5日
 */
public class BootstrapApplication {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 要扫描的包名列表
	 */
	private List<Class<?>> scanBasePackageList = new ArrayList<Class<?>>(); 
	
	/**
	 * 后台模板工程默认配置文件
	 */
	private String bootstarpApplicationProperties = "bootstarp-application.properties";
	
	/**
	 * sources:用户后台系统服务启动类Class
	 * @param sources
	 */
	public BootstrapApplication(Class<?> source) {
		this.addScanBasePackage(TemplateComponentScan.class);
		this.addScanBasePackage(source);
	}
	
	/**
	 * 启动
	 * @throws IOException 
	 */
	public void start(String[] args) throws IOException {
		logger.info("[application] [method] [start]");

		// 加载默认的SpringBoot配置文件信息
		Properties properties = new Properties();
		URL url = BootstrapApplication.class.getClassLoader().getResource(bootstarpApplicationProperties);
		properties.load(url.openStream());
		
		Class<?>[] classes = new Class<?>[this.scanBasePackageList.size()];
		for(int i = 0; i < this.scanBasePackageList.size(); i++) {
			classes[i] = this.scanBasePackageList.get(i);
		}
		
		SpringApplication application = new SpringApplication(classes);
		application.setBannerMode(Mode.LOG);
		application.setDefaultProperties(properties);
		Environment env = application.run(args).getEnvironment();
		
		String applicationName = env.getProperty("spring.application.name");
		String port = env.getProperty("server.port");
		String serverContextPath = env.getProperty("server.context-path");
		logger.info("[application] [{}] [serverContextPath:{}] [port:{}] [start]", applicationName, serverContextPath, port);
	}
	
	/**
	 * 添加要扫描的包名
	 * @param clazz 将扫描该class的包名下的所有类	
	 */
	public void addScanBasePackage(Class<?> clazz) {
		this.scanBasePackageList.add(clazz);
	}
	
}
