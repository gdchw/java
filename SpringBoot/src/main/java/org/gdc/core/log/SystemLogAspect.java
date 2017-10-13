package org.gdc.core.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 切点类, AOP实现日志日志
 * 
 * @author Administrator
 *
 */
@Aspect
@Component
public class SystemLogAspect {

	// 本地日志记录对象
	private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

	@After("@annotation(log)")
	public void systemServiceLog(JoinPoint point, SystemServiceLog log) {
		String description = log.description();
		String targetName = point.getTarget().getClass().getName();
		String methodName = point.getSignature().getName();
		
		logger.info(description+"=====操作类:"+targetName+"=====方法:"+methodName);
	}
}
