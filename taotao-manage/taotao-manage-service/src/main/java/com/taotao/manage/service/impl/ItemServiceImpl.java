package com.taotao.manage.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.mapper.ItemCatMapper;
import com.taotao.manage.mapper.ItemDescMapper;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ItemServiceImpl extends BaseServiceImpl<Item> implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	// 保存基本信息和描述信息
	@Override
	public Long saveItem(Item item, String desc) {
		// 保存基本信息
		saveSelective(item);

		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(itemDesc.getCreated());
		itemDesc.setItemDesc(desc);
		// 这里需要设置创建时间和更新时间,是因为下面insertSelective已经不是通用BaseServiceImpl里面的方法了，所以需要自行设置
		itemDescMapper.insertSelective(itemDesc);

		// 为什么需要返回ID
		/**
		 * 因为是Duboo,它传输是使用url的，我们如果是返回对象，是通过地址的，在不适用Dubbo的时候是可以，但是现在使用了
		 * 如果在Controller需要使用这个对象,就不能通过地址传输
		 */
		return item.getId();

	}

	@Override
	public void updateItem(Item item, String desc) {
		// 保存基本信息
		updateSelective(item);
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(itemDesc.getCreated());
		itemDesc.setItemDesc(desc);
		// 这里需要设置创建时间和更新时间,是因为下面insertSelective已经不是通用BaseServiceImpl里面的方法了，所以需要自行设置
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);

	}

	// 根据商品标题模糊分页查询商品数据列表并按照更新时间降序排序
	@Override
	public DataGridResult queryItemList(String title, Integer page, Integer rows) {
		// 使用分页助手
		PageHelper pageHelper = new PageHelper();
		pageHelper.startPage(page, rows);
		// 创建Example
		Example example = new Example(Item.class);
		try {
		// 判断是否有条件
			if (StringUtils.isNotBlank(title)) {
				// 对标题条件进行解码
				title = URLEncoder.encode(title, "utf-8");
				// 创建条件查询
				Criteria criteria = example.createCriteria();
				// 对标题进行模糊查询
				criteria.andLike("title", "%"+title+"%");
				
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//对更新日期做降序排列
		example.orderBy("updated").desc();
		//执行条件查询
		List<Item> items = itemMapper.selectByExample(example);
		
		for (Item item : items) {
			//对类目进行设置
			item.setItemCat(itemCatMapper.selectByPrimaryKey(item.getCid()));
		}
		
		//使用分页助手的pageInfo对象来获取条件查询的总记录数和查询集合
		PageInfo<Item> pageInfo = new PageInfo<>(items);
		//进行返回数据的封装
		DataGridResult dataGridResult = new DataGridResult();
		dataGridResult.setTotal(pageInfo.getTotal());
		dataGridResult.setRows(pageInfo.getList());
		return dataGridResult;
	}

	@Override
	public void reshelf(Long[] ids) {
		for (Long id : ids) {
			//先根据id获取对象
			Item item = itemMapper.selectByPrimaryKey(id);
			item.setStatus(1);
			itemMapper.updateByPrimaryKeySelective(item);
		}
	}
	
	@Override
	public void instock(Long[] ids) {
		for (Long id : ids) {
			//先根据id获取对象
			Item item = itemMapper.selectByPrimaryKey(id);
			item.setStatus(2);
			itemMapper.updateByPrimaryKeySelective(item);
		}
	}

}
