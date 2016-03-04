package com.worksap.stm.sample.service.spec;

import java.util.List;

import com.worksap.stm.sample.exception.ServiceException;

public interface UserRoleService {
	List<String> getBy(String userId) throws ServiceException;
	void insert(String userId, List<String> roleList) throws ServiceException;
	void deleteBy(String userId) throws ServiceException;
}
