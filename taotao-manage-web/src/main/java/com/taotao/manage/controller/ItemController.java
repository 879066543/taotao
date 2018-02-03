package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemService;

/**
 * 商品的保存,商品的信息被分为两张表，其中一张表示基本信息，另外一张表示描述信息。 将描述信息分开到一张表是为了更有效率的存储。
 * 
 * @author zhangwenfeng
 *
 */
@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> saveItem(Item item, @RequestParam(value = "desc",required=false) String desc) {
		try {
			// 保存基本信息和描述信息
			Long ItemId = itemService.saveItem(item, desc);
			System.out.println("有返回值........"+item.getId());
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> updateItem(Item item, @RequestParam(value = "desc",required=false) String desc) {
		try {
			
			itemService.updateItem(item, desc);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}
	}
	
	
	
	
}
