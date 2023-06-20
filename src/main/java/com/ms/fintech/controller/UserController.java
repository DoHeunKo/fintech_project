package com.ms.fintech.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.fintech.apidtos.AccountBalanceDto;
import com.ms.fintech.apidtos.UserMeAccountDto;
import com.ms.fintech.apidtos.UserMeDto;
import com.ms.fintech.command.BalanceCommand;
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
	@GetMapping("/info_balance")
	public List<BalanceCommand> info_balance(HttpServletRequest request){
		System.out.println("나의 정보조회 후 계좌별 잔액 조회하기");
		
		HttpSession session=request.getSession();
		UserDto dto=(UserDto)session.getAttribute("dto");
		System.out.println(dto);
		
		UserMeDto userMeDto= accountFeign
				.requestUserMe("Bearer "+dto.getUserTokenDto().get(0).getToken(), dto.getUser_seq_no()+"");
		List<UserMeAccountDto> adto=userMeDto.getRes_list();
		List<BalanceCommand> balanceCommands=new ArrayList<>();
		for(int i=0;i<adto.size();i++) {
			String bank_tran_id=dto.getClient_use_code()+'U'+createNum();
			String tran_dtime=getDateTime();
//			System.out.println(adto.get(i).getFintech_use_num());
			AccountBalanceDto abdto=accountFeign.requestAccountBalanceList("Bearer "+dto.getUserTokenDto().get(0).getToken()
					,bank_tran_id
					,adto.get(i).getFintech_use_num() 
					, tran_dtime);
			BalanceCommand balanceCommand=new BalanceCommand();
			balanceCommand.setBank_name(adto.get(i).getBank_name());
			balanceCommand.setFintech_use_num(adto.get(i).getFintech_use_num());
			balanceCommand.setBalance_amt(abdto.getBalance_amt());
			balanceCommands.add(balanceCommand);
			
//			System.out.println(abdto);
//			System.out.println(adto.get(i).getFintech_use_num()+"계좌 잔액: "+abdto.getBalance_amt());
		}
		return balanceCommands;
		
	}
	
	//이용기관 부여번호 9자리 생성하는 메서드
		public String createNum() {
			String createNum="";// "468345554"
			for (int i = 0; i < 9; i++) {
				createNum+=((int)(Math.random()*10))+"";
			}
			System.out.println("이용기관부여번호9자리생성:"+createNum);
			return createNum;
		}
		
		//현재시간 구하는 메서드
		public String getDateTime() {
			LocalDateTime now = LocalDateTime.now();
			
			String formatNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			
			return formatNow;
		}

}