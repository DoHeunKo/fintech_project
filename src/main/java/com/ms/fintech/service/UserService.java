package com.ms.fintech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.ms.fintech.command.LoginCommand;
import com.ms.fintech.command.RegistCommand;
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
	public String userLogin(LoginCommand loginCommand,HttpServletRequest request,
			Model model) {
			UserDto dto=userMapper.userLogin(loginCommand.getEmail());
//			System.out.println("화면비밀번호"+loginCommand.getPassword());
//			System.out.println(dto.getAddress());
//			System.out.println("DB비밀번호" +dto.getPassword());
			if(dto !=null) {
				if(loginCommand.getPassword().equals(dto.getPassword())) {
					request.getSession().setAttribute("dto", dto);
					return "thymeleaf/user/userMain";
				}
				else {
					System.out.println("비밀번호오류");
					return "thymeleaf/loginform";
				}
			}
			else {
				
				System.out.println("회원정보없음");
				return "thymeleaf/loginform";
			}
		
	}
	

	
	
}
