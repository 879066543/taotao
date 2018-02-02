package com.taotao.manage.service;

import java.util.List;

import com.taotao.manage.pojo.ItemCat;

public interface ItemCatService {
	
	List<ItemCat> queryItemCatListByPage(Integer page, Integer rows);

}
