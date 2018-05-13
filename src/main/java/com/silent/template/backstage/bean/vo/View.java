package com.silent.template.backstage.bean.vo;

/**
 * 返回的view对象
 * @author TanJin
 * @date 2016-3-22
 */
public class View {
	private String page;		//跳转页
	private String desc;		//描述
	private String title;		//跳转页标题
	private boolean isFullPage = false;		//是否为全页面
	
	public View() {}
	
	public View(String page) {
		this.page = page;
	}
	
	public View(String page, String desc) {
		this.page = page;
		this.desc = desc;
	}
	
	public View(String page, boolean isFullPage) {
		this.page = page;
		this.isFullPage = isFullPage;
	}
	
	public View(String page, String desc, boolean isFullPage) {
		this.page = page;
		this.desc = desc;
		this.isFullPage = isFullPage;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public boolean isFullPage() {
		return isFullPage;
	}

	public void setFullPage(boolean isFullPage) {
		this.isFullPage = isFullPage;
	}

	@Override
	public String toString() {
		return "View [page=" + page + ", desc=" + desc + ", title=" + title + ", isFullPage=" + isFullPage + "]";
	}
}