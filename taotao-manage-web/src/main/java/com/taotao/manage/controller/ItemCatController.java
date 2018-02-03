package com.taotao.manage.controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

	//需求：http://localhost:9091/rest/item/cat/query/1?rows=5
	@Autowired
	private ItemCatService itemCatService;
	
	/*@RequestMapping("/query/{page}")
	@ResponseBody
	public List<ItemCat> queryItemCatListByPage(@PathVariable("page") Integer page,Integer rows){
		List<ItemCat> list = itemCatService.queryItemCatListByPage(page, rows);
		
		return list;
	}*/
	
	@RequestMapping("/query/{page}")
	public ResponseEntity<List<ItemCat>> queryItemCatListByPage(@PathVariable("page") Integer page,@RequestParam(defaultValue="100")Integer rows) {
		try {
			//如果是正确，返回状态码200,ResponseEntity是会将对象转换成JSON格式返回到页面
			//List<ItemCat> list = itemCatService.queryItemCatListByPage(page, rows);
			List<ItemCat> list = itemCatService.queryListByPage(page, rows);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			//如果有异常,就返回500
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	//用来展示节点树
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ItemCat>> queryItemCatListByParentId(@RequestParam(value="id",defaultValue="0") Long parent_id){
		ItemCat itemCat = new ItemCat();
		itemCat.setParentId(parent_id);
		try {
			List<ItemCat> list = itemCatService.queryListByWhere(itemCat);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
}
