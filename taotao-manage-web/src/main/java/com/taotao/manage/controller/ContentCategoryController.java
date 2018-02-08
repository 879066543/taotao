package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;

	/**
	 * 根据父id查询所有子节点
	 * @param parentId
	 * @return
	 */
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ContentCategory>> queryContentCategoryListByParentId(
			@RequestParam(value="id",defaultValue="0")Long parentId){
		ContentCategory contentCategory = new ContentCategory();
		contentCategory.setParentId(parentId);
		try {
			List<ContentCategory> contentCategories = contentCategoryService.queryListByWhere(contentCategory);
			return ResponseEntity.ok(contentCategories);
		} catch (Exception e) {
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**新增
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory){
		
		try {
			ContentCategory result = contentCategoryService.saveContentCategory(contentCategory);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 更新
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public ResponseEntity<Void> updateContentCategory(ContentCategory contentCategory){
		
		try {
			contentCategoryService.updateSelective(contentCategory);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ResponseEntity<Void> deleteContentCategory(ContentCategory contentCategory){
		try {
			//删除内容分类以及其子类
			contentCategoryService.deleteContentCategory(contentCategory);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
}
