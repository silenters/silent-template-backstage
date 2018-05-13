package com.silent.template.backstage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.silent.template.backstage.bean.po.UserPo;

/**
 * 用户信息处理DAO
 * @author TanJin
 * @date 2018年3月5日
 */
public interface UserDao {

	/**
	 * 根据用户名获取指定用户信息
	 * @param username
	 * @return
	 */
	@Select("SELECT * FROM auth_user WHERE username = #{username}")
	public UserPo loadUserByUsername(String username);
	
	/**
	 * 获取所有用户信息
	 */
	@Select("SELECT * FROM auth_user")
	public List<UserPo> loadAll();

	/**
	 * 根据用户ID获取指定用户信息
	 * @param userId
	 * @return
	 */
	@Select("SELECT * FROM auth_user WHERE id = #{id}")
	public UserPo loadUserById(String userId);

	/**
	 * 根据用户ID删除指定用户
	 * @param userId
	 * @return
	 */
	@Delete("DELETE FROM auth_user WHERE id = #{id}")
	public int deleteUser(String userId);
	
	/**
	 * 新增用户
	 * @param userPo
	 * @return
	 */
	@Insert("INSERT INTO auth_user(id, username, password, realname, company, job, salt, status, time) VALUES "
			+ "(#{id}, #{username}, #{password}, #{realname}, #{company}, #{job}, #{salt}, #{status}, #{time})")
	public int insertUser(UserPo userPo);
	
	/**
	 * 更新用户信息
	 * @param userPo
	 * @return
	 */
	@Update("UPDATE auth_user SET username=#{username}, password=#{password}, realname=#{realname}, company=#{company}, job=#{job}, salt=#{salt}, status=#{status}, time=#{time} WHERE id = #{id}")
	public int updateUser(UserPo userPo);
}
