package com.taotao.common.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 用于easy-ui的数据回显
 * 需要返回的格式是:
 * {"total":总记录数,"rows":[记录列表]}
 */
public class DataGridResult implements Serializable {

	
	private Long total;
	
	private List<?> rows;

	public DataGridResult() {
		super();
	}

	public DataGridResult(Long total, List<?> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
	
}
