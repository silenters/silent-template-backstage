package com.silent.template.backstage.bean.po;


/**
 * 用户与权限角色对应关系
 * @author TanJin
 * @date 2016-3-22
 */
public class UserRolePo {
	
	private String id;
	private String user_id;			//用户ID
	private String role_id;			//权限角色ID
	private long time;				//操作时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	} 

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "UserRoleBean [id=" + id + ", user_id=" + user_id + ", role_id="
				+ role_id + ", time=" + time + "]";
	}
}
