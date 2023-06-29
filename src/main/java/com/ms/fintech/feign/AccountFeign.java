package com.ms.fintech.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.ms.fintech.apidtos.AccountBalanceDto;
import com.ms.fintech.apidtos.AccountTransactionListDto;
import com.ms.fintech.apidtos.UserCardinfoDto;
import com.ms.fintech.apidtos.UserMeDto;
import com.ms.fintech.apidtos.UserOobDto;
import com.ms.fintech.apidtos.WithdrawDto;

@FeignClient(name="feign", url="https://testapi.openbanking.or.kr")
public interface AccountFeign {
	
	@GetMapping(path = "/v2.0/user/me")
	public UserMeDto requestUserMe(
	          @RequestHeader("Authorization") String access_token,
			  @RequestParam("user_seq_no") String user_seq_no);
		
	
	@GetMapping(path = "/v2.0/account/transaction_list/fin_num")
	public AccountTransactionListDto requestAccountTransactionList(
			@RequestHeader("Authorization") String access_token,  
			@RequestParam("bank_tran_id") String bank_tran_id, 
			  @RequestParam("fintech_use_num") String fintech_use_num, 
			  @RequestParam("inquiry_type") String inquiry_type, 
			  @RequestParam("inquiry_base") String inquiry_base, 
			  @RequestParam("from_date") String from_date, 
			  @RequestParam("to_date") String to_date, 
			  @RequestParam("sort_order") String sort_order, 
			  @RequestParam("tran_dtime") String tran_dtime);
	
	@GetMapping(path="/v2.0/account/balance/fin_num")
	public AccountBalanceDto requestAccountBalanceList(
			@RequestHeader("Authorization") String access_token,
			@RequestParam("bank_tran_id") String bank_tran_id, 
			  @RequestParam("fintech_use_num") String fintech_use_num,
			  @RequestParam("tran_dtime") String tran_dtime
			  );
	
	@PostMapping(path="/oauth/2.0/token")
	public UserOobDto requestOobToken(
			@RequestParam("client_id") String client_id,
			@RequestParam("client_secret")String client_secret,
			@RequestParam("scope") String scope,
			@RequestParam("grant_type") String grant_type
			);
	@PostMapping(path="/oauth/2.0/token")
	public UserCardinfoDto requestCardToken(
			@RequestParam("code") String code,
			@RequestParam("client_id") String client_id,
			@RequestParam("client_secret")String client_secret,
			@RequestParam("redirect_uri") String redirect_uri,
			@RequestParam("grant_type") String grant_type
			);
	@PostMapping(path="/v2.0/transfer/withdraw/fin_num")
	public WithdrawDto requestWithdraw(
			@RequestHeader("Authorization") String access_token,
			@RequestParam("bank_tran_id") String bank_tran_id, 
			@RequestParam("cntr_account_type") String cntr_account_type, 
			@RequestParam("cntr_account_num") String cntr_account_num,
			@RequestParam("dps_print_content") String dps_print_content,
			@RequestParam("fintech_use_num") String fintech_use_num,
			@RequestParam("tran_amt") String tran_amt,
			@RequestParam("tran_dtime")String tran_dtime,
			@RequestParam("req_client_name")String req_client_name,
			@RequestParam("req_client_num")String req_client_num,
			@RequestParam("transfer_purpose")String transfer_purpose,
			@RequestParam("req_client_fintech_use_num")String req_client_fintech_use_num,
			@RequestParam("recv_client_name")String recv_client_name,
			@RequestParam("recv_client_bank_code")String recv_client_bank_code,
			@RequestParam("recv_client_account_num")String recv_client_account_num
			);
}
