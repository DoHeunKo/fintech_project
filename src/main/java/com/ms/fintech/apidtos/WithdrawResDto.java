package com.ms.fintech.apidtos;

import lombok.Data;

@Data
public class WithdrawResDto {
	private String api_tran_id;
	private String api_tran_dtm;
	private String rsp_code;
	private String rsp_message;
	private String dps_account_holder_name;
	private String bank_name;
	private String account_holder_name;
	private String tran_amt;
	private String wd_limit_remain_amt;
}
