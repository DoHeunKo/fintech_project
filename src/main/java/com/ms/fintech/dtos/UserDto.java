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
@Alias(value="UserDto")
public class UserDto {
	private int user_seq;
	private String name;
	private String email;
	private String password;
	private String phone;
	private String sex;
	private String is_married;
	private String is_join;
	private String address;
	private int age;
	private String job;
	private String user_seq_no;
	private String client_use_code;
	private String pattern_chk;
	private String card_password;
	

}
