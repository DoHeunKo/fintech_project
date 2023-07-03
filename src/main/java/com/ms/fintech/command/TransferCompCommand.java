package com.ms.fintech.command;

import lombok.Data;

@Data
public class TransferCompCommand {
	private String dp_name;
	private String dp_fintech_use_num;
	private String tran_amt;
}
