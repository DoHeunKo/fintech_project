package com.ms.fintech.command;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MonthCommand {
	private List<CategoryCommand> categoryCommand=new ArrayList<>();
	
//	public MonthCommand() {
//        this.categoryCommand = new ArrayList<>(13);
//    }
}
