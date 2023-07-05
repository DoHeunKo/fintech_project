package com.ms.fintech.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.fintech.apidtos.AccountTransactionDto;
import com.ms.fintech.apidtos.AccountTransactionListDto;
import com.ms.fintech.apidtos.UserMeAccountDto;
import com.ms.fintech.apidtos.UserMeDto;
import com.ms.fintech.command.CategoryCommand;
import com.ms.fintech.command.LoginCommand;
import com.ms.fintech.command.MonthCommand;
import com.ms.fintech.command.RegistCommand;
import com.ms.fintech.command.UserRankingCommand;
import com.ms.fintech.dtos.CrawlerDto;
import com.ms.fintech.dtos.UserDto;
import com.ms.fintech.feign.AccountFeign;
import com.ms.fintech.mapper.UserMapper;
import com.ms.fintech.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class GuestController {
	
	@Autowired 
	private IUserService userService;
	@Autowired
	private AccountFeign accountFeign;
	
	@Autowired
	private UserMapper mapper;
	
	
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
	
	@GetMapping("/emailChk")
	@ResponseBody
	public Map<String,String> emailChk(String email){
		if(email=="") {
			System.out.println("공백");
		}else {
			System.out.println(email);
		}
		
		String resultEmail=userService.emailChk(email);
		Map<String,String> map=new HashMap<>();
		map.put("email", resultEmail);
		return map;
	}
	
	@GetMapping("/authresult")
    public String authResult(String code,Model model) throws IOException, ParseException{
        System.out.println("인증후 코드 :"+ code);

        HttpURLConnection conn=null;
        JSONObject result=null;

        URL url=new URL("https://testapi.openbanking.or.kr/oauth/2.0/token?"
                + "code="+code
                + "&client_id=4987e938-f84b-4e23-b0a2-3b15b00f4ffd"
                + "&client_secret=3ff7570f-fdfb-4f9e-8f5a-7b549bf2adec"
                + "&redirect_uri=http://localhost:8070/authresult"
                + "&grant_type=authorization_code");

        conn=(HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);//데이터를 가져오려면 true로 설정

        BufferedReader br=new BufferedReader(
                            new InputStreamReader(conn.getInputStream(),"utf-8"));

        StringBuilder response=new StringBuilder();
        String responseLine=null;
        while((responseLine=br.readLine())!=null) {
            System.out.println(responseLine.trim());
            response.append(responseLine.trim());
        }

        //json형태의 텍스트 데이터를 json객체로 변환시키는 작업
        result=(JSONObject) new JSONParser().parse(response.toString());

        //json객체에서 결과값을 가져오기
        String token=result.get("access_token").toString();
        String user_seq_no=result.get("user_seq_no").toString();
        String scope=result.get("scope").toString();
        
        System.out.println("token:"+ token);
        System.out.println("user_seq_no:"+ user_seq_no);
        System.out.println("scope:"+scope);

        //클라이언트로 전달
        model.addAttribute("token",token);
        model.addAttribute("user_seq_no",user_seq_no);
        model.addAttribute("scope",scope);
//        String str="<script type='text/javascript'>"
//				 + "    self.close();"
//				 + "</script>";
//		return str;
        return "thymeleaf/authresult";
    }
	
	@PostMapping("/login")
	public String login(@Validated LoginCommand loginCommand,
						BindingResult result,
						HttpServletRequest request, Model model,HttpSession session) {
		
		if(result.hasErrors()) {
			return "thymeleaf/loginform";
		}
		System.out.println(loginCommand.getEmail());
		session.setAttribute("email", loginCommand.getEmail());
		return userService.userLogin(loginCommand, request,model);
	}
//	

	@PostMapping(value="/regist")
	public String regist(@Validated RegistCommand registCommand,
						BindingResult result,
						Model model) {
		//유효값 처리
//		System.out.println(registCommand.getSex());
//		System.out.println(registCommand.getAge());
//		System.out.println(registCommand.getUser_seq_no());
//		System.out.println(registCommand.getToken());
//		System.out.println(registCommand.getScope());
		if(registCommand.getToken()==""||registCommand.getScope()=="") {
			return "thymeleaf/registform";
		}
		if(result.hasErrors()) {
			System.out.println("유효값처리 오류");
			model.addAttribute("registCommand", registCommand);
			return "thymeleaf/registform";
		}
		try {
			userService.userRegist(registCommand);
			System.out.println("회원가입 완료 여부");
			return "redirect:/loginform";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/registform";
		}
	}
	@GetMapping("/mznews")
	public String mznews(Model model) {
		List<CrawlerDto> list;
		list = mapper.getNewsList();
		model.addAttribute("crawlerDto", list);
		
		return "thymeleaf/mznews";
	}
	
	@GetMapping("/ranking")
	public String ranking() {
		List<List<Integer>> month_total=new ArrayList<>();
		List<UserDto> ulist=mapper.rankingUserInfo();
		System.out.println(ulist);
		String client_use_code="M202201886";
		int user_cnt=ulist.size();
		for(int i=0;i<user_cnt;i++) {
			UserMeDto userMeDto=accountFeign.requestUserMe(
					"Bearer "+ ulist.get(i).getUserTokenDto().get(0).getToken(),
					ulist.get(i).getUser_seq_no());
			
			List<UserMeAccountDto> adto=userMeDto.getRes_list();

			String tran_dtime=getDateTime();
			int plus=0;
			int minus=0;
			for(int j=0;j<adto.size();j++) {
				String bank_tran_id=client_use_code+'U'+createNum();
				AccountTransactionListDto atdto=accountFeign.requestAccountTransactionList(
						"Bearer "+ ulist.get(i).getUserTokenDto().get(0).getToken(), 
						bank_tran_id, 
						adto.get(i).getFintech_use_num(), 
						"A", 
						"D", 
						"20230101", 
						"20230610", 
						"D", 
						getDateTime());
				System.out.println(atdto);
				per_account(atdto,month_total,plus,minus);
				
			}
			System.out.println(i+"번쨰 유저 수입 :"+plus+" 소비 : "+minus);
			
			
		}
		System.out.println(month_total);
		return "thymeleaf/ranking";
	}
	
	public void per_account(AccountTransactionListDto accountTransactionListDto,List<List<Integer>> month_total
			,int plus,int minus) {
		int tran_size=accountTransactionListDto.getRes_list().size();
		//한 계좌의 거래내역
		for(int i=0;i<tran_size;i++) {
			AccountTransactionDto accountTransactionDto=accountTransactionListDto.getRes_list().get(i);
			//거래일자
			String tran_date_y=accountTransactionDto.getTran_date().substring(0,4);
			String tran_date_m=accountTransactionDto.getTran_date().substring(4,6);
			//월별 구분
//			per_month(accountTransactionDto,month_total);
			
    		String input_type=accountTransactionDto.getInout_type();
    		int money=Integer.parseInt(accountTransactionDto.getTran_amt());
    		if(input_type.equals("입금")) {
    			plus+=money;
    		}else {
    			minus+=money;
    		}
		}
		
	}
	
	//해당 월과 배열의 인덱스가 일치하는지
	public void per_month(AccountTransactionDto accountTransactionDto,List<List<Integer>> month_total) {
		int tran_date_y=Integer.parseInt(accountTransactionDto.getTran_date().substring(0,4)) ;
		int tran_date_m=Integer.parseInt(accountTransactionDto.getTran_date().substring(4,6));
		if (tran_date_y == 2023) {
	        int monthIndex = tran_date_m - 1;

	        if (monthIndex >= 0 && monthIndex < 12) {
	           
	        	if (monthIndex >= 0 && monthIndex < 12) {
	                List<Integer> monthData = null;

	                if (month_total.size() > monthIndex) {
	                    monthData = month_total.get(monthIndex);
	                } else {
	                    monthData = new ArrayList<>();
	                    month_total.add(monthIndex, monthData);
	                }
	                	
	                String tran_type=accountTransactionDto.getTran_type();
	        		String input_type=accountTransactionDto.getInout_type();
	        		int money=Integer.parseInt(accountTransactionDto.getTran_amt());
	        		monthData.add(money);
	        }
	        }
	    }
		
		
	}
		
	@GetMapping("/error")
	public String error() {
		return "thymeleaf/error";
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
