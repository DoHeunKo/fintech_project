package com.ms.fintech.dtos;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Alias(value="WithdrawDepositDto")
public class WithdrawDepositDto {
	private int user_seq;
	
	private String fintech_use_num;
	private String inout_type;
	private String tran_amt;
	
	private Date tran_date;
}