package com.ms.fintech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.ms.fintech.apidtos.AccountBalanceDto;
import com.ms.fintech.apidtos.UserCardinfoDto;
import com.ms.fintech.apidtos.UserOobDto;
import com.ms.fintech.command.LoginCommand;
import com.ms.fintech.command.RegistCommand;
import com.ms.fintech.dtos.AccountDto;
import com.ms.fintech.dtos.RoomDto;
import com.ms.fintech.dtos.UserDto;
import com.ms.fintech.dtos.UserTokenDto;
import com.ms.fintech.mapper.UserMapper;


import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService implements IUserService{
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public String emailChk(String email) {	
		return userMapper.emailChk(email);
	}

	@Override
	@Transactional
	public void userRegist(RegistCommand registCommand) {
		UserDto dto =new UserDto();
		dto.setEmail(registCommand.getEmail());
		dto.setPassword(registCommand.getPassword());
		dto.setName(registCommand.getName());
		dto.setPhone(registCommand.getPhone());
		dto.setAddress(registCommand.getAddress());
		dto.setSex(registCommand.getSex());
		dto.setIs_married(registCommand.getIs_married());
		dto.setAge(registCommand.getAge());
		dto.setJob(registCommand.getJob());
		dto.setUser_seq_no(registCommand.getUser_seq_no());
		boolean isS1= userMapper.userRegist(dto);
		if(isS1) {
			System.out.println("유저등록완료");
		}
		boolean isS2=userMapper.tokenRegist(new UserTokenDto(dto.getUser_seq(),registCommand.getScope(),registCommand.getToken()));
		if(isS2) {
			System.out.println("토큰 트랜잭션처리 완료");
		}
		
	}

	@Override
	public String userLogin(LoginCommand loginCommand, HttpServletRequest request,
	         Model model) {
	      UserDto dto=userMapper.userLogin(loginCommand.getEmail());
	      if(dto != null) {
	         if(loginCommand.getPassword().equals(dto.getPassword())) {
	            request.getSession().setAttribute("dto", dto);
	            if(dto.getRole().equals("MANAGER")){
	               return "redirect:/manager/managerMain";
	            } else {
	            	return "redirect:/user/userMain";
	            }
	            
	         }else {
	            System.out.println("비밀번호 틀림");
	            return "thymeleaf/loginForm";
	         }
	      }else {
	         System.out.println("회원 정보 없음, 회원가입 필요");
	         return "thymeleaf/loginForm";	
	      }
	   }
	@Override
	public int withdraw(int user_seq) {
		return userMapper.withdraw(user_seq);
	}
	
	@Override
	@Transactional
	public boolean pattern(int user_seq,UserOobDto odto) {
		boolean isS1=userMapper.pattern(user_seq);
		
		if(isS1) {
			System.out.println("분석완료");
		}
		
		UserTokenDto dto=new UserTokenDto();
		dto.setUser_seq(user_seq);
		dto.setScope(odto.getScope());
		dto.setToken(odto.getAccess_token());
		
		boolean isS2=userMapper.tokenRegist(dto);
		if(isS2) {
			System.out.println("oob토큰등록완료");
		}
		
		return true;
	}
	
	@Override
	@Transactional
	public boolean join(int user_seq, UserCardinfoDto cdto) {
		boolean isS1=userMapper.join(user_seq);
		if(isS1) {
			System.out.println("카드등록 및 참가완료");
		}
		
		UserTokenDto tdto=new UserTokenDto();
		tdto.setUser_seq(user_seq);
		tdto.setToken(cdto.getAccess_token());
		tdto.setScope(cdto.getScope());
		boolean isS2=userMapper.tokenRegist(tdto);
		if(isS2) {
			System.out.println("카드토큰 등록완료");
		}
		return true;
	}
	
	
	@Override
	public String patternChk(int user_seq) {
		
		return userMapper.patternChk(user_seq);
	}

	@Override
	public String joinChk(int user_seq) {
		
		return userMapper.joinChk(user_seq);
	}

	@Override
	public boolean registAccount(int user_seq, AccountBalanceDto abdto) {
		AccountDto adto=new AccountDto();
		adto.setUser_seq(user_seq);
		adto.setFintech_use_num(abdto.getFintech_use_num());
		adto.setBalance_amt(abdto.getBalance_amt());
		adto.setBank_name(abdto.getBank_name());
		return userMapper.registAccount(adto);
	}

	@Override
	public int accountChk(int user_seq) {
		
		return userMapper.accountChk(user_seq);
	}

	@Override
	public int oobChk(int user_seq) {
		
		return userMapper.oobChk(user_seq);
	}

	@Override
	@Transactional
	public boolean withdraw_deposit(
			int wd_user_seq,
			String wd_fintech_use_num,
			String tran_amt,
			int dp_user_seq,
			String dp_fintech_use_num) {
		//이체후 DB 갱신
		boolean addWithdraw=userMapper.insertWithdraw(
				wd_user_seq,wd_fintech_use_num,tran_amt);
		
		boolean addDeposit=userMapper.insertDeposit(
				dp_user_seq,dp_fintech_use_num,tran_amt);
		
		if(addWithdraw && addDeposit) {
			return true;
		}
		
		return false;
	}

	

	

	

	
	
}
