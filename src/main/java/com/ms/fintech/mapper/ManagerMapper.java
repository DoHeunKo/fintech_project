package com.ms.fintech.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ms.fintech.dtos.CrawlerDto;

@Mapper
public interface ManagerMapper {
	
	@Select("select * from mz_info limit #{page}, 6")
	public List<CrawlerDto> getPagedList(int page);
	

	@Delete("delete from mz_info where seq = #{seq}")
	public int deleteNews(int seq);

	@Delete("delete from mz_info where seq = #{seq}")
	public int deleteRoom(int seq);
}
