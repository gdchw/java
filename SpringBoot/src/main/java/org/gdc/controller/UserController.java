package org.gdc.controller;

import org.gdc.model.User;
import org.gdc.service.UserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
//启动注解事务管理
@EnableTransactionManagement
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/get")
	public Object getUser(){
		User user = userService.getUserById(1);
		
		return user;
	}
}
