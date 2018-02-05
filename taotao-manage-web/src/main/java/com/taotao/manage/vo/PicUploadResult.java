package com.taotao.manage.vo;

/**
 * 在使用Kindeditor时文件上传所需要返回的参数 
 *
 */
public class PicUploadResult {
	
	//判断是否上传成功,不是0就代表失败
	private Integer error;
	//用于在页面中直接可以访问的地址
	private String url;
	//长和宽是自己想要返回,不返回kindeditor也有统一默认的长宽
	private String height;
	private String width;
	
	
	
	
	
	public Integer getError() {
		return error;
	}
	public void setError(Integer error) {
		this.error = error;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	
	
	

}
