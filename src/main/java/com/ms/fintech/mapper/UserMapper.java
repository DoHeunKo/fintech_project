package com.ms.fintech.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ms.fintech.dtos.CrawlerDto;
import com.ms.fintech.dtos.UserDto;
import com.ms.fintech.dtos.UserTokenDto;

@Mapper
public interface UserMapper {
	public String emailChk(String email);
	
	public boolean userRegist(UserDto dto);
	
	public boolean tokenRegist(UserTokenDto tdto);
	
	public UserDto userLogin(String email);
	
	public int withdraw(int user_seq);
	
	@Select("select title, src, href from mz_info")
	public List<CrawlerDto> getNewsList();
}
