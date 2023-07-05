package com.ms.fintech.command;

import lombok.Data;

@Data
public class UserRankingCommand {
	private int user_seq;
	private String email;
	private String scope;
	private String user_seq_no;
	private String token;
}
