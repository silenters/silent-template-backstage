package com.silent.template.backstage.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.silent.framework.base.utils.StringUtils;
import com.silent.framework.base.utils.UuidUtils;
import com.silent.template.backstage.bean.po.RolePo;
import com.silent.template.backstage.bean.po.UserRolePo;
import com.silent.template.backstage.dao.RoleDao;
import com.silent.template.backstage.dao.UserRoleDao;

/**
 * 权限相关操作Service
 * @author TanJin
 * @date 2016-3-31
 */
@Service
public class RoleService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserRoleDao userRoleDao;
	
	public RolePo queryRoleById(String id) {
		return roleDao.loadById(id);
	}
	
	/**
	 * 更新权限数据 
	 * @date 2017年6月14日
	 */
	public boolean updateOrInsert(RolePo rolePo) {
		rolePo.setTime(System.currentTimeMillis());
		if(StringUtils.isEmpty(rolePo.getId())) {
			rolePo.setId(UuidUtils.create());
			return roleDao.insert(rolePo) > 0;
		}
		return roleDao.update(rolePo) > 0;
	}
	
	/**
	 * 获取所有权限角色信息
	 * @date 2016-8-4
	 */
	public List<RolePo> queryAll() {
		return roleDao.loadAllRole();
	}
	
	/**
	 * 删除指定角色
	 * @param 
	 * @return
	 */
	public boolean deleteRole(String id) {
		return roleDao.deleteRole(id) > 0;
	}
		
	/**
	 * 根据登录用户账号ID，获取对应的权限角色Map 
	 * @param loginUserId	登陆账号用ID
	 * @throws IOException 
	 * @date 2016-3-29
	 */
	public Map<String, UserRolePo> queryLoginUserRoleMap(String loginUserId) {
		List<UserRolePo> list = userRoleDao.loadListByUserId(loginUserId);
		Map<String, UserRolePo> map = new HashMap<String, UserRolePo>();
		for(UserRolePo po : list) {
			map.put(po.getRole_id(), po);
		}
		return map;
	}
	
	/**
	 * 保存登陆用户与权限角色对应关系
	 * @param userid 登陆用户ID
	 * @param roles 权限角色ID字符串（以逗号分隔）
	 * @date 2016-3-31
	 */
	public boolean saveUserRoleId(String userid, String roles) {
		List<String> roleIdList = stringToIntegerList(roles);
		if(StringUtils.isEmpty(userid)) {
			return false;
		}
		
		boolean flg = true;
		try {
			Map<String, UserRolePo> map = queryLoginUserRoleMap(userid); 
			for(String roleid : roleIdList) {
				if (StringUtils.isEmpty(roleid)) {
					continue;
				}
				
				UserRolePo userRolePo = userRoleDao.loadByUserIdRoldId(userid, roleid);
				if(null == userRolePo) {
					UserRolePo bean = new UserRolePo();
					bean.setId(UuidUtils.create());
					bean.setUser_id(userid);
					bean.setRole_id(roleid);
					bean.setTime(System.currentTimeMillis());
					userRoleDao.insert(bean);
				}
				map.remove(roleid);
			}
			//删除剩余权限角色
			for(UserRolePo po : map.values()) {
				userRoleDao.delete(po.getId());
			}
		} catch (Exception e) {
			logger.error("", e);
			flg = false;
		} finally {
		}
		return flg;
	}
	
	/**
	 * 将字符串转换成INT型列表
	 * @date 2016-3-31
	 */
	private List<String> stringToIntegerList(String string) {
		if(StringUtils.isEmpty(string)){
			return new ArrayList<String>();
		}
		String[] itemArray = string.split(",");
		return Arrays.asList(itemArray);
	}
}
