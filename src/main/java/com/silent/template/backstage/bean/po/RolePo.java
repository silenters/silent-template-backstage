package com.silent.template.backstage.bean.po;


import javax.validation.constraints.NotBlank;

/**
 * 权限角色信息
 * @author TanJin
 * @date 2016-3-22
 */
public class RolePo {

	private String id;
	@NotBlank
	private String name;				//权限角色名称
	@NotBlank
	private String description;			//权限角色描述
	private long time;					//操作时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "RoleBean [id=" + id + ", name=" + name + ", description=" + description + ", time=" + time + "]";
	}
	
}
