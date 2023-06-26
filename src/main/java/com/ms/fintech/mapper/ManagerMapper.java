package com.ms.fintech.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ms.fintech.dtos.CrawlerDto;
import com.ms.fintech.dtos.UserDto;

@Mapper
public interface ManagerMapper {
	
	@Select("select * from mz_info limit #{page}, 6")
	public List<CrawlerDto> getPagedList(int page);
}
