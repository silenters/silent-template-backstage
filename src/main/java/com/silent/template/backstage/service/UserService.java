package com.silent.template.backstage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.silent.framework.base.utils.StringUtils;
import com.silent.framework.base.utils.UuidUtils;
import com.silent.template.backstage.bean.po.UserPo;
import com.silent.template.backstage.dao.UserDao;

/**
 * 登录用户操作Service
 * @author TanJin
 * @date 2017-8-21
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	/**
	 * 根据username获取指定用户信息
	 * @date 2017年8月21日
	 */
	public UserPo queryByUsername(String username) {
		return userDao.loadUserByUsername(username);
	}
	
	/**
	 * 获取所有的用户列表
	 * @return
	 */
	public List<UserPo> queryAllUserList() {
		return userDao.loadAll();
	}
	
	/**
	 * 根据用户ID获取指定用户信息
	 * @param id 用户ID
	 * @return
	 */
	public UserPo queryById(String id) {
		return userDao.loadUserById(id);
	}
	
	/**
	 * 删除指定用户
	 * @param id 用户ID
	 * @return
	 */
	public boolean deleteUser(String id) {
		return userDao.deleteUser(id) > 0;
	}

	/**
	 * 更新用户信息
	 * @param bean
	 * @return
	 */
	public boolean updateOrInsert(UserPo userPo) {
		userPo.setTime(System.currentTimeMillis());
		if(StringUtils.isEmpty(userPo.getId())) {
			// 设置ID
			userPo.setId(UuidUtils.create());
			return userDao.insertUser(userPo) > 0;
		}
		return userDao.updateUser(userPo) > 0;
	}
	
	/**
	 * 更新用户信息
	 * @param 
	 * @return
	 */
	public boolean updateUser(UserPo userPo) {
		return userDao.updateUser(userPo) > 0;
	}
	
}
