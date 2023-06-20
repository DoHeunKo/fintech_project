package com.ms.fintech.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ms.fintech.command.LoginCommand;
import com.ms.fintech.dtos.RoomDto;
import com.ms.fintech.dtos.UserDto;
import com.ms.fintech.service.IUserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	IUserService userService;
	
	@GetMapping("/userMain")
	public String userMain() {
		return "thymeleaf/user/userMain";
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/loginform";
	}
	@GetMapping("/myinfo")
	public String myinfo() {
		return "thymeleaf/user/myinfo";
	}
	
	@GetMapping("community")
	public String comunity(Model model) {
		var roomList = new ArrayList<RoomDto>();
		//TODO 관리자 페이지에서 추가 및 삭제 기능 구현
		roomList.add(RoomDto.builder().roomNo(1).roomTitle("거지방 1호").build());
		roomList.add(RoomDto.builder().roomNo(2).roomTitle("거지방 2호").build());
		roomList.add(RoomDto.builder().roomNo(3).roomTitle("거지방 3호").build());
		
		model.addAttribute("dtos", roomList);
		return "thymeleaf/user/community";
	}
	@GetMapping("community/chatroom")
	public String chatRoom(HttpSession session, Model model, @RequestParam("roomno") int roomNo, @ModelAttribute LoginCommand command) {
		model.addAttribute("roomNo", roomNo);
		UserDto dto = (UserDto)session.getAttribute("dto");
		if (dto == null) {
			model.addAttribute("userId", "unknown");
		} else {
			model.addAttribute("userId", dto.getEmail().split("@")[0]);
		}
		return "thymeleaf/user/chatroom";
	}
	@GetMapping("withdraw")
	public String withdraw(HttpSession session) {
		int user_seq = ((UserDto)session.getAttribute("dto")).getUser_seq();
		return userService.withdraw(user_seq) > 0 ? "redirect:/" : "redirect:/error";
	}
	
	
}