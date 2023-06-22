package com.ms.fintech.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.digester.ArrayStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.fintech.apidtos.AccountBalanceDto;
import com.ms.fintech.apidtos.AccountTransactionDto;
import com.ms.fintech.apidtos.AccountTransactionListDto;
import com.ms.fintech.apidtos.UserMeAccountDto;
import com.ms.fintech.apidtos.UserMeDto;
import com.ms.fintech.command.BalanceCommand;
import com.ms.fintech.command.CategoryCommand;
import com.ms.fintech.command.GraphData;
import com.ms.fintech.command.LoginCommand;
import com.ms.fintech.command.MonthCommand;
import com.ms.fintech.dtos.RoomDto;
import com.ms.fintech.dtos.UserDto;
import com.ms.fintech.service.IUserService;
import com.ms.fintech.feign.AccountFeign;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;
	@Autowired
	private AccountFeign accountFeign;
	
	@ModelAttribute
	public void initDto(Model model) {
		model.addAttribute("role", "guest");
	}
	
	
//	static int entertainment;
//	static int transportation;
//	static int convenience;
//	static int food;
//	static int transfer;
//	static int cafe;
//	static int hobby;
//	static int etc;
	static int plus;
	static int minus;
	static int plus_cnt;
	static int [] month_plus=new int[12];
	static int [] month_minus=new int[12];

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
		System.out.println(userMeDto);
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
	
	@ResponseBody
	@GetMapping("/pattern")
	public MonthCommand pattern(HttpServletRequest request,Model model) {
		HttpSession session=request.getSession();
		UserDto dto=(UserDto)session.getAttribute("dto");
		System.out.println(dto);
		MonthCommand monthCommand=new MonthCommand();
		
		UserMeDto userMeDto= accountFeign
				.requestUserMe("Bearer "+dto.getUserTokenDto().get(0).getToken(), dto.getUser_seq_no()+"");
		List<UserMeAccountDto> adto=userMeDto.getRes_list();
		for(int i=0;i<adto.size();i++) {
			String bank_tran_id=dto.getClient_use_code()+'U'+createNum();
			String tran_dtime=getDateTime();
			AccountTransactionListDto accountTransactionList=accountFeign.requestAccountTransactionList(
					"Bearer "+dto.getUserTokenDto().get(0).getToken(),
					bank_tran_id, 
					adto.get(i).getFintech_use_num(), 
					"A", 
					"D", 
					"20230101", 
					"20230610", 
					"D", 
					getDateTime());
			System.out.println(accountTransactionList);
			per_account(accountTransactionList,monthCommand);
//			category(accountTransactionList);
		}
//		System.out.println("입금 : "+ plus+"원");
//		System.out.println("출금 : "+minus+"원");
//		System.out.println("유흥 :"+entertainment);
//		System.out.println("교통 : "+transportation);
//		System.out.println("편의점 :"+convenience);
//		System.out.println("식비 : "+food);
//		System.out.println("이체 : "+transfer);
//		System.out.println("카페 : "+cafe);
//		System.out.println("취미 : "+hobby);
//		System.out.println("기타 : "+etc);
//		int bal=plus-minus+4000000;
//		System.out.println("잔액 : "+bal);
//		for(int i=0;i<12;i++) {
//			System.out.println(i+1+"월 입금 : "+month_plus[i]+" 출금 : "+month_minus[i]);
//		}
		
		System.out.println(monthCommand.getCategoryCommand().size());
		int month_size=monthCommand.getCategoryCommand().size();
		for(int i=0;i<month_size;i++) {
			System.out.println(i+1+"월 카테고리별 소비"+monthCommand.getCategoryCommand().get(i));
		}
		System.out.println(monthCommand);
		GraphData graphdata=new GraphData();
		List<Integer> category_data=new ArrayList<>();
		List<String> category_labels=new ArrayList<>();
		for(int i=0;i<month_size;i++) {
			category_data.add(i, monthCommand.getCategoryCommand().get(i).getTotal()) ;
			category_labels.add(i+"월");
		}
		graphdata.setCategory_data(category_data);
		graphdata.setCategory_labels(category_labels);
//		List<Double> category_data=new ArrayList<>(8);
//		List<String> category_labels=Arrays.asList("entertainment","transportation",
//				"convenience","food","transfer","cafe","hobby","etc");
//		category_data.add((double) Math.round((double) ((double)entertainment/minus*100)));
//		category_data.add((double) Math.round((double) ((double)transportation/minus*100)));
//		category_data.add((double) Math.round((double) ((double)convenience/minus*100)));
//		category_data.add((double) Math.round((double) ((double)food/minus*100)));
//		category_data.add((double) Math.round((double) ((double)transfer/minus*100)));
//		category_data.add((double) Math.round((double) ((double)cafe/minus*100)));
//		category_data.add((double) Math.round((double) ((double)hobby/minus*100)));
//		category_data.add((double) Math.round((double) ((double)etc/minus*100)));
//		GraphData graphData = new GraphData(category_labels,category_data);
//		model.addAttribute("graphData",graphData);
		return monthCommand;
	}
	
	//계좌별 거래내역조회
	public void per_account(AccountTransactionListDto accountTransactionListDto,MonthCommand monthCommand) {
		int tran_size=accountTransactionListDto.getRes_list().size();
		//한 계좌의 거래내역
		for(int i=0;i<tran_size;i++) {
			AccountTransactionDto accountTransactionDto=accountTransactionListDto.getRes_list().get(i);
			//거래일자
			String tran_date_y=accountTransactionDto.getTran_date().substring(0,4);
			String tran_date_m=accountTransactionDto.getTran_date().substring(4,6);
			//월별 구분
			per_month(accountTransactionDto,monthCommand);
		}
		
	}
	
	//해당 월과 배열의 인덱스가 일치하는지
	public void per_month(AccountTransactionDto accountTransactionDto,MonthCommand monthCommand) {
		int tran_date_y=Integer.parseInt(accountTransactionDto.getTran_date().substring(0,4)) ;
		int tran_date_m=Integer.parseInt(accountTransactionDto.getTran_date().substring(4,6));
		if (tran_date_y == 2023) {
	        int monthIndex = tran_date_m - 1;

	        if (monthIndex >= 0 && monthIndex < 12) {
	           
	            CategoryCommand categoryCommand = null;

	           
	            if (monthCommand.getCategoryCommand().size() > monthIndex) {
	                categoryCommand = monthCommand.getCategoryCommand().get(monthIndex);
	            } else {
	              
	                categoryCommand = new CategoryCommand();
	                monthCommand.getCategoryCommand().add(monthIndex, categoryCommand);
	            }

	            per_category(accountTransactionDto, categoryCommand);
	        }
	    }
		
		
	}
	
	public void per_category(AccountTransactionDto accountTransactionDto,CategoryCommand categoryCommand) {
		String tran_type=accountTransactionDto.getTran_type();
		String input_type=accountTransactionDto.getInout_type();
		int money=Integer.parseInt(accountTransactionDto.getTran_amt());
		if(input_type.equals("입금")) {
			int plus=categoryCommand.getPlus();
			plus+=money;
			categoryCommand.setPlus(plus);
		}
		if(input_type.equals("출금")) {
			int total=categoryCommand.getTotal();
			total+=money;
			categoryCommand.setTotal(total);
			if(tran_type.equals("유흥")) {
				 int entertainment = categoryCommand.getEntertainment();
				entertainment+=money;
				categoryCommand.setEntertainment(entertainment);
			}else if(tran_type.equals("교통")) {
				int transportation = categoryCommand.getTransportation();
				transportation+=money;
				categoryCommand.setTransportation(transportation);
			}else if(tran_type.equals("편의점")) {
				int convenience=categoryCommand.getConvenience();
				convenience+=money;
				categoryCommand.setConvenience(convenience);
			}else if(tran_type.equals("식비")) {
				int food=categoryCommand.getFood();
				food+=money;
				categoryCommand.setFood(food);
			}else if(tran_type.equals("이체")) {
				int transfer=categoryCommand.getTransfer();
				transfer+=money;
				categoryCommand.setTransfer(transfer);
			}else if(tran_type.equals("카페")) {
				int cafe=categoryCommand.getCafe();
				cafe+=money;
				categoryCommand.setCafe(cafe);
			}else if(tran_type.equals("취미")) {
				int hobby=categoryCommand.getHobby();
				hobby+=money;
				categoryCommand.setHobby(hobby);
			}else {
				int etc=categoryCommand.getEtc();
				etc+=money;
				categoryCommand.setEtc(etc);
			}	
		}
		
	}
	
	//매달의 소비 카테고리, 총 소비
//	public void per_month(AccountTransactionListDto accountTransactionList,MonthCommand monthCommand) {
//		int tranlist_size=accountTransactionList.getRes_list().size();
//		List<AccountTransactionDto> atdto=accountTransactionList.getRes_list();
//		
//			for(int j=0;j<month_plus.length;j++) {
//				for(int i=0;i<tranlist_size;i++) {
//					String inout_type=atdto.get(i).getInout_type();
//					String tran_date=atdto.get(i).getTran_date();
//					int tran_date_y=Integer.parseInt(atdto.get(i).getTran_date().substring(0, 4));
//					int tran_date_m=Integer.parseInt(atdto.get(i).getTran_date().substring(4, 6));
//					int tran_date_d=Integer.parseInt(atdto.get(i).getTran_date().substring(6,8));
//					String tran_type=atdto.get(i).getTran_type();
//					int money=Integer.parseInt(atdto.get(i).getTran_amt());
//					
//					if(inout_type.equals("입금")) {
//						if(tran_date_y==2023 && tran_date_m==j+1) {
//							
//							month_plus[j]+=money;
//						}
//					}else {
//						if(tran_date_y==2023 && tran_date_m==j+1) {
//							
//							category(tran_type,money);
//							month_minus[j]+=money;
//							
//							
//						}
//					}
//			}
//		}
//	}
//	public void category(String tran_type,int money) {
//		if(tran_type.equals("유흥")) {
//			entertainment+=money;
//		}else if(tran_type.equals("교통")) {
//			transportation+=money;
//		}else if(tran_type.equals("편의점")) {
//			convenience+=money;
//		}else if(tran_type.equals("식비")) {
//			food+=money;
//		}else if(tran_type.equals("이체")) {
//			transfer+=money;
//		}else if(tran_type.equals("카페")) {
//			cafe+=money;
//		}else if(tran_type.equals("취미")) {
//			hobby+=money;
//		}else {
//			etc+=money;
//		}	
//	}

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
		
		@GetMapping("withdraw")
		public String withdraw(HttpSession session) {
			int user_seq = ((UserDto)session.getAttribute("dto")).getUser_seq();
			return userService.withdraw(user_seq) > 0 ? "redirect:/" : "redirect:/error";
		}

		
}