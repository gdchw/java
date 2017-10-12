package org.gdc.mapper;

import org.apache.ibatis.annotations.Param;
import org.gdc.model.Test;
import org.gdc.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

//	@Select("select * from User where id = #{id}")
	public User getUserById(@Param("id")int id);
	
	public User getUserAndBooksById(int id);
	
	public Test getTestById(int id);

}
