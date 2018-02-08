package com.taotao.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.manage.service.ContentService;

@Controller
public class IndexController {
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/index")
	public ModelAndView toIndex() {
		ModelAndView modelAndView = new ModelAndView("index");
		try {
			//大广告的加载
			String data = contentService.queryPortalBigAdData();
			modelAndView.addObject("bigAdData", data);
		} catch (Exception e) {
		}
		return modelAndView;
	}
}
