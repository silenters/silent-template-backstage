package com.silent.template.backstage.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.silent.template.backstage.bean.po.RolePagePo;
import com.silent.template.backstage.dao.RolePageDao;

/**
 * 权限与页面对应关系处理Service
 * @author TanJin
 * @date 2017-6-13
 */
@Service
public class RolePageService {

	@Autowired
	private RolePageDao rolePageDao;
	
	/**
	 * 插入数据 
	 * @date 2017年6月14日
	 */
	public boolean insert(RolePagePo rolePagePo) {
		return rolePageDao.insert(rolePagePo) > 0;
	}
	
	/**
	 * 删除指定权限与页面对应关系
	 * @date 2017年6月14日
	 */
	public boolean deleteById(String rolePageId) {
		return rolePageDao.deleteById(rolePageId) > 0;
	}
	
	/**
	 * 根据权限角色ID及页面ID获取指定数据
	 * @param roleId
	 * @param pageId
	 * @return
	 * @throws IOException
	 * @date 2017年6月13日
	 */
	public RolePagePo queryByRoleIdPageId(String roleId, String pageId) throws IOException {
		return rolePageDao.loadByRoleIdAndPageId(roleId, pageId);
	}
}
