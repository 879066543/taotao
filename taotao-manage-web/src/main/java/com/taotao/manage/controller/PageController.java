package com.taotao.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.remoting.exchange.Request;

/**
 * 用来首页的对其他页面的访问
 * @author zhangwenfeng
 *
 */

@Controller
@RequestMapping("/page")
public class PageController {
	
	@RequestMapping(value="/{url}",method=RequestMethod.GET)
	public String toIndex(@PathVariable String url) {
		return url;
	}

}
