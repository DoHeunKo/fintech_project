package com.ms.fintech.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardInfoDto {
	private int user_seq;
	private String fintech_use_num;
	private String balance_amt;
}
