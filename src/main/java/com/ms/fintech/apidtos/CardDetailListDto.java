package com.ms.fintech.apidtos;


import java.util.List;

import lombok.Data;

@Data
public class CardDetailListDto {
	private String api_tran_id;
	private String api_tran_dtm;
	private String rsp_code;
	private String rsp_message;
	private String bank_tran_id;
	private String bank_tran_date;
	private String bank_code_tran;
	private String bank_rsp_code;
	private String bank_rsp_message;
	private String user_seq_no;
	private String next_page_yn;
	private String befor_inquiry_trace_info;
	private String bill_detail_cnt;
	private List<CardDetailDto> bill_detail_list;
	
	
}
