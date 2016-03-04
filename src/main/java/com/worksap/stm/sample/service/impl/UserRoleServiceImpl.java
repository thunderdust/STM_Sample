package com.worksap.stm.sample.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worksap.stm.sample.dao.spec.UserRoleDao;
import com.worksap.stm.sample.dto.RoleDto;
import com.worksap.stm.sample.exception.ServiceException;
import com.worksap.stm.sample.service.spec.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService {
	
	@Autowired
	UserRoleDao userRoleDao;

	@Override
	public List<String> getBy(String userId) throws ServiceException {
		List<RoleDto> roleList;
		try {
			roleList = userRoleDao.getBy(userId);
		} catch (IOException e) {
			throw new ServiceException("Cannot fetch user for Id: " + userId, e);
		}
		
		List<String> roles = new ArrayList<String>();
		for(RoleDto role : roleList) {
			roles.add(role.getName());
		}
		return roles;
	}

	@Override
	public void insert(String userId, List<String> roleList) throws ServiceException {
		List<RoleDto> roles = new ArrayList<RoleDto>();
		for(String roleStr : roleList) {
			RoleDto role = new RoleDto();
			role.setName(roleStr);
			roles.add(role);
		}
		
		try {
			userRoleDao.insert(userId, roles);
		} catch (IOException e) {
			throw new ServiceException("Cannot add roles for userId: " + userId, e);
		}
	}

	@Override
	public void deleteBy(String userId) throws ServiceException {
		try {
			userRoleDao.deleteBy(userId);
		} catch (IOException e) {
			throw new ServiceException("Cannot delete role for userId: " + userId, e);
		}
	}
	
}
