package com.ms.fintech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("tech")
	public String tech() {
		
		return "index";
	}
}
