package com.ms.fintech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import com.ms.fintech.command.LoginCommand;
import com.ms.fintech.command.RegistCommand;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class GuestController {
	
	@GetMapping("/")
	public String main() {
		return "thymeleaf/main";
	}
	
	@GetMapping("/loginform")
	public String loginform(Model model) {
		model.addAttribute("loginCommand",new LoginCommand());
		return "thymeleaf/loginform";
	}
	
	@GetMapping("/registform")
	public String registform(Model model) {
		model.addAttribute("registCommand",new RegistCommand());
//		model.addAttribute("loginCommand",new LoginCommand());
		return "thymeleaf/registform";
	}
	
//	@PostMapping("/login")
//	public String login(@Validated LoginCommand loginCommand,
//						BindingResult result,
//						HttpServletRequest request, Model model,HttpSession session) {
//		
//		if(result.hasErrors()) {
//			return "thymeleaf/user/loginForm";
//		}
//		session.setAttribute("id", loginCommand.getId());
//		return userService.userLogin(loginCommand, request,model);
//	}
	

//	@PostMapping(value="/regist")
//	public String regist(@Validated RegistCommand registCommand,
//						BindingResult result,
//						Model model) {
//		//유효값 처리
//		if(result.hasErrors()) {
//			return "thymeleaf/user/registForm";
//		}
//		try {
//			userService.userRegist(registCommand);
//			return "redirect:/user/loginForm";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "redirect:/user/registForm";
//		}
//	}
	
}
