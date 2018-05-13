package com.silent.template.backstage.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.silent.template.backstage.bean.vo.View;

/**
 * 首页Controller
 * @author TanJin
 * @date 2017-8-17
 */
@Controller("defaultIndexController")
@RequestMapping("/")
public class IndexController {

	@RequestMapping("/index")
	public View index(HttpServletRequest request, HttpServletResponse response) {
		return new View(null, false);
	}
}
