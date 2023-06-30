package com.ms.fintech.apidtos;

import lombok.Data;

@Data
public class DepositReqListDto {
	private String tran_no;
	private String bank_tran_id;
	private String fintech_use_num;
	private String print_content;
	private String tran_amt;
	private String req_client_fintech_use_num;
	private String req_client_name;
	private String req_client_num;
	private String transfer_purpose;
}
