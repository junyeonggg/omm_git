package com.omm.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.omm.dto.InquireDto;
import com.omm.dto.PagingSearch;

@Mapper
public interface InquireDao {
	@Select("select count(*) from tbl_inquire")
	public int getTotCnt();
	
	@Select("select inquire_id,user_nickname as user_id, inquire_title, inquire_content, inquire_create_date from tbl_inquire join tbl_member on tbl_member.user_id=tbl_inquire.user_id where inquire_title like concat('%',#{keyword},'%') order by inquire_id desc limit #{startRecord},#{recordSize};")
	List<InquireDto> selectAll(PagingSearch paging);

	@Select("select inquire_id, user_nickname ,tbl_member.user_id,inquire_title,inquire_content, inquire_create_date from tbl_inquire join tbl_member on tbl_member.user_id = tbl_inquire.user_id where inquire_id=#{inquire_id}")
	HashMap<String, Object> selectInquireById(int inquire_id);

	@Insert("insert into tbl_inquire values(null,#{user_id},#{inquire_title},#{inquire_content},#{inquire_create_date})")
	void insertInquire(InquireDto inquireDto);

	@Update("update tbl_inquire set inquire_title=#{inquire_title}, inquire_content=#{inquire_content}, inquire_create_date = #{inquire_create_date} where inquire_id=#{inquire_id}")
	public void updateForm(InquireDto inquireDto);

	@Delete("delete from tbl_inquire where inquire_id = #{inquire_id}")
	public void deleteInquire(int inquire_id);

}
