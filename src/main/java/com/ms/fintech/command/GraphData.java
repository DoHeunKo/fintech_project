package com.ms.fintech.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GraphData {
	
	private List<String> category_labels;
    private List<Double> category_data;
}
