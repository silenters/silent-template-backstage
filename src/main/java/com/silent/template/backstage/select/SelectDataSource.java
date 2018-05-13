package com.silent.template.backstage.select;

import java.util.Map;

/**
 * Select的数据源
 */
public interface SelectDataSource  {

	/**
	 * 获取Select的选项数据
	 * @return
	 */
	public Map<String, String> options();
}
