package com.taotao.manage.service;


import com.taotao.manage.pojo.Item;

public interface ItemService extends BaseService<Item> {

	//保存基本信息和描述信息
	Long saveItem(Item item, String desc);

	//更新
	void updateItem(Item item, String desc);
	

}
