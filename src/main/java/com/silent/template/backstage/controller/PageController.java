package com.silent.template.backstage.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContext;

import com.silent.framework.base.bean.ResponseBaseBean;
import com.silent.framework.base.bean.ResponseBean;
import com.silent.framework.base.utils.StringUtils;
import com.silent.template.backstage.bean.po.PagePo;
import com.silent.template.backstage.bean.vo.PageNodeVo;
import com.silent.template.backstage.bean.vo.PageVo;
import com.silent.template.backstage.bean.vo.View;
import com.silent.template.backstage.constants.AuthConstants;
import com.silent.template.backstage.constants.AuthI18NCode;
import com.silent.template.backstage.service.PageService;

/**
 * 系统页面
 * @author TanJin
 * @date 2016-3-23
 */
@Controller
@RequestMapping("/admin/authority/page")
public class PageController {
	private final static String INDEX = "admin/page/index.html";
	
	@Autowired
	private PageService pageService;
	
	@RequestMapping("/index")
	public View index(HttpServletRequest request, HttpServletResponse response) {
		List<PagePo> pageList = pageService.queryAll();
		request.setAttribute(AuthConstants.LIST, pageList);
		return new View(INDEX);
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	public ResponseBean<PagePo> edit(HttpServletRequest request) {
		String id = request.getParameter("id");
		ResponseBean<PagePo> responseBean = new ResponseBean<PagePo>();
		if(StringUtils.isEmpty(id)) {
			responseBean.buildError();
			return responseBean;
		}
		
		PagePo bean = pageService.queryById(id);
		if(null == bean) {
			responseBean.buildError();
			return responseBean;
		}
		responseBean.setData(bean);
		responseBean.buildSuccess();
		return responseBean;
	}
	
	/**
	 * 保存页面节点信息
	 */
	@RequestMapping(value={"/add/save","/edit/save"})
	@ResponseBody
	private ResponseBean<PageVo> save(@Validated PagePo pagePo, HttpServletRequest request) throws Exception {
		RequestContext context = new RequestContext(request);
		pageService.updateOrInsert(pagePo);
		
		PageVo pageVo = new PageVo();
		pageVo.setBean(pagePo);
		pageVo.setNode(pageService.buildTreeNode(pagePo));
		
		ResponseBean<PageVo> responseBean = new ResponseBean<PageVo>();
		responseBean.buildSuccess();
		responseBean.setMsg(context.getMessage(AuthI18NCode.COMMON_SAVE_SUCCESS));
		responseBean.setData(pageVo);
		return responseBean;
	}
	
	/**
	 * 删除指定节点信息
	 * @date 2016-3-24
	 */
	@RequestMapping("/del")
	@ResponseBody
	protected ResponseBaseBean delete(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		ResponseBaseBean responseBean = new ResponseBaseBean();
		RequestContext context = new RequestContext(request);
		PagePo pagePo = pageService.queryById(id);
		if(null == pagePo) {
			responseBean.buildError(context.getMessage(AuthI18NCode.ERROR_DATA_NOT_EXIST));
			return responseBean;
		}
		
		if(pageService.deleteById(id)) {
			responseBean.buildSuccess(context.getMessage(AuthI18NCode.COMMON_DELETE_SUCCESS));
		} else {
			responseBean.buildError(context.getMessage(AuthI18NCode.COMMON_DELETE_ERROR));
		}
		return responseBean;
	}
	
	/**
	 * 修改排序
	 * @date 2016-3-24
	 */
	@RequestMapping("/index/changeSequence")
	@ResponseBody
	private ResponseBean<String> changeSequence(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		String pId = request.getParameter("pId");
		String moveType = request.getParameter("moveType");
		String targetNodeId = request.getParameter("targetNodeId");
		boolean parentChange = StringUtils.parseBoolean(request.getParameter("parentChange"), false);
	
		PagePo pagePo = pageService.queryById(id);
		PagePo target = pageService.queryById(targetNodeId);
		
		if(StringUtils.isNotEmpty(id) && null != pagePo){
			if(parentChange){
				pagePo.setParent_id(pId);
			}
			if(!"inner".equals(moveType)){
				int step = "prev".equals(moveType) ? 1 : -1;
				if(StringUtils.isNotEmpty(targetNodeId) && null != target){
					pagePo.setSequence(target.getSequence() + step);
				}
			}
			pagePo.setTime(System.currentTimeMillis());
			pageService.update(pagePo);
		}
		
		ResponseBean<String> responseBean = new ResponseBean<String>();
		responseBean.buildSuccess();
		responseBean.setData(pageService.getNodeName(pagePo));
		return responseBean;
	}

	/**
	 * 获取节点树JSON信息
	 * @date 2016-3-23
	 */
	@RequestMapping("/index/tree")
	@ResponseBody
	private ResponseBean<List<PageNodeVo>> getTreeNodes(HttpServletRequest request) throws Exception {
		List<PagePo> pageList = pageService.queryAll();
		Iterator<PagePo> iterator = pageList.iterator();
		
		List<PageNodeVo> treeNodes = new ArrayList<PageNodeVo>();
		while(iterator.hasNext()) {
			PagePo pagePo = iterator.next();
			PageNodeVo node = pageService.buildTreeNode(pagePo);
			if(StringUtils.isEmpty(pagePo.getParent_id())) {
				node.setOpen(true);
			}
			treeNodes.add(node);
			iterator.remove();
		}
		
		ResponseBean<List<PageNodeVo>> responseBean = new ResponseBean<List<PageNodeVo>>();
		responseBean.buildSuccess();
		responseBean.setData(treeNodes);
		return responseBean;
	}

}
