package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemDescService;

@Controller
@RequestMapping("/item/desc")
public class ItemDescController {

	@Autowired
	private ItemDescService itemDescService;
	
	@RequestMapping(value="/{ItemId}",method=RequestMethod.GET)
	public ResponseEntity<ItemDesc> queryItemDescById(@PathVariable("ItemId")Long ItemId){
	
		try {
			ItemDesc itemDesc = itemDescService.queryById(ItemId);
			return ResponseEntity.ok(itemDesc);
		} catch (Exception e) {
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
}
