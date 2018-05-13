package com.silent.template.backstage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.silent.template.backstage.bean.po.RolePo;

/**
 * 权限角色处理DAO
 * @author TanJin
 * @date 2016-3-24
 */
public interface RoleDao {

	/**
	 * 获取所有权限角色信息
	 * @param 
	 * @return
	 */
	@Select("SELECT * FROM auth_role")
	public List<RolePo> loadAllRole();
	
	/**
	 * 根据权限角色ID获取指定信息
	 * @param id
	 * @return
	 */
	@Select("SELECT * FROM auth_role WHERE id=#{id}")
	public RolePo loadById(String id);
	
	/**
	 * 删除指定权限角色
	 * @param id
	 * @return
	 */
	@Delete("DELETE FROM auth_role WHERE id=#{id}")
	public int deleteRole(String id);
	
	/**
	 * 更新指定权限角色信息
	 * @param rolePo
	 * @return
	 */
	@Update("UPDATE auth_role SET name=#{name}, description=#{description}, time=#{time} WHERE id=#{id}")
	public int update(RolePo rolePo);
	
	/**
	 * 新增权限角色
	 * @param rolePo
	 * @return
	 */
	@Insert("INSERT INTO auth_role(id, name, description, time) VALUES (#{id}, #{name}, #{description}, #{time})")
	public int insert(RolePo rolePo);
}
