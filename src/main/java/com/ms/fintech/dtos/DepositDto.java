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
@Alias(value="DepositDto")
public class DepositDto {
	private int user_seq;
	private int wd_seq;
	private String wd_fintech_use_num;
	private String wd_tran_amt;
	private String cur_balance_amt;
	private String plus_balance_amt;
	private String user_fintech_use_num;
	private Date tran_date;
}