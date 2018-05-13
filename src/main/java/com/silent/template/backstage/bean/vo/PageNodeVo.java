package com.silent.template.backstage.bean.vo;

/**
 * 系统页面Node节点
 * @author TanJin
 * @date 2018年3月21日
 */
public class PageNodeVo {

	private String id;			// 节点页面ID
	private String pId;			// 节点父页面ID
	private String name;		// 节点页面名称
	private boolean open=false;		// 是否开启，默认不开启
	private boolean checked=false;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	@Override
	public String toString() {
		return "PageNodeVo [id=" + id + ", pId=" + pId + ", name=" + name + ", open=" + open + ", checked=" + checked
				+ "]";
	}
	
}
