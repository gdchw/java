package org.gdc.controller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
// 扫描全局文件
@SpringBootApplication(scanBasePackages="org.gdc")
//指定扫描的mapper接口所在的包
@MapperScan("org.gdc.mapper")
public class IndexController {

	
	@RequestMapping("/")
	@ResponseBody
	public Object home() {
		return "hello java!";
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(IndexController.class, args);
	}
}
