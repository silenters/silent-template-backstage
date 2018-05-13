package com.silent.template.backstage.dao;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.silent.template.backstage.bean.po.UserRolePo;

/**
 * 用户与权限对应关系操作DAO
 * @author TanJin
 * @date 2016-3-22
 */
public interface UserRoleDao {

	/**
	 * 插入数据
	 * @throws IOException 
	 * @date 2017年6月11日
	 */
	@Insert("INSERT INTO auth_user_role(id, user_id, role_id, time) VALUES (#{id}, #{user_id}, #{role_id}, #{time})")
	public int insert(UserRolePo userRolePo);
	
	/**
	 * 根据ID删除指定数据
	 * @throws IOException 
	 * @date 2017年6月11日
	 */
	@Delete("DELETE FROM auth_user_role WHERE id = #{id}")
	public int delete(String id);
	
	/**
	 * 根据用户ID获取指定用户的权限对应关系列表
	 * @param userId
	 * @return
	 * @throws IOException 
	 */
	@Select("SELECT * FROM auth_user_role WHERE user_id = #{userId}")
	public List<UserRolePo> loadListByUserId(String userId);
	
	/**
	 * 根据用户ID及权限ID获取指定数据
	 * @date 2017年6月11日
	 */
	@Select("SELECT * FROM auth_user_role WHERE user_id = #{userid} and role_id = #{roleid}")
	public UserRolePo loadByUserIdRoldId(@Param("userid")String userid, @Param("roleid")String roleid);
}
