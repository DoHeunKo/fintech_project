package com.ms.fintech.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginCommand {
	@NotBlank(message="이메일을 입력해주세요")
	private String email;
	@NotBlank(message="비밀번호를 입력해주세요")
	private String password;
}
