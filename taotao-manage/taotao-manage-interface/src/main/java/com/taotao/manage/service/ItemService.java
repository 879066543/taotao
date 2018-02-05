package com.taotao.manage.service;


import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.pojo.Item;

public interface ItemService extends BaseService<Item> {

	//保存基本信息和描述信息
	Long saveItem(Item item, String desc);

	//更新
	void updateItem(Item item, String desc);

	
	DataGridResult queryItemList(String title, Integer page, Integer rows);

	void reshelf(Long[] ids);

	//下架
	void instock(Long[] ids);
	

}
