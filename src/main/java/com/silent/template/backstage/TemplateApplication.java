package com.silent.template.backstage;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 模板后台工程自启动测试用的启动方法
 * @author TanJin
 * @date 2018年3月8日
 */
@SpringBootApplication
@EnableTransactionManagement	// 启用事务管理
public class TemplateApplication {

	public static void main(String[] args) throws Exception {
		BootstrapApplication application = new BootstrapApplication(TemplateApplication.class);
		application.start(args);
	}
}
