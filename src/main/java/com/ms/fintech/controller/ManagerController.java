package com.ms.fintech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.fintech.dtos.UserDto;
import com.ms.fintech.mapper.ManagerMapper;
import com.ms.fintech.mapper.UserMapper;

@Controller
@RequestMapping("manager")
public class ManagerController {
	@Autowired
	private UserMapper mapper;
	@Autowired 
	private ManagerMapper managerMapper;
	
	@ModelAttribute
	public void getUserList(Model model) {
		model.addAttribute("userDtos",mapper.getUserList());
		model.addAttribute("roomDto",mapper.getRoomList());
		model.addAttribute("newsDto",mapper.getNewsList());
	}
	
	@GetMapping("/managerMain")
	public String adminMain() {
		return "thymeleaf/manager/managerMain";
	}
	
	@GetMapping("/userinfo")
	public String userInfo(@RequestParam int seq, Model model) {
		var userDto = mapper.getUser(seq);
		model.addAttribute("userDto", userDto);
		return "thymeleaf/manager/userinfo";
	}
	
	@GetMapping("/withdraw")
	public void withdraw(@RequestParam int seq) {
		mapper.withdraw(seq);
	}
	
	
	@GetMapping("/getpagedList")
	public void getPagedList(@RequestParam("page") int page, Model model) {
		model.addAttribute("pagedNewsList",managerMapper.getPagedList(page));
	}
}
