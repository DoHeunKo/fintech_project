package com.ms.fintech.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.fintech.apidtos.AccountBalanceDto;
import com.ms.fintech.apidtos.AccountTransactionDto;
import com.ms.fintech.apidtos.AccountTransactionListDto;
import com.ms.fintech.apidtos.DepositReqDto;
import com.ms.fintech.apidtos.DepositReqListDto;
import com.ms.fintech.apidtos.DepositResDto;
import com.ms.fintech.apidtos.UserCardinfoDto;
import com.ms.fintech.apidtos.UserMeAccountDto;
import com.ms.fintech.apidtos.UserMeCardDto;
import com.ms.fintech.apidtos.UserMeDto;
import com.ms.fintech.apidtos.UserOobDto;
import com.ms.fintech.apidtos.WithdrawResDto;
import com.ms.fintech.apidtos.WithdrawReqDto;
import com.ms.fintech.command.BalanceCommand;
import com.ms.fintech.command.CategoryCommand;
import com.ms.fintech.command.GraphData;
import com.ms.fintech.command.LoginCommand;
import com.ms.fintech.command.MonthCommand;
import com.ms.fintech.dtos.CardInfoDto;
import com.ms.fintech.dtos.RoomDto;
import com.ms.fintech.dtos.UserDto;
import com.ms.fintech.dtos.UserTokenDto;
import com.ms.fintech.service.IUserService;
import com.ms.fintech.feign.AccountFeign;
import com.ms.fintech.mapper.UserMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;
	@Autowired
	private AccountFeign accountFeign;
	@Autowired
	private UserMapper mapper;
	static int plus;
	static int minus;
	static int plus_cnt;

	
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
	
	@GetMapping("/community")
	public String comunity(Model model) {
		model.addAttribute("roomDtos", mapper.getRoomList());
		System.out.println(mapper.getRoomList().get(0).getRoomTitle());
		return "thymeleaf/user/community";
	}
	@GetMapping("/community/chatroom")
	public String chatRoom(HttpSession session, Model model, @RequestParam("roomno") int roomNo) {
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
	public List<BalanceCommand> info_balance(HttpServletRequest request,Model model){
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
			//눌렀을 때 계좌테이블에 있는 경우에는 db등록불가 하도록 처리
			
			int ret=userService.accountChk(dto.getUser_seq());
			
			if(ret<4) {
				userService.registAccount(dto.getUser_seq(), abdto);
			}
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
	@PostMapping("/oob_token")
	public boolean oob_token(HttpServletRequest request) {
		HttpSession session=request.getSession();
		UserDto dto=(UserDto)session.getAttribute("dto");
		
		//분석페이지 로딩시 패턴체크와 함께 oob토큰여부도 체크(중복삽입방지)
		//로그인시 가져온 토큰으로 확인
		int token_count=dto.getUserTokenDto().size();
		boolean flag=false;
		for(int i=0;i<token_count;i++) {
			if(dto.getUserTokenDto().get(i).getScope().equals("oob")) {
				flag=true;
			}
		}
		
		int oobCnt=userService.oobChk(dto.getUser_seq());
		
		if(oobCnt>0) {
			System.out.println("이미 oob등록");
			return false;
		}else {
			UserOobDto odto=accountFeign.requestOobToken(
					"4987e938-f84b-4e23-b0a2-3b15b00f4ffd", 
					"3ff7570f-fdfb-4f9e-8f5a-7b549bf2adec", 
					"oob", 
					"client_credentials");
			System.out.println(odto);
			
			System.out.println(dto.getUser_seq());
			userService.pattern(dto.getUser_seq(),odto);
			return true;
		}
		
		
	}
	
	
	@GetMapping("/card_token")
	public String card_token(HttpServletRequest request,String code) {
		System.out.println("컨트롤러: 카드토큰");
		System.out.println(code);
		HttpSession session=request.getSession();
		UserDto dto=(UserDto)session.getAttribute("dto");
		int token_count=dto.getUserTokenDto().size();
		boolean flag=false;
		
		for(int i=0;i<token_count;i++) {
			if(dto.getUserTokenDto().get(i).getScope().contains("cardinfo")) {
				flag=true;
			}
		}
		
		UserCardinfoDto cdto=accountFeign.requestCardToken(
				code,
				"4987e938-f84b-4e23-b0a2-3b15b00f4ffd",
				"3ff7570f-fdfb-4f9e-8f5a-7b549bf2adec",
				"http://localhost:8070/user/card_token",
				"authorization_code"
				);
		
		userService.join(dto.getUser_seq(), cdto);
		
		return "thymeleaf/user/authresult_card";
	}
	
	@ResponseBody
	@GetMapping("/pattern")
	public MonthCommand pattern(HttpServletRequest request,Model model) {
		HttpSession session=request.getSession();
		UserDto dto=(UserDto)session.getAttribute("dto");
		System.out.println(dto);
		MonthCommand monthCommand=new MonthCommand();
		List<UserTokenDto> userTokenList = dto.getUserTokenDto();
		System.out.println(userTokenList.size());
		String getInitialScope="inquiry";
		String token = null;
		UserMeDto userMeDto=null;
		for (UserTokenDto userToken : userTokenList) {
			System.out.println(userToken);
		    if (userToken.getScope().contains(getInitialScope)) {
		    	System.out.println("inquiry 찾음");
		    	
		        token = userToken.getToken();
		        System.out.println(token);
		        userMeDto= accountFeign
						.requestUserMe("Bearer "+token, dto.getUser_seq_no()+"");
		        break;
		    }
		}

		List<UserMeAccountDto> adto=userMeDto.getRes_list();
		System.out.println(adto.size());
		for(int i=0;i<adto.size();i++) {
			String bank_tran_id=dto.getClient_use_code()+'U'+createNum();
			String tran_dtime=getDateTime();
			System.out.println("account들어가기전 token");
			AccountTransactionListDto accountTransactionList=accountFeign.requestAccountTransactionList(
					"Bearer "+token,
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

		}

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

		return monthCommand;
	}

	@GetMapping("/transfer")
	public String transfer() {
		return "thymeleaf/user/transfer";
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
	
	@ResponseBody
	@GetMapping("/patternChk")
	public Map<String,String> patternChk(HttpServletRequest request) {
		HttpSession session=request.getSession();
		UserDto dto=(UserDto)session.getAttribute("dto");
		String result=userService.patternChk(dto.getUser_seq());
		
		System.out.println(result);
		Map<String,String> map=new HashMap<>();
		map.put("result", result);
		return map;
	}
	@ResponseBody
	@GetMapping("/joinChk")
	public Map<String,String> joinChk(HttpServletRequest request) {
		HttpSession session=request.getSession();
		UserDto dto=(UserDto)session.getAttribute("dto");
		String result=userService.joinChk(dto.getUser_seq());
		
		System.out.println(result);
		Map<String,String> map=new HashMap<>();
		map.put("result", result);
		return map;
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
		
		@GetMapping("/withdraw")
		public String withdraw(HttpSession session) {
			int user_seq = ((UserDto)session.getAttribute("dto")).getUser_seq();
			return userService.withdraw(user_seq) > 0 ? "redirect:/" : "redirect:/error";
		}
		
		@GetMapping("/createPW")
		public String createPW(HttpSession session, Model model) {
//			model.addAttribute("dto",((UserDto)session.getAttribute("dto")));
			return "thymeleaf/user/createPW";
		}
		@GetMapping("/setPW")
		public String setPW(HttpSession session, @RequestParam(name = "pw") String pw) {
			int userSeq = ((UserDto)session.getAttribute("dto")).getUser_seq();
			mapper.setPassword(userSeq, pw);
			return "thymeleaf/user/transfer";
		}
		
		@GetMapping("/AuthPW")
		public String AuthPW() {
			return "thymeleaf/user/authPW";
		}
		
		@GetMapping("/cardPWChk")
		@ResponseBody
		public boolean cardPWChk(HttpSession session) {
			int user_seq = ((UserDto)session.getAttribute("dto")).getUser_seq();
			String ret=mapper.cardPWChk(user_seq);
			if(ret==null) {
				System.out.println("비밀번호 설정 안함");
				return false;
			}else {
				return true;
			}
			
		}
		
		
		
		
		@GetMapping("/withdraw_deposit")
		public String withdraw_deposit(
				@RequestParam("dps_name")String dps_name,
				@RequestParam("dps_bank")String dps_bank,
				@RequestParam("dps_fintech_use_num")String dps_fintech_use_num,
				@RequestParam("dps_tran_amt")String dps_tran_amt,HttpServletRequest request) {
			
			
			HttpSession session=request.getSession();
			UserDto dto=(UserDto)session.getAttribute("dto");
			
			CardInfoDto cdto=mapper.getCardInfo(dto.getUser_seq());
			
			String bank_tran_id1=dto.getClient_use_code()+'U'+createNum();
			
			List<UserTokenDto> userTokenList = dto.getUserTokenDto();
			String getInitialScope1="inquiry";
			String getInitialScope2="oob";
			String wtoken = null;
			String dtoken=null;
			for (UserTokenDto userToken : userTokenList) {
				System.out.println(userToken);
			    if (userToken.getScope().contains(getInitialScope1)) {	    	
			        wtoken = userToken.getToken();
			        break;
			    }
			    if(userToken.getScope().contains(getInitialScope2)) {
			    	dtoken=userToken.getToken();
			    	break;
			    }
			}
			
			WithdrawReqDto wrdto=new WithdrawReqDto();
			wrdto.setBank_tran_id(bank_tran_id1);
			wrdto.setCntr_account_type("N");
			wrdto.setCntr_account_num("100000000003");
			wrdto.setDps_print_content("환불");
			wrdto.setFintech_use_num(cdto.getFintech_use_num());
			wrdto.setTran_amt("1000");
			wrdto.setTran_dtime(getDateTime());
			wrdto.setReq_client_name("dkjdkfj");
			wrdto.setReq_client_num("HONGGILDONG1234");
			wrdto.setTransfer_purpose("WD");
			wrdto.setReq_client_fintech_use_num(cdto.getFintech_use_num());
			
			WithdrawResDto wdto=accountFeign.requestWithdraw(
					"Bearer "+wtoken,
					wrdto);
//					dps_name,
//					"004",
//					"2314213324213333"
					

			System.out.println(wdto);
			
			String bank_tran_id2=dto.getClient_use_code()+'U'+createNum();

			//입금 -> 위에서 보냈을 때 받은 사람의 계좌(페이지에서 받는 계좌를 적어야함),홈페이지에 등록 필수
			DepositReqDto drdto=new DepositReqDto();
			drdto.setCntr_account_type("N");
			drdto.setCntr_account_num("200000000003");
			drdto.setWd_pass_phrase("NONE");
			drdto.setWd_print_content("송금");
			drdto.setName_check_option("off");
			drdto.setTran_dtime(getDateTime());
			drdto.setReq_cnt("1");
			
			DepositReqListDto drldto=new DepositReqListDto();
			drldto.setTran_no("1");
			drldto.setBank_tran_id(bank_tran_id2);
			drldto.setFintech_use_num(dps_fintech_use_num);
			drldto.setPrint_content("송금");
			drldto.setTran_amt("10000");
			drldto.setReq_client_fintech_use_num(dps_fintech_use_num);
			drldto.setReq_client_name(dps_name);
			drldto.setReq_client_num("HONGGILDONG1234");
			drldto.setTransfer_purpose("TR");
			
			List<DepositReqListDto> drldtos=new ArrayList<>();
			drldtos.add(drldto);
			
			drdto.setReq_list(drldtos);
			
			DepositResDto ddto=accountFeign.requestDeposit(
					"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJNMjAyMjAxODg2Iiwic2NvcGUiOlsib29iIl0sImlzcyI6Imh0dHBzOi8vd3d3Lm9wZW5iYW5raW5nLm9yLmtyIiwiZXhwIjoxNjk1MjU2MjA4LCJqdGkiOiIyMmFiMmY2OS1hNDhiLTRkOWItOTlhYS03NzA2MWQ4MjY1NmEifQ.TxYQA1EhGBYnOYoHpp4TyyCjg1UKat6c4kepIcgJGPI",
					drdto);
			System.out.println(ddto);
			return "redirect:/user/transfer";
		}
		
		@GetMapping("/linkAccount")
		public String linkAccount() {
			return "thymeleaf/user/linkAccount";
		}
		
		@GetMapping("/card_info")
		public String card_info(HttpSession session ,@RequestParam("fintech_use_num") String fintechUseNum ,@RequestParam("balance_amt") String balanceAmt, @RequestParam("selected") int selected) {
			var userDto= (UserDto)session.getAttribute("dto");
			UserMeDto userMeDto= accountFeign
					.requestUserMe("Bearer "+userDto.getUserTokenDto()
					.get(0).getToken(), userDto.getUser_seq_no()+"");
			
//			var cardDto = new UserMeCardDto();
//			cardDto.setBank_code_std("090");
//			cardDto.setMember_bank_code("090");
//			userMeDto.getInquiry_card_list().add(cardDto);
			var dto = CardInfoDto.builder().user_seq(userDto.getUser_seq()).
											fintech_use_num(fintechUseNum).
											balance_amt(balanceAmt).
											charge_month("202301").
											settlement_seq_no("1").
											member_bank_code(userMeDto.getInquiry_card_list().get(0).getMember_bank_code()).
											bank_code_std(userMeDto.getInquiry_card_list().get(0).getBank_code_std()).build();
			System.out.println(userMeDto);
			mapper.insertCardInfo(dto);
			return "thymeleaf/user/transfer";
		}
		
		
}