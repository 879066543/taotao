package com.taotao.manage.service.impl;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.mapper.ItemDescMapper;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemService;


@Service
public class ItemServiceImpl extends BaseServiceImpl<Item> implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	//保存基本信息和描述信息
	@Override
	public Long saveItem(Item item, String desc) {
		//保存基本信息
		saveSelective(item);
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(itemDesc.getCreated());
		itemDesc.setItemDesc(desc);
		//这里需要设置创建时间和更新时间,是因为下面insertSelective已经不是通用BaseServiceImpl里面的方法了，所以需要自行设置
		itemDescMapper.insertSelective(itemDesc);
		
		//为什么需要返回ID
		/**
		 * 因为是Duboo,它传输是使用url的，我们如果是返回对象，是通过地址的，在不适用Dubbo的时候是可以，但是现在使用了
		 * 如果在Controller需要使用这个对象,就不能通过地址传输
		 */
		return item.getId();
		
	}

	@Override
	public void updateItem(Item item, String desc) {
		//保存基本信息
		saveSelective(item);
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(itemDesc.getCreated());
		itemDesc.setItemDesc(desc);
		//这里需要设置创建时间和更新时间,是因为下面insertSelective已经不是通用BaseServiceImpl里面的方法了，所以需要自行设置
		itemDescMapper.insertSelective(itemDesc);
		

	}


}
