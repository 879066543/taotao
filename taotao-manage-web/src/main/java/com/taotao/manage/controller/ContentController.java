package com.taotao.manage.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;

@Controller
@RequestMapping("/content")
public class ContentController {
	
	@Autowired
	private ContentService contentService;

	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<DataGridResult> queryContentBycategoryId(
			@RequestParam(value="page",defaultValue="1") Integer page,
			@RequestParam(value="rows",defaultValue="20")Integer rows,
			@RequestParam(value="categoryId",defaultValue="0")Long categoryId){
		try {
			DataGridResult dataGridResult = contentService.queryBycategoryId(page,rows,categoryId);
			return ResponseEntity.ok(dataGridResult);
		} catch (Exception e) {
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 新增内容
	 * @param content
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> saveContent(Content content){
		
		try {
			contentService.saveSelective(content);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	/**
	 * 编辑内容
	 * @param content
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public ResponseEntity<Void> editContent(Content content){
		try {
			contentService.updateSelective(content);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	/**
	 * 删除内容
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ResponseEntity<Map<String, Integer>> deleteContent(@RequestParam(value="ids") Long[] ids){
		Map<String,Integer> map = new HashMap<>();
		try {
			contentService.deleteByIds(ids);
			map.put("status", 200);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
