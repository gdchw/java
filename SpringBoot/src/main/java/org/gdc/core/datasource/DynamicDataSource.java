package org.gdc.core.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 获取当前数据源(动态数据源)
 * 
 * @author Administrator
 * 
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {

		return DynamicDataSourceContextHolder.getDataSourceType();
	}

}
