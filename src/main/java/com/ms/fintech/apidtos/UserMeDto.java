package com.ms.fintech.apidtos;

import java.util.List;

import lombok.Data;

@Data
public class UserMeDto {
	private String api_tran_id;
	private String api_tran_dtm;
	private String rsp_code;
	private String rsp_message;
	private String user_seq_no;
	private String user_ci;
	private String user_name;
	private String user_info;
	private String user_gender;
	private String user_cell_no;
	private String user_email;
	private String res_cnt;
	private List<UserMeAccountDto> res_list;
}
