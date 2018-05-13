package com.silent.template.backstage.bean.po;

/**
 * 权限角色与页面对应关系
 * @author TanJin
 * @date 2016-3-22
 */
public class RolePagePo {

	private String id;
	private String role_id;			//权限角色ID
	private String page_id;			//页面ID
	private long time;				//操作时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getPage_id() {
		return page_id;
	}

	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "RolePageBean [id=" + id + ", role_id=" + role_id + ", page_id="
				+ page_id + ", time=" + time + "]";
	}
}
