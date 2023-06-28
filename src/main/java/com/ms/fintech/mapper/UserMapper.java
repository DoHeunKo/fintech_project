package com.ms.fintech.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ms.fintech.dtos.CrawlerDto;
import com.ms.fintech.dtos.RoomDto;
import com.ms.fintech.dtos.UserDto;
import com.ms.fintech.dtos.UserTokenDto;

@Mapper
public interface UserMapper {
	public String emailChk(String email);
	
	public String patternChk(int user_seq);
	
	public String joinChk(int user_seq);

	public boolean userRegist(UserDto dto);
	
	public boolean tokenRegist(UserTokenDto tdto);
	
	public UserDto userLogin(String email);
	
	public int withdraw(int user_seq);
	
	public boolean pattern(int user_seq);

	public boolean join(int user_seq);
	
	@Select("select * from mz_info")
	public List<CrawlerDto> getNewsList();
	
	@Select("select * from community_info")
	public List<RoomDto> getRoomList();

	@Select("select * from user_info")
	public List<UserDto> getUserList();
	
	@Select("select * from user_info where user_seq = #{user_seq}")
	public UserDto getUser(int userSeq);

	@Update("update user_info set card_password = #{pw} where user_seq = #{userSeq}")
	public int setPassword(int userSeq, String pw);
}
