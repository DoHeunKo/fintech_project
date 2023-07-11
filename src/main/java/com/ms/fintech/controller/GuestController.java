package com.ms.fintech.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import com.ms.fintech.apidtos.CardDetailListDto;
import com.ms.fintech.apidtos.UserMeAccountDto;
import com.ms.fintech.apidtos.UserMeDto;
import com.ms.fintech.command.CategoryCommand;
import com.ms.fintech.command.LoginCommand;
import com.ms.fintech.command.MonthCommand;
import com.ms.fintech.command.RankingCommand;
import com.ms.fintech.command.RegistCommand;
import com.ms.fintech.command.UserRankingCommand;
import com.ms.fintech.dtos.CardInfoDto;
import com.ms.fintech.dtos.CrawlerDto;
import com.ms.fintech.dtos.UserDto;
import com.ms.fintech.dtos.UserTokenDto;
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
	public String ranking(HttpSession session,Model model) {
//		List<List<Integer>> month_total=new ArrayList<>();
		List<UserDto> ulist=mapper.rankingUserInfo();
		int user_cnt=ulist.size();
//		System.out.println(ulist);
		List<String> ids=new ArrayList();
		for(int i=0;i<user_cnt;i++) {
			String user_email=ulist.get(i).getEmail();
			int atIndex =user_email.indexOf('@');
			System.out.println(user_email.substring(0, atIndex));
			ids.add(user_email.substring(0, atIndex));
		}
		
		model.addAttribute("ids",ids);
		
		List<RankingCommand> rankInfo=new ArrayList<>();
//		System.out.println(ulist);
		String client_use_code="M202201886";
		
		for(int i=0;i<user_cnt;i++) {
			String inq_token=null;
			String card_token=null;
			String getInitialScope1="inquiry";
			String getInitialScope2="cardinfo";
			
			
			List<UserTokenDto> wd_userTokenList = ulist.get(i).getUserTokenDto();
//			System.out.println(wd_userTokenList);
			for (UserTokenDto userToken : wd_userTokenList) {
//				System.out.println(userToken);
			    if (userToken.getScope().contains(getInitialScope1)) {	    	
			        inq_token = userToken.getToken();
			        
			    }
			    else if(userToken.getScope().contains(getInitialScope2)) {
			    	card_token=userToken.getToken();	
			    }
			}    
//			System.out.println(inq_token);
//			System.out.println(card_token);
			UserMeDto userMeDto=accountFeign.requestUserMe(
					"Bearer "+ inq_token,
					ulist.get(i).getUser_seq_no());
			
			List<UserMeAccountDto> adto=userMeDto.getRes_list();
//			System.out.println(adto);
//			System.out.println(adto.size());
//			String tran_dtime=getDateTime();
//			int plus=0;
//			int minus=0;
			int [] month_plus=new int[12];
			int [] month_minus=new int[12];
			
			for(int j=0;j<adto.size();j++) {
				String bank_tran_id=client_use_code+'U'+createNum();
				AccountTransactionListDto atdto=accountFeign.requestAccountTransactionList(
						"Bearer "+ inq_token, 
						bank_tran_id, 
						adto.get(j).getFintech_use_num(), 
						"A", 
						"D", 
						"20230101", 
						"20230610", 
						"D", 
						getDateTime());
//				System.out.println(atdto);
				per_account(atdto,month_plus,month_minus);
				
		
			}
				
//			System.out.println(Arrays.toString(month_plus));
//			System.out.println(Arrays.toString(month_minus));
			int plus_sum=0;
			int minus_sum=0;
			int current_month=Integer.parseInt(getDateTime().substring(4,6));
			//총 수입,소비 합
			//최소소비 %
			double min_cur=100.0;
			for(int j=0;j<current_month-2;j++) {
				plus_sum+=month_plus[j];
				minus_sum+=month_minus[j];
				double min_percent=(Math.round((double)month_minus[j]/month_plus[j]*100*10))/10.0;
//				System.out.println(min_percent);
				if(min_cur>min_percent && min_percent!=0.0) {
					min_cur=min_percent;
				}
			}
//			System.out.println("최소 소비 % : "+min_cur);
			//평균
			int plus_avg=plus_sum/current_month-1;
			int minus_avg=minus_sum/current_month-1;
			//메일
			String email=ulist.get(i).getEmail();
			
			//전체소비퍼센트
			double total_cs_percent=(Math.round((double)minus_avg/plus_avg*100*10))/10.0;
//			double total_cs_percent=(Math.round(total_cs_percent1*10))/10.0;
			//전달소비퍼센트 (일단 5월로)
			double last_cs_percent=(Math.round((double)month_minus[current_month-2]/month_plus[current_month-2]*100*10))/10.0;
			
			//카드토큰을 이용해서 카드 상세 청구조회해서 이번달 소비% 수입은 평균수입으로 고정
			CardInfoDto cdto=mapper.rankingCardInfo(ulist.get(i).getUser_seq());
			
			String bank_tran_id=client_use_code+'U'+createNum();
			CardDetailListDto cardDetaildto=accountFeign.requestCardList(
					"Bearer "+card_token, 
					bank_tran_id ,
					ulist.get(i).getUser_seq_no(), 
					cdto.getBank_code_std(), 
					cdto.getMember_bank_code(), 
					cdto.getCharge_month(), 
					cdto.getSettlement_seq_no());
			
			int cardDetail_size=cardDetaildto.getBill_detail_list().size();
			//이번달 사용금액
			int this_month=0;
			for(int j=0;j<cardDetail_size;j++) {
				this_month+=Integer.parseInt(cardDetaildto.getBill_detail_list().get(i).getPaid_amt());
			}
//			System.out.println(this_month);
			//이번달 퍼센트
			double this_month_percent=(Math.round(((double)this_month/plus_avg*100*10)))/10.0;
			//남은 금액 (전체평균소비%-5%- 이번달 사용금액)
			double this_month_balance1=Math.ceil((minus_avg - (minus_avg * 0.05) - this_month) / 100) * 100;
			int this_month_balance=(int) this_month_balance1;
//			System.out.println(this_month_balance);
			
			DecimalFormat formatter = new DecimalFormat("#,###");
	        String formattedNumber = formatter.format(this_month_balance);
			
			RankingCommand rc=new RankingCommand();
			rc.setEmail(email);
			rc.setTotal_cs_percent(total_cs_percent);
			rc.setLast_cs_percent(last_cs_percent);
			rc.setThis_month_percent(this_month_percent);
			rc.setThis_month_balance(formattedNumber);
			rc.setMin_cur(min_cur);
			rankInfo.add(rc);
//			System.out.println(rc);
		}
			
		
		Collections.sort(rankInfo, Comparator.comparingDouble(RankingCommand::getThis_month_percent));

        // Print the sorted list
        for (RankingCommand command : rankInfo) {
//            System.out.println(command.getEmail()+" : "+command.getThis_month_percent());
        }
        
        model.addAttribute("rankinfo", rankInfo);
		return "thymeleaf/ranking";
	}
	
	public void per_account(AccountTransactionListDto accountTransactionListDto
			,int [] month_plus
			,int [] month_minus) {
		int tran_size=accountTransactionListDto.getRes_list().size();
		//한 계좌의 거래내역
		for(int i=0;i<tran_size;i++) {
			AccountTransactionDto accountTransactionDto=accountTransactionListDto.getRes_list().get(i);
			//거래일자
			int tran_date_y=Integer.parseInt(accountTransactionDto.getTran_date().substring(0,4)) ;
			int tran_date_m=Integer.parseInt(accountTransactionDto.getTran_date().substring(4,6));
			//월별 구분
			String tran_type=accountTransactionDto.getTran_type();
			String input_type=accountTransactionDto.getInout_type();
			int money=Integer.parseInt(accountTransactionDto.getTran_amt());
			
			if(tran_date_y==2023) {
				
				int monthIndex=tran_date_m-1;
//				if (monthIndex >= 0 && monthIndex < 12) {
					if(input_type.equals("입금")) {
						month_plus[monthIndex]+=money;
					}
					else {
						month_minus[monthIndex]+=money;
					}
//				}
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
		
	@GetMapping("/errorpage")
	public String error() {
		return "thymeleaf/error";
	}
	//이용기관 부여번호 9자리 생성하는 메서드
			public String createNum() {
				String createNum="";// "468345554"
				for (int i = 0; i < 9; i++) {
					createNum+=((int)(Math.random()*10))+"";
				}
//				System.out.println("이용기관부여번호9자리생성:"+createNum);
				return createNum;
			}
			
			//현재시간 구하는 메서드
			public String getDateTime() {
				LocalDateTime now = LocalDateTime.now();
				
				String formatNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
				
				return formatNow;
			}
}
