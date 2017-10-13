package org.gdc.core.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;


/**
 * 动态数据源注册<br>
 * 启动动态数据源(在启动类中如:Application.class 添加@Import(DynamicDataSourceRegister.class))
 * 
 * 
 * @author Administrator
 *
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

	private ConversionService conversionService = new DefaultConversionService();
	private PropertyValues dataSourcePropertyValues;

	// 如果配置文件中未指定数据源类型,使用该默认值
	private static final Object DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";

	// 默认数据源
	private DataSource defaultDataSource;
	
	// 多数据源
	private Map<String, DataSource> customDtaSources = new HashMap<>();


	/**
	 * 加载多数据源
	 */
	@Override
	public void setEnvironment(Environment arg0) {

		// 初始化主数据源
		initdefaultDataSource(arg0);
		
		// 初始化更多数据源
		initCustomDataSource(arg0);
		
	}
	
	@Override
	public void registerBeanDefinitions(AnnotationMetadata arg0, BeanDefinitionRegistry arg1) {

		Map<Object, Object> targetDataSource = new HashMap<Object, Object>();
		// 将主数据源添加到更多数据源中
		targetDataSource.put("dataSource", defaultDataSource);
		DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
		// 添加更多数据源
		targetDataSource.putAll(customDtaSources);
		for (String key : customDtaSources.keySet()) {
			DynamicDataSourceContextHolder.dataSourceIds.add(key);
		}
		
		// 创建DynamicDataSource
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(DynamicDataSource.class);
		
		beanDefinition.setSynthetic(true);
		MutablePropertyValues mutablePropertyValues = beanDefinition.getPropertyValues();
		mutablePropertyValues.addPropertyValue("defaultTargetDataSource", defaultDataSource);
		mutablePropertyValues.addPropertyValue("targetDataSources", targetDataSource);
		
		arg1.registerBeanDefinition("dataSource", beanDefinition);
		
		logger.info("Dynamic DataSource Registry");
	}

	
	
	/** 
	 *  初始化主数据源
	 * 
	 * @param env
	 */
	private void initdefaultDataSource(Environment env) {
		
		// 读取主数据源
		RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
		Map<String, Object> dsMap = new HashMap<>();
		dsMap.put("type", propertyResolver.getProperty("type"));
		dsMap.put("driver-class-name", propertyResolver.getProperty("driver-class-name"));
		dsMap.put("url", propertyResolver.getProperty("url"));
		dsMap.put("username", propertyResolver.getProperty("username"));
		dsMap.put("password", propertyResolver.getProperty("password"));
		
		// 创建DataSource
		defaultDataSource = buildDataSource(dsMap);
		
		// 绑定DataSource
		dataBinder(defaultDataSource, env);
		
	}

	/**
	 * 初始化更多数据源
	 * 
	 * @param env
	 */
	private void initCustomDataSource(Environment env) {
		
		// 读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "custom.datasource.");
        String dsPrefixs = propertyResolver.getProperty("names");
        for (String dsPrefix : dsPrefixs.split(",")) {// 多个数据源
            Map<String, Object> dsMap = propertyResolver.getSubProperties(dsPrefix + ".");
            // 创建DataSource
            DataSource dataSource = buildDataSource(dsMap);
            customDtaSources.put(dsPrefix, dataSource);
            dataBinder(dataSource, env);
        }
	}
	
	
	/**
	 * 创建DataSource
	 * 
	 * @param dsMap
	 * 
	 * @param type
	 * @param driverClassName
	 * @param url
	 * @param username
	 * @param password
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DataSource buildDataSource(Map<String, Object> dsMap) {

		try {
			Object type = dsMap.get("type");
			if (type == null) {
				type = DATASOURCE_TYPE_DEFAULT; // 默认数据源
			}
			Class<? extends DataSource> datasSourceType;
			datasSourceType = (Class<? extends DataSource>) Class.forName((String) type);

			String driverClassName = dsMap.get("driver-class-name").toString();
			String url = dsMap.get("url").toString();
			String username = dsMap.get("username").toString();
			String password = dsMap.get("password").toString();

			DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
					.username(username).password(password).type(datasSourceType);

			return factory.build();

		} catch (ClassNotFoundException  e) {
			
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 *  为DataSource绑定更多数据源
	 * 
	 * @param dataSource
	 * @param env
	 */
	private void dataBinder(DataSource dataSource, Environment env){
		
		 RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
		 dataBinder.setConversionService(conversionService);
		 dataBinder.setIgnoreNestedProperties(false);//false
		 dataBinder.setIgnoreInvalidFields(false);//false
	     dataBinder.setIgnoreUnknownFields(true);//true
	     
	     if(dataSourcePropertyValues == null){
	            Map<String, Object> rpr = new RelaxedPropertyResolver(env, "spring.datasource").getSubProperties(".");
	            Map<String, Object> values = new HashMap<>(rpr);
	            
	            // 排除已经设置的属性
	            values.remove("type");
	            values.remove("driver-class-name");
	            values.remove("url");
	            values.remove("username");
	            values.remove("password");
	            
	            dataSourcePropertyValues = new MutablePropertyValues(values);
	        }
	     
	     dataBinder.bind(dataSourcePropertyValues);
	}
	
	
	
}
