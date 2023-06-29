package com.ms.fintech.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardInfoDto {
	private int user_seq;
	private String fintech_use_num;
	private String balance_amt;
	private String bank_code_std;
	private String member_bank_code;
	private String charge_month;
	private String settlement_seq_no;
}
