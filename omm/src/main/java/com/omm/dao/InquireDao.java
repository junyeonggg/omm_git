package com.omm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.omm.dto.InquireDto;

@Mapper
public interface InquireDao {

	@Select("select * from tbl_inquire")
	List<InquireDto> selectAll();

	@Select("select * from tbl_inquire where inquire_id=#{id}")
	InquireDto selectInquireById(int id);

}
