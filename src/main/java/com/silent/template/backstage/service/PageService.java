package com.silent.template.backstage.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.silent.framework.base.utils.StringUtils;
import com.silent.framework.base.utils.UuidUtils;
import com.silent.template.backstage.bean.po.PagePo;
import com.silent.template.backstage.bean.po.RolePagePo;
import com.silent.template.backstage.bean.po.UserRolePo;
import com.silent.template.backstage.bean.vo.PageNodeVo;
import com.silent.template.backstage.dao.PageDao;
import com.silent.template.backstage.dao.RolePageDao;
import com.silent.template.backstage.dao.UserRoleDao;

/**
 * 页面相关操作Service
 * @date 2016-3-24
 */
@Service
public class PageService {
	
	@Autowired
	private PageDao pageDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private RolePageDao rolePageDao;
	
	/**
	 * 更新数据
	 * @date 2017年6月14日
	 */
	public boolean updateOrInsert(PagePo pagePo) {
		pagePo.setTime(System.currentTimeMillis());
		if(StringUtils.isEmpty(pagePo.getId())) {
			pagePo.setId(UuidUtils.create());
			return pageDao.insert(pagePo) > 0;
		}
		return pageDao.update(pagePo) > 0;
	}
	
	/**
	 * 根据ID删除指定信息
	 * @date 2017年6月14日
	 */
	public boolean deleteById(String id) {
		return pageDao.deleteById(id) > 0;
	}
	
	/**
	 * 更新数据
	 * @date 2017年6月14日
	 */
	public boolean update(PagePo pagePo) {
		pagePo.setTime(System.currentTimeMillis());
		return pageDao.update(pagePo) > 0;
	}
	
	/**
	 * 根据ID获取指定信息
	 * @date 2017年6月14日
	 */
	public PagePo queryById(String id) {
		return pageDao.loadPageById(id);
	} 
	
	/**
	 * 获取所有页面信息
	 * @date 2017年6月14日
	 */
	public List<PagePo> queryAll() {
		return pageDao.loadAllPage();
	}
	
	/**
	 * 按照排序查询所有页面
	 * @return
	 * @throws SQLException
	 * @throws IOException 
	 */
	public List<PagePo> queryAllSortPage() {
		List<PagePo> pageList = queryAll();
		Map<String, PagePo> map = new HashMap<String, PagePo>();
		List<PagePo> list = new ArrayList<PagePo>();
		Iterator<PagePo> iterator = pageList.iterator();
		while(iterator.hasNext()) {
			PagePo pagePo = iterator.next();
			map.put(pagePo.getId(), pagePo);
			//最外层的父对象
			if(StringUtils.isEmpty(pagePo.getParent_id())) {
				list.add(pagePo);
				iterator.remove();
			}
		}
		
		iterator = pageList.iterator();
		while(iterator.hasNext()) {
			PagePo pagePo = iterator.next();
			PagePo parentPo = map.get(pagePo.getParent_id());
			if(parentPo != null) {
				parentPo.addChild(pagePo);
				iterator.remove();
			} else {
				list.add(pagePo);
			}
		}
		return list;
	}
	
	/**
	 * 查询该用户能访问的page id
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public Set<String> queryUserPageId(String userid) {
		List<UserRolePo> list = userRoleDao.loadListByUserId(userid);
		Set<String> userPageSet = new HashSet<String>();
		for(UserRolePo po : list) {
			List<RolePagePo> rolePageList = rolePageDao.loadListByRoleId(po.getRole_id());
			for(RolePagePo tempPo : rolePageList) {
				userPageSet.add(tempPo.getPage_id());
			}
		}
		return userPageSet;
	}
	
	/**
	 * 查询该角色能访问的page id
	 * @param roleid
	 * @return
	 * @throws SQLException
	 */
	public Map<String, RolePagePo> queryRolePageMap(String roleid) {
		Map<String, RolePagePo> userPageMap = new HashMap<String, RolePagePo>();
		List<RolePagePo> rolePageList = rolePageDao.loadListByRoleId(roleid);
		for(RolePagePo tempPo : rolePageList) {
			userPageMap.put(tempPo.getPage_id(), tempPo);
		}
		return userPageMap;
	}
	
	/**
	 * 查询该用户能访问的页面
	 * @param userid
	 * @return
	 * @throws SQLException
	 * @throws IOException 
	 */
	public List<PagePo> queryUserSortPage(String userid) {
		Set<String> userPageSet = queryUserPageId(userid);
		List<PagePo> pageList = queryAllSortPage();
		remove(pageList, userPageSet);
		return pageList;
	}
	
	/**
	 * 删除该用户无访问权限的Page信息，仅保留用户有权限访问的Page信息
	 * @date 2016-3-24
	 */
	private void remove(List<PagePo> list, Set<String> userPage) {
		Iterator<PagePo> iterator = list.iterator();
		while(iterator.hasNext()) {
			PagePo pagePo = iterator.next();
			if(pagePo.getChildList() != null && pagePo.getChildList().size() > 0) {
				remove(pagePo.getChildList(), userPage);
				
				if(pagePo.getChildList().size() == 0) {
					iterator.remove();
				}
			} else if(!userPage.contains(pagePo.getId())) {
				iterator.remove();
			}
		}
	}

	/**
	 * 构建信息树节点JSON
	 * @date 2016-3-24
	 */
	public PageNodeVo buildTreeNode(PagePo pagePo) {
		PageNodeVo pageNodeVo = new PageNodeVo();
		pageNodeVo.setId(pagePo.getId());
		pageNodeVo.setpId(StringUtils.isNotEmpty(pagePo.getParent_id()) ? pagePo.getParent_id() : "");
		pageNodeVo.setName(getNodeName(pagePo));
		return pageNodeVo;
	}
	
	/**
	 * 生成节点名称
	 * @date 2016-3-24
	 */
	public String getNodeName(PagePo pagePo) {
		return new StringBuilder().append(pagePo.getName()).append(" -- 排序:").append(pagePo.getSequence()).toString();
	}
}
