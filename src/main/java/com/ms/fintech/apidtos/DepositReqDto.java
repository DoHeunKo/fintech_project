package com.ms.fintech.apidtos;

import java.util.List;

import lombok.Data;

@Data
public class DepositReqDto {
	private String cntr_account_type;
	private String cntr_account_num;
	private String wd_pass_phrase;
    private String wd_print_content;
	private String name_check_option;
	private String tran_dtime;
	private String req_cnt;
	private List<DepositReqListDto> req_list;
}
