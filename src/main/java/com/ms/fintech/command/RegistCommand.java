package com.ms.fintech.command;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistCommand {
	
	@NotBlank(message="이메일: 필수정보입니다")
	private String email;
	@NotBlank(message="비밀번호 입력 : 8~15자리")
	@Length(min = 1, max=15)
	private String password;
	@NotBlank(message="이름 : 필수정보입니다")
	private String name;
	@NotBlank(message="휴대폰 번호 : 필수정보입니다")
	private String phone;
	@NotBlank(message="주소 : 필수정보입니다")
	private String address;
	@NotBlank(message="성별 : 필수정보입니다")
	private String sex;
	@NotBlank(message="결혼여부 : 필수정보입니다")
	private String is_married;
	@NotNull(message="연령대 : 필수정보입니다")
	private Integer age;
	@NotBlank(message="직업 : 필수정보입니다")
	private String job;
	private String user_seq_no;
	private String token;
	private String scope;
}
