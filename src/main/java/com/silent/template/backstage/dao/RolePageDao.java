package com.silent.template.backstage.dao;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.silent.template.backstage.bean.po.RolePagePo;

/**
 * 权限角色与页面对应关系处理DAO
 * @author TanJin
 * @date 2016-3-24
 */
public interface RolePageDao {

	/**
	 * 根据权限ID获取相关信息列表
	 * @param roleId
	 * @return
	 */
	@Select("SELECT * FROM auth_role_page WHERE role_id = #{roleId}")
	public List<RolePagePo> loadListByRoleId(String roleId);
	
	/**
	 * 根据权限角色ID及页面ID获取指定数据
	 * @param roleId
	 * @param pageId
	 * @return
	 * @throws IOException
	 * @date 2017年6月13日
	 */
	@Select("SELECT * FROM auth_role_page WHERE role_id = #{roleId} AND page_id = #{pageId}")
	public RolePagePo loadByRoleIdAndPageId(@Param("roleId")String roleId, @Param("pageId")String pageId);
	
	/**
	 * 新增权限角色与页面对应关系记录
	 * @param rolePagePo
	 * @return
	 */
	@Insert("INSERT INTO auth_role_page(id, role_id, page_id, time) VALUES (#{id}, #{role_id}, #{page_id}, #{time})")
	public int insert(RolePagePo rolePagePo);
	
	/**
	 * 根据ID删除指定数据
	 * @param id
	 * @return
	 */
	@Delete("DELETE FROM auth_role_page WHERE id = #{id}")
	public int deleteById(String id);
}
