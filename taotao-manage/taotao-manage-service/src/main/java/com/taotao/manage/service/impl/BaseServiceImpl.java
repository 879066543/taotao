package com.taotao.manage.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.taotao.manage.pojo.BasePojo;
import com.taotao.manage.service.BaseService;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 *	通用Service层的实现类 
 *
 * @param <T>
 */
public class BaseServiceImpl<T extends BasePojo> implements BaseService<T> {
	
	//在spring 4.x版本之后引入一个新特性:泛型依赖注入；子接口必须实现Mapper
	@Autowired
	private Mapper<T> mapper;
	
	private Class<T> clazz;
	public BaseServiceImpl() {
		//this表示当前正在被实例化的对象；如UserServiceImpl
		ParameterizedType pt = (ParameterizedType)this.getClass().getGenericSuperclass();
		clazz = (Class<T>)pt.getActualTypeArguments()[0];
		
	}
	

	@Override
	public T queryById(Serializable id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public List<T> queryAll() {
		
		return mapper.selectAll();
	}

	//通过条件查询
	@Override
	public List<T> queryListByWhere(T t) {
		return mapper.select(t);
	}

	@Override
	public Integer queryCountByWhere(T t) {
		return mapper.selectCount(t);
	}

	@Override
	public List<T> queryListByPage(Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		return mapper.selectAll();
	}

	//选择性更新和新增的意思是:没有值的字段或者没有修改的字段，在使用通用mapper不会出现sql语句。
	@Override
	public void saveSelective(T t) {
		//每一个表中都有一个创建时间和更新时间。如果没传入创建时间的话,就需要帮它创建
		if (t.getCreated()==null) {
			t.setCreated(new Date());
			t.setUpdated(t.getCreated());
		}
		if (t.getUpdated()==null) {
			t.setUpdated(new Date());
		}
		mapper.insertSelective(t);

	}

	@Override
	public void updateSelective(T t) {
		//因为在修改的时候。创建的时间是不能修改的，只有修改时间可以修改
		if (t.getUpdated()==null) {
			t.setUpdated(new Date());
		}
		mapper.updateByPrimaryKeySelective(t);

	}

	@Override
	public void deleteById(Serializable id) {
		mapper.deleteByPrimaryKey(id);

	}

	@Override
	public void deleteByIds(Serializable[] ids) {
		//设置条件
		Example example = new Example(this.clazz);
		Criteria criteria = example.createCriteria();
		//这里做的事情就是将一个数组转换成一个List然后再作为条件拼接在where后面
		criteria.andIn("id", Arrays.asList(ids));
		mapper.deleteByExample(example);

	}

}
