package com.silent.template.backstage.bean.po;


import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

/**
 * 页面信息
 * @author TanJin
 * @date 2016-3-22
 */
public class PagePo {
	
	private String id;
	@NotBlank
	private String name;				//页面名称
	private String url;					//页面URL
	private String description;			//页面描述
	private String parent_id;			//父页面ID
	private int level;					//页面等级
	private int sequence;				//排序
	private int visible;				//是否可见
	private long time;					//操作时间
	
	private List<PagePo> childList;				//子页面集合
	private String parentPageUrl;				//父页面URL
	
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public List<PagePo> getChildList() {
		return childList;
	}

	public void setChildList(List<PagePo> childList) {
		this.childList = childList;
	}

	public String getParentPageUrl() {
		return parentPageUrl;
	}

	public void setParentPageUrl(String parentPageUrl) {
		this.parentPageUrl = parentPageUrl;
	}

	/**
	 * 添加子页面信息
	 * @date 2016-3-23
	 */
	public void addChild(PagePo pageBean) {
		if(this.childList == null) {
			this.childList = new ArrayList<PagePo>();
		}
		this.childList.add(pageBean);
	}

	@Override
	public String toString() {
		return "PageBean [id=" + id + ", name=" + name + ", url=" + url
				+ ", description=" + description + ", parent_id=" + parent_id
				+ ", level=" + level + ", sequence=" + sequence + ", visible="
				+ visible + ", time=" + time + ", childList=" + childList
				+ ", parentPageUrl=" + parentPageUrl + "]";
	}
	
}
