package com.ms.fintech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuestController {
	
	@GetMapping("/")
	public String main() {
		return "thymeleaf/main";
	}
}
