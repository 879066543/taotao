package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.vo.DataGridResult;
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
	
	@RequestMapping(value="/update",method = RequestMethod.POST)
	public ResponseEntity<Void> updateItem(Item item, @RequestParam(value = "desc",required=false) String desc) {
		try {
			
			itemService.updateItem(item, desc);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}
	}
	
	//数据列表的展示，接受参数：page当前页、rows页面大小、title商品标题条件搜索
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<DataGridResult> queryItemList(
							@RequestParam(value="title",required=false)String title,
							@RequestParam(value="page",defaultValue="1")Integer page,
							@RequestParam(value="rows",defaultValue="30")Integer rows){
		
		try {
			//进行一个条件分页排序查询
			DataGridResult dataGridResult = itemService.queryItemList(title, page, rows);
			return ResponseEntity.ok(dataGridResult);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	//批量删除Item
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ResponseEntity<Void> deleteItemById(@RequestParam("ids")Long[] ids){
		try {
			//批量删除
			itemService.deleteByIds(ids);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	//上架
	@RequestMapping(value="/reshelf",method=RequestMethod.POST)
	public ResponseEntity<Void> reshelfItemById(@RequestParam("ids")Long[] ids){
		try {
			//批量删除
			itemService.reshelf(ids);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	//下架
	@RequestMapping(value="/instock",method=RequestMethod.POST)
	public ResponseEntity<Void> instockItemById(@RequestParam("ids")Long[] ids){
		try {
			//批量删除
			itemService.instock(ids);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	
	
}
