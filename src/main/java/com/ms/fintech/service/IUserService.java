package com.ms.fintech.service;

import org.springframework.ui.Model;

import com.ms.fintech.apidtos.AccountBalanceDto;
import com.ms.fintech.apidtos.UserCardinfoDto;
import com.ms.fintech.apidtos.UserOobDto;
import com.ms.fintech.command.LoginCommand;
import com.ms.fintech.command.RegistCommand;
import com.ms.fintech.dtos.UserTokenDto;

import jakarta.servlet.http.HttpServletRequest;

public interface IUserService {
	public String emailChk(String email);
	
	public void userRegist(RegistCommand registCommand);
	
	public String userLogin(LoginCommand loginCommand,HttpServletRequest request,Model model);
	
	public int withdraw(int userSeq);
	
	public String patternChk(int user_seq);
	
	public String joinChk(int user_seq);
	
	public boolean pattern(int user_seq,UserOobDto odto);
	
	public boolean join(int user_seq,UserCardinfoDto cdto);
	
	public boolean registAccount(int user_seq,AccountBalanceDto abdto);
	
	public int accountChk(int user_seq);
	
	public int oobChk(int user_seq);
	
//	public boolean insertDeposit(int user_seq,String fintech_use_num,String tran_amt); 
//	public boolean insertWithdraw(int user_seq,String fintech_use_num,String tran_amt); 
	
	public boolean withdraw_deposit(int wd_user_seq,
			String wd_fintech_use_num,
			String tran_amt,
			int dp_user_seq,
			String dp_fintech_use_num);
}
