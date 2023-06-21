package com.ms.fintech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("manager")
public class ManagerController {
	
	@GetMapping("/managerMain")
	public String adminMain() {
		
		return "thymeleaf/manager/managerMain";
	}
}
