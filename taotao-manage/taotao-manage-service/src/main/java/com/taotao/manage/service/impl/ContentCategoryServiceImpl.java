package com.taotao.manage.service.impl;

import java.awt.image.PixelGrabber;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.mapper.ContentCategoryMapper;
import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@Service
public class ContentCategoryServiceImpl extends BaseServiceImpl<ContentCategory> implements ContentCategoryService {

	@Autowired
	private ContentCategoryMapper contentCategoryMapper;

	@Override
	public ContentCategory saveContentCategory(ContentCategory contentCategory) {
		contentCategory.setIsParent(false);
		contentCategory.setStatus(1);
		contentCategory.setSortOrder(1);
		saveSelective(contentCategory);

		// 选择使用从新创建一个新的对象,但是把id赋值进去,然后再选择性新增,这样做的好处就是可以让数据库少查询一次（如果是使用id先查询父节点的话）
		// 还需要将父节点的isParent设置为1
		ContentCategory parentCC = new ContentCategory();
		parentCC.setId(contentCategory.getParentId());
		parentCC.setIsParent(true);
		updateSelective(parentCC);

		return contentCategory;
	}

	/**
	 * 删除内容分类及其子分类
	 */
	@Override
	public void deleteContentCategory(ContentCategory contentCategory) {
		List<Long> ids = new ArrayList<>();
		// 还需要将本节点加入数组
		ids.add(contentCategory.getId());
		// 获取子孙节点的id
		getContentCategory(ids, contentCategory.getId());
		//批量删除
		deleteByIds(ids.toArray(new Long[] {}));
		
		//还需要将变为已经不是父节点的节点的父节点改为0
		ContentCategory category = new ContentCategory();
		category.setParentId(contentCategory.getParentId());
		Integer num = queryCountByWhere(category);
		if(num == 0) {
			//说明已经没有子节点了
			ContentCategory Pcategory = new ContentCategory();
			Pcategory.setId(contentCategory.getId());
			Pcategory.setIsParent(false);
			updateSelective(Pcategory);
		}
		
	}

	// 获取子孙节点的id
	private void getContentCategory(List<Long> ids, Long id) {
		ContentCategory contentCategory = new ContentCategory();
		contentCategory.setParentId(id);
		List<ContentCategory> list = queryListByWhere(contentCategory);
		if (list != null && list.size() > 0) {
			for (ContentCategory cc : list) {
				ids.add(cc.getId());
				getContentCategory(ids, cc.getId());

			}
		}

	}

}
