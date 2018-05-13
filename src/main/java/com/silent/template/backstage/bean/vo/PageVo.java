package com.silent.template.backstage.bean.vo;

import com.silent.template.backstage.bean.po.PagePo;

/**
 * 页面信息保存之后返回给前端的信息
 * @author TanJin
 * @date 2018年3月21日
 */
public class PageVo {

	private PagePo bean;
	private PageNodeVo node;
	
	public PagePo getBean() {
		return bean;
	}
	public void setBean(PagePo bean) {
		this.bean = bean;
	}
	public PageNodeVo getNode() {
		return node;
	}
	public void setNode(PageNodeVo node) {
		this.node = node;
	}
	
	@Override
	public String toString() {
		return "PageVo [bean=" + bean + ", node=" + node + "]";
	}
}
