package com.ms.fintech.command;

import java.util.List;

import lombok.Data;

@Data
public class MonthCommand {
	private List<CategoryCommand> categoryList;
}
