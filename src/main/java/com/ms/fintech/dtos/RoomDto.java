package com.ms.fintech.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoomDto{
	private int roomNo;
	private String roomTitle;
}
