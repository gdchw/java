package org.gdc.core.datasource;

import java.util.ArrayList;
import java.util.List;

public class DynamicDataSourceContextHolder {

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	public static final List<String> dataSourceIds = new ArrayList<>();

	public static void setDataSourceType(String dataSourceId) {
		contextHolder.set(dataSourceId);
	}

	public static String getDataSourceType() {
		return contextHolder.get();
	}

	public static void clearDataSourceType() {
		contextHolder.remove();
	}

	/**
	 * 判断指定数据源(DataSource)当前是否存在
	 * 
	 * @param dataSourceId
	 * @return
	 */
	public static boolean containsDataSourceType(String dataSourceId) {
		
		return dataSourceIds.contains(dataSourceId);
	}

}
