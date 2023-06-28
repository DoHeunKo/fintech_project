package com.ms.fintech.dtos;

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
@Alias(value="AccountDto")
public class AccountDto {
	private int user_seq;
	private String fintech_use_num;
	private String bank_name;
	private String balance_amt;

}