package com.taotao.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.taotao.manage.mapper.ItemCatMapper;
import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;


@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private ItemCatMapper itemCatMapper;

	@Override
	public List<ItemCat> queryItemCatListByPage(Integer page, Integer rows) {
		//使用分页助手PageHelper设置分页
		PageHelper.startPage(page, rows);
		//两种办法都可以.select传一个Null进去代表没有条件
//		return itemCatMapper.select(null);
		return itemCatMapper.selectAll();
	}

}
