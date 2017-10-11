package org.gdc.service.impl;

import org.gdc.mapper.UserMapper;
import org.gdc.model.User;
import org.gdc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper mapper;
	
	@Override
	public User getUserById(int id) {
		
		return mapper.getUserById(id);
	}

}
