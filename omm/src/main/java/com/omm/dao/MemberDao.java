package com.omm.dao;

import com.omm.dto.MemberDto;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;
import java.sql.Timestamp;


@Mapper
public interface MemberDao {
    @Select("select count(*) from tbl_member where user_id=#{user_id}")
    public int checkId(@Param("user_id") String userid) throws DataAccessException;
    @Select("select count(*) from tbl_member where user_nickname=#{user_nickname}")
    public int checkNickname(@Param("user_nickname") String user_nickname) throws DataAccessException;
    @Select("select count(*)  from tbl_member where user_email=#{user_email}")
    public int checkEmail(@Param("user_email") String user_email) throws DataAccessException;
    @Select("select count(*)  from tbl_member where user_tel=#{user_tel}")
    public int checkTel(@Param("user_tel") String user_tel) throws DataAccessException;
    @Insert("insert into tbl_member values (#{user_id}, #{user_pw}, #{user_name}, #{user_nickname}, #{user_email}," +
            "#{user_addr_zip}, #{user_addr}, #{user_addr_detail}, #{user_tel}, #{user_gender}, #{user_birth}, 0)")
    public boolean insert_member(MemberDto dto) throws DataAccessException;
    @Select("select * from tbl_member where user_id=#{user_id}")
    public MemberDto get_by_user_id(@Param("user_id") String user_id) throws DataAccessException;

    @Insert("insert into email_check values(#{user_email},#{code},null)")
    void emailCodeSave(String user_email, String code);

    @Select("select user_email from email_check where code=#{code}")
    String emailCodeCheck(String code);

    @Select("select count(*) from email_check where user_email=#{user_email}")
    int delete_check(String user_email);

    @Delete("delete from email_check where user_email=#{user_email}")
    void delete_code(String user_email);

    @Select("select * from tbl_member where user_name=#{user_name} and user_email=#{user_email}")
    public MemberDto getByUserNameAndEmail(@Param("user_name") String user_name, @Param("user_email") String user_email) throws DataAccessException;

    @Select("select count(*) from tbl_member where user_id=#{user_id} and user_email=#{user_email}")
    public int checkIdAndEmailCount(@Param("user_id") String user_id, @Param("user_email") String user_email) throws DataAccessException;
    @Update("update tbl_member set user_pw = #{encryptedPassword} where user_id = #{user_id}")
    void updatePassword(@Param("user_id") String user_id, @Param("encryptedPassword") String encryptedPassword);

    @Update("update email_check set temp_password = #{encryptedPassword}, expiry_time = #{expiryTime} where user_email = #{user_email}")
    void saveTemporaryPassword(@Param("user_email") String user_email,
                               @Param("encryptedPassword") String encryptedPassword,
                               @Param("expiryTime") Timestamp expiryTime);

}
