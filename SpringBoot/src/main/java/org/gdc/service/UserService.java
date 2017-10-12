package org.gdc.service;

import org.gdc.model.Test;
import org.gdc.model.User;

public interface UserService {

	public User getUserById(int id);
	
	public User getUserByIdds1(int id);
	

	public User getUserAndBooksById(int id);
	
	
	public Test getTestById(int id);
	
}
