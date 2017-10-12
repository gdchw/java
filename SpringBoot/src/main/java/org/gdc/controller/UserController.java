package org.gdc.controller;

import org.gdc.model.Test;
import org.gdc.model.User;
import org.gdc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
// 启动注解事务管理
//@EnableTransactionManagement
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/get/ubook")
	public Object getUBooks() {
		User user = userService.getUserAndBooksById(1);

		return user;
	}

	@RequestMapping("/get")
	public Object getUser() {
		User user = userService.getUserById(1);

		return user;
	}
	
	@RequestMapping("/get/ds1")
	public Object getUserDs1() {
		User user = userService.getUserByIdds1(1);

		return user;
	}
	
	@RequestMapping("/get/ds2")
	public Object getUserDs2() {
		Test test = userService.getTestById(1);

		return test;
	}

}
