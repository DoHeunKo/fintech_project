package com.ms.fintech.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ms.fintech.command.UserRankingCommand;
import com.ms.fintech.dtos.AccountDto;
import com.ms.fintech.dtos.CardInfoDto;
import com.ms.fintech.dtos.CrawlerDto;
import com.ms.fintech.dtos.RoomDto;
import com.ms.fintech.dtos.UserDto;
import com.ms.fintech.dtos.UserTokenDto;
import com.ms.fintech.dtos.WithdrawDepositDto;

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
	
	public boolean registAccount(AccountDto adto);
	
	public int accountChk(int user_seq);
	
	public int oobChk(int user_seq);
	
	public String cardPWChk(int user_seq);
	
	@Select("SELECT COUNT(*) FROM card_info WHERE user_seq=#{user_seq}")
	public int linkChk(int user_seq);
	
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

	@Insert("insert into card_info values(#{user_seq},#{fintech_use_num},#{balance_amt},#{bank_code_std},#{member_bank_code},#{charge_month},#{settlement_seq_no})")
	public int insertCardInfo(CardInfoDto dto);
	
	@Select("select * from card_info where user_seq=#{user_seq}")
	public CardInfoDto getCardInfo(int user_seq);
	
	public UserDto dp_info(String name);
	
	public UserTokenDto dp_token(int user_seq);
	
	@Insert("insert into withdraw_deposit values(#{user_seq},#{fintech_use_num},'in',#{tran_amt},NOW())")
	public boolean insertDeposit(int user_seq,String fintech_use_num,String tran_amt);
	
	@Insert("insert into withdraw_deposit values(#{user_seq},#{fintech_use_num},'out',#{tran_amt},NOW())")
	public boolean insertWithdraw(int user_seq,String fintech_use_num,String tran_amt);
	
	@Select("select room_title from community_info where room_no =#{title}")
	public String getRoomTitle(int title);
	
	@Select("select card_password from user_info where user_seq=#{user_seq}")
	public String isPWcorrect(int user_seq);
	
	public List<UserDto> rankingUserInfo();
}

