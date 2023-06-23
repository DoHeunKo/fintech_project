package com.ms.fintech.service;

import org.springframework.ui.Model;

import com.ms.fintech.apidtos.UserOobDto;
import com.ms.fintech.command.LoginCommand;
import com.ms.fintech.command.RegistCommand;


import jakarta.servlet.http.HttpServletRequest;

public interface IUserService {
	public String emailChk(String email);
	
	public void userRegist(RegistCommand registCommand);
	
	public String userLogin(LoginCommand loginCommand,HttpServletRequest request,Model model);
	
	public int withdraw(int userSeq);
	
	public String patternChk(int user_seq);
	
	public boolean pattern(int user_seq,UserOobDto odto);
}
