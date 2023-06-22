package com.ms.fintech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ms.fintech.mapper.UserMapper;

@Controller
@RequestMapping("manager")
public class ManagerController {
	@Autowired
	private UserMapper mapper;
	
	@ModelAttribute
	public void getUserList(Model model) {
		model.addAttribute("userDto",mapper.getUserList());
		model.addAttribute("roomDto",mapper.getRoomList());
		model.addAttribute("newsDto",mapper.getNewsList());
	}
	
	@GetMapping("/managerMain")
	public String adminMain() {
		return "thymeleaf/manager/managerMain";
	}
}
