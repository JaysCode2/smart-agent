package com.example.smartagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.smartagent.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * 用户信息Mapper接口
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM user_info WHERE username = #{username} AND is_deleted = 0 LIMIT 1")
    UserInfo selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT * FROM user_info WHERE email = #{email} AND is_deleted = 0 LIMIT 1")
    UserInfo selectByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户
     */
    @Select("SELECT * FROM user_info WHERE phone = #{phone} AND is_deleted = 0 LIMIT 1")
    UserInfo selectByPhone(@Param("phone") String phone);

    /**
     * 根据用户名或邮箱或手机号查询用户
     */
    @Select("SELECT * FROM user_info WHERE (username = #{identifier} OR email = #{identifier} OR phone = #{identifier}) AND is_deleted = 0 LIMIT 1")
    UserInfo selectByIdentifier(@Param("identifier") String identifier);
}
