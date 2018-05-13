package com.silent.template.backstage.configurer;

import java.util.Properties;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.silent.framework.base.configurer.ResourceContext;
import com.silent.framework.base.utils.StringUtils;

/**
 * MyBatis自定义配置类
 * @author TanJin
 * @date 2018年3月12日
 */
@Configuration
public class MybatisConfigurerAdapter {
	protected static Logger logger = LoggerFactory.getLogger(MybatisConfigurerAdapter.class);
	
	/**
	 * 配置MyBatis扫描Mapper的包路径<br>
	 */
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer() {
		// 项目主配置文件
		Properties properties = ResourceContext.loadResourceConfigProperties("bootstarp-application.properties", "application.properties");
		// MyBatis默认扫描Mapper包路径
		String defaultMybatisMapperScanPackage = properties.getProperty("mybatis.mapper.default.scan.package");
		// MyBatis用户自定义扫描Mapper包路径
		String userMybatisMapperScanPackage = properties.getProperty("mybatis.mapper.user.scan.package");
		
		StringBuilder mapperScanPackage = new StringBuilder();
		if(StringUtils.isNotEmpty(defaultMybatisMapperScanPackage)) {
			mapperScanPackage.append(defaultMybatisMapperScanPackage);
		}
		if(StringUtils.isNotEmpty(userMybatisMapperScanPackage)) {
			mapperScanPackage.append(","); 
			mapperScanPackage.append(userMybatisMapperScanPackage);
		}
		
		logger.info("[MybatisConfigurerAdapter] [MapperScannerConfigurer] [mapper scan package =============>> {}]", mapperScanPackage.toString());
		
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage(mapperScanPackage.toString());
		return mapperScannerConfigurer;
	}
}
