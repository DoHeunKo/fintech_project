package com.ms.fintech.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ms.fintech.dtos.CrawlerDto;
import com.ms.fintech.dtos.RoomDto;

@Mapper
public interface ManagerMapper {

	@Select("select * from mz_info limit #{page}, 6")
	public List<CrawlerDto> getPagedList(int page);
	
	@Select("select * from community_info limit #{page}, 4")
	public List<RoomDto> getPagedRoom(int page);
	
	@Delete("delete from community_info where room_no = #{seq}")
	public int deleteRoom(int seq);

	@Delete("delete from mz_info where seq = #{seq}")
	public int deleteNews(int seq);

	@Insert("insert into community_info(room_title) values(#{title})")
	public void createRoom(String title);
	
	
}
