package com.silent.template.backstage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.silent.template.backstage.bean.po.PagePo;

/**
 * 页面相关操作DAO
 * @date 2016-3-24
 */
public interface PageDao {

	/**
	 * 更新页面信息
	 * @param  pagePo
	 * @return
	 */
	@Update("UPDATE auth_page SET name=#{name}, url=#{url}, description=#{description}, parent_id=#{parent_id}, "
			+ "level=#{level}, sequence=#{sequence}, visible=#{visible}, time=#{time} WHERE id=#{id}")
	public int update(PagePo pagePo);
	
	/**
	 * 新增页面信息
	 * @param pagePo
	 * @return
	 */
	@Insert("INSERT INTO auth_page(id, name, url, description, parent_id, level, sequence, visible, time) VALUES "
			+ "(#{id}, #{name}, #{url}, #{description}, #{parent_id}, #{level}, #{sequence}, #{visible}, #{time})")
	public int insert(PagePo pagePo);
	
	/**
	 * 根据ID删除指定页面信息
	 * @param id 页面ID
	 * @return
	 */
	@Delete("DELETE FROM auth_page WHERE id=#{id}")
	public int deleteById(String id);
	
	/**
	 * 根据ID获取指定页面数据
	 * @param id 页面ID
	 * @return
	 */
	@Select("SELECT * FROM auth_page WHERE id=#{id}")
	public PagePo loadPageById(String id);
	
	/**
	 * 获取所有页面信息，并根据序号降序排列
	 */
	@Select("SELECT * FROM auth_page ORDER BY sequence DESC")
	public List<PagePo> loadAllPage();
}
