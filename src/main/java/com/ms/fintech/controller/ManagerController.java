package com.ms.fintech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public void getUserList(Model model,
			@RequestParam(defaultValue = "0", name = "page") Integer page,
			@RequestParam(defaultValue = "0", name = "Rpage") Integer Rpage,
			@RequestParam(defaultValue = "0", name = "preOrNext") String preOrNext) {
		model.addAttribute("userDtos",mapper.getUserList());
		if (preOrNext.equals("p")) {
			page -= 2;
		} else if (preOrNext.equals("n")) {
			page += 2;
		}
		if (Rpage < 0 || page < 0) {
			page = 0;
			Rpage = 0;
		}
		model.addAttribute("page",page);
		model.addAttribute("Rpage",page);
		model.addAttribute("newsDto", managerMapper.getPagedList(page * 6));
		model.addAttribute("roomDto", managerMapper.getPagedRoom(Rpage * 4));
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
	public String withdraw(@RequestParam int seq) {
		mapper.withdraw(seq);
		return "redirect:/";
	}

	@GetMapping("/deleteNews")
	public String deleteNews(@RequestParam int seq) {
		managerMapper.deleteNews(seq);
		return "redirect:/";
	}
	@GetMapping("/deleteRoom")
	public String deleteRoom(@RequestParam int seq) {
		managerMapper.deleteRoom(seq);
		return "redirect:/";
	}
	@GetMapping("/createRoom")
	@ResponseBody
	public String createRoom(@RequestParam String title) {
		managerMapper.createRoom(title);
		System.out.println(title);
		return title;
	}
	@GetMapping("/createform")
	public String createForm() {
		return "thymeleaf/manager/createform";
	}
	
}
