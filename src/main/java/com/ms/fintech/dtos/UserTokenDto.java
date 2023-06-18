package com.ms.fintech.dtos;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Alias(value="UserTokenDto")
public class UserTokenDto {
	private int user_seq;
	private String scope;
	private String token;

}