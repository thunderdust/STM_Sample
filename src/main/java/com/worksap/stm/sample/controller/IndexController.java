package com.worksap.stm.sample.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.worksap.stm.sample.service.spec.UserService;


@Controller
public class IndexController {
	private static final String APP_NAME = "Book Store Application";
	
	@Autowired
	private UserService userService;

	@RequestMapping(value={"/", "/index"})
	public String index(Model model, HttpSession session) {
		model.addAttribute("appName", APP_NAME);
		return "index";
	}
}
