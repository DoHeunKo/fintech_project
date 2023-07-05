package com.ms.fintech.apidtos;


import java.util.List;

import lombok.Data;

@Data
public class CardDetailDto {
	private String card_value;
	private String paid_date;
	private String paid_time;
	private String paid_amt;
	private String merchant_name_masked;
	private String credit_fee_amt;
	private String product_type;

}
