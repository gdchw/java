package org.gdc.service.impl;

import org.gdc.core.log.SystemServiceLog;
import org.gdc.core.datasource.TargetDataSource;
import org.gdc.mapper.UserMapper;
import org.gdc.model.Test;
import org.gdc.model.User;
import org.gdc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper mapper;
	
	@Override
	@SystemServiceLog(description = "查询数据")
	public User getUserById(int id) {
		
		return mapper.getUserById(id);
	}

	@Override
	public User getUserAndBooksById(int id) {
		
		return mapper.getUserAndBooksById(id);
	}

	@TargetDataSource(name = "ds1")
	@Override
	public User getUserByIdds1(int id) {
		
		return mapper.getUserById(id);
	}


	@TargetDataSource(name = "ds2")
	@Override
	public Test getTestById(int id) {
		
		return mapper.getTestById(id);
	}


}
