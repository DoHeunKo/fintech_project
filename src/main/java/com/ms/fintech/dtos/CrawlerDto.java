package com.ms.fintech.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrawlerDto {
	private String title;
	private String src;
	private String href;
}
