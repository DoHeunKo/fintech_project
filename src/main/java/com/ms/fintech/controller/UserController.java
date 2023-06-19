package com.ms.fintech.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.fintech.apidtos.UserMeDto;
import com.ms.fintech.command.LoginCommand;
import com.ms.fintech.dtos.RoomDto;
import com.ms.fintech.dtos.UserDto;
import com.ms.fintech.feign.AccountFeign;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private AccountFeign accountFeign;
	
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
		return "thymeleaf/community";
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
		return "thymeleaf/chatroom";
	}
	
	@GetMapping("/analysis")
	public String analysis() {
		return "thymeleaf/user/analysis";
	}

	@ResponseBody
	@GetMapping("/allmyinfo")
	public String myInfo(HttpServletRequest request){
//		public UserMeDto myInfo(HttpServletRequest request){
			System.out.println("나의 정보조회하기");
			
			HttpSession session=request.getSession();
			UserDto dto=(UserDto)session.getAttribute("dto");
			System.out.println(dto);
			
			//feign 인터페이스가 요청해주고 결과값도 받아준다
			UserMeDto userMeDto= accountFeign
					.requestUserMe("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxMTAxMDMyODM4Iiwic2NvcGUiOlsiaW5xdWlyeSIsImxvZ2luIiwidHJhbnNmZXIiXSwiaXNzIjoiaHR0cHM6Ly93d3cub3BlbmJhbmtpbmcub3Iua3IiLCJleHAiOjE2OTQ5MTU5MDQsImp0aSI6IjdmMjMwMWQzLWY2MzAtNDVkMi05MDBmLTYzNjVjYTU2MDMyZiJ9.Qn0KErtxaESGeyrcbVvua35VfFNk1K-3aSUJUwhY9Yc", dto.getUser_seq_no()+"");
			
//			System.out.println(userMeDto.getUser_name());
			//결과값을 받아서 request 스코프에 담아서 전달할 수 있다.
			System.out.println(userMeDto);
			request.setAttribute("userMeDto", userMeDto);
			
//			return userMeDto;	
			return "thymeleaf/user/analysis";
		}
}