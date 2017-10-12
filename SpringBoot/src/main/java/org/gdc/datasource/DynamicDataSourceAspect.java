package org.gdc.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 切换数据源Advice
 * 
 * @author Administrator
 * 
 */
@Aspect  //aop的注解
@Order(-1)  //保证该AOP在@Transactional 之前执行
@Component
public class DynamicDataSourceAspect {

	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
	
	
	
	/**
	 *  切换数据源
	 * 
	 * @param point
	 * @param ds
	 */
	@Before("@annotation(ds)")
	public void changeDataSource(JoinPoint point, TargetDataSource ds){
		String dataSourceId = ds.name();
		if (!DynamicDataSourceContextHolder.containsDataSourceType(dataSourceId)) {
			logger.error("数据源[{}]不存在, 使用默认数据源 > {}", dataSourceId, point.getSignature());
		}else {
			logger.debug("Use DataSource : {} > {}", dataSourceId, point.getSignature());
			DynamicDataSourceContextHolder.setDataSourceType(dataSourceId); //存储当前数据源
		}
	}
	
	/**
	 *  清除数据源
	 * 
	 * @param point
	 * @param ds
	 */
	@After("@annotation(ds)")
	public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
		logger.debug("clear DataSource : {} > {}", ds.name(), point.getSignature());
		DynamicDataSourceContextHolder.clearDataSourceType();
	}
}
