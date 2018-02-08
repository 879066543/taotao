package com.taotao.manage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.mapper.ContentMapper;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;
import com.taotao.manage.service.redis.RedisService;

import tk.mybatis.mapper.entity.Example;

@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {
	// 大广告显示条数
	@Value("${BIG_AD_NUM}")
	private Integer BIG_AD_NUM;
	// 大广告的id
	@Value("${BIG_AD_ID}")
	private Long BIG_AD_ID;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private ContentMapper contentMapper;
	
	@Autowired
	private RedisService redisService;
	
	//大广告在redis中的key
	private static final String REDIS_BIG_AD_KEY ="PORTAL_INDEX_BIG_AD_DATA";
	
	private static final int REDIS_BIG_AD_TIME = 60*60*24;

	/**
	 * 根据内容分类id分页查询内容列表并按照更新时间降序排序
	 */
	@Override
	public DataGridResult queryBycategoryId(Integer page, Integer rows, Long categoryId) {
		PageHelper pageHelper = new PageHelper();
		pageHelper.startPage(page, rows);
		Example example = new Example(Content.class);
		example.createCriteria().andEqualTo("categoryId", categoryId);
		// 根据时间降序
		example.orderBy("updated").desc();
		List<Content> list = contentMapper.selectByExample(example);
		PageInfo<Content> pageInfo = new PageInfo<>(list);

		// 返回表格对象
		return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	/**
	 * 大广告查询
	 * 先从redis数据库中查,如果存在对应的,就返回,如果没有,就去数据库中查找。在把数据存放在redis中
	 */
	@Override
	public String queryPortalBigAdData() throws Exception {
		String result = "";
		try {
			result = redisService.get(REDIS_BIG_AD_KEY);
			if (StringUtils.isNotBlank(result)) {
				return result;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 大广告的id
		// 显示大广告的条数
		DataGridResult dataGridResult = queryBycategoryId(1, BIG_AD_NUM, BIG_AD_ID);
		
		List<Content> rows = (List<Content>) dataGridResult.getRows();
		// 存放大广告
		List<Map<String, Object>> BigAds = new ArrayList<>();
		if (rows != null && rows.size() > 0) {
			Map<String, Object> map;
			for (Content c : rows) {
				map = new HashMap<>();
				map.put("alt", c.getTitle());
				map.put("height", 240);
				map.put("heightB", 240);
				map.put("href", c.getUrl());
				map.put("src", c.getPic());
				map.put("srcB", c.getPic());
				map.put("width", 670);
				map.put("widthB", 550);
				BigAds.add(map);
			}
		}
		//从数据库中取出大广告设计并设置进redis，且设置时间为1天
		String queryFormsql = objectMapper.writeValueAsString(BigAds);
		try {
			redisService.setex(REDIS_BIG_AD_KEY, REDIS_BIG_AD_TIME, queryFormsql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryFormsql;
	}

}
