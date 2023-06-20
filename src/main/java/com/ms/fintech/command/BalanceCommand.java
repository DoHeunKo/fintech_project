package com.ms.fintech.command;

import lombok.Data;

@Data
public class BalanceCommand {
	private String bank_name;
	private String fintech_use_num;
	private String balance_amt;
}
