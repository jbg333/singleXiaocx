package com.weixin.note.serv.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 查询参数
 *
 * @author chenyi
 * @email 228112142@qq.com
 * @date 2017-03-14 23:15
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	// 当前页码
	private int page;
	// 每页条数
	private int limit;

	public Query(Map<String, Object> params) {
		this.putAll(params);

		String pageStr = String.valueOf(params.get("page"));
		// 分页参数
		if (params.containsKey("page") && StringUtils.isNotEmpty(pageStr) && !"null".equals(pageStr)) {
			this.page = Integer.parseInt(String.valueOf(params.get("page")));
		} else {
			this.page = 1;
		}

		String limitStr = String.valueOf(params.get("limit"));
		if (params.containsKey("limit") && StringUtils.isNotEmpty(limitStr) && !"null".equals(limitStr)) {
			this.limit = Integer.parseInt(String.valueOf(params.get("limit")));
		} else {
			this.limit = 20;
		}

		this.put("offset", (page - 1) * limit);
		this.put("page", page);
		this.put("limit", limit);
		// 防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
		if (params.containsKey("sidx")) {
			String sidx = (String)params.get("sidx");
			if (StringUtils.isNotEmpty(sidx)) {
				this.put("sidx", SQLFilter.sqlInject(sidx, true));
			}
		}

		if (params.containsKey("order")) {
			String order = (String)params.get("order");
			if (StringUtils.isNotEmpty(order)) {
				this.put("order", SQLFilter.sqlInject(order, false));
			}
		}
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
