package com.taotao.manage.service;

import java.util.List;

import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.pojo.ItemCat;

public interface ContentService extends BaseService<Content> {


	/**
	 * 根据内容分类id分页查询内容列表并按照更新时间降序排序
	 * @param categoryId
	 * @return
	 */
	DataGridResult queryBycategoryId(Integer page, Integer rows, Long categoryId);

	/**大广告数据加载
	 * 
	 */
	String queryPortalBigAdData()  throws Exception;
	
	

}
