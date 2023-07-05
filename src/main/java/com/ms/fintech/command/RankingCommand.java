package com.ms.fintech.command;

import lombok.Data;

@Data
public class RankingCommand {
	private String email;
	private double total_cs_percent;
	private double last_cs_percent;
	private double this_month_percent;
	private int this_month_balance;
	private double min_cur;
}
