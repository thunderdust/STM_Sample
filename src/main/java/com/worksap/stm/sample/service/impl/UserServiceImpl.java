package com.worksap.stm.sample.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.worksap.stm.sample.dao.spec.UserDao;
import com.worksap.stm.sample.dto.UserAccountDto;
import com.worksap.stm.sample.entity.UserAccountCreationEntity;
import com.worksap.stm.sample.entity.UserAccountEntity;
import com.worksap.stm.sample.entity.UserAccountFetchEntity;
import com.worksap.stm.sample.entity.UserAccountListEntity;
import com.worksap.stm.sample.exception.ServiceException;
import com.worksap.stm.sample.service.spec.OfficeService;
import com.worksap.stm.sample.service.spec.UserRoleService;
import com.worksap.stm.sample.service.spec.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private OfficeService officeService;

	public UserAccountEntity getBy(String id) throws ServiceException {
		UserAccountDto account = null;
		try {
			account = userDao.getBy(id);
		} catch (IOException e) {
			throw new ServiceException("Cannot find user for id: " + id, e);
		}

		List<String> roles = null;
		String officeName = null;
		UserAccountEntity userAccountEntity = null;
		if (account != null) {
			roles = userRoleService.getBy(id);
			officeName = officeService.getBy(account
					.getOfficeId());
			userAccountEntity = new UserAccountEntity(account, roles,
					officeName);
		}
		return userAccountEntity;
	}

	@Override
	public UserAccountListEntity getBy(
			UserAccountFetchEntity entity) throws ServiceException {
		List<UserAccountDto> accounts = null;
		int userCountByOfficeId;
		try {
			accounts = userDao.getBy(entity.getOfficeId(), entity.getStart(), entity.getLength());
			userCountByOfficeId = userDao.getTotalCount(entity.getOfficeId());
		} catch (IOException e) {
			throw new ServiceException("Cannot find user for officeId: "
					+ entity.getOfficeId(), e);
		}

		List<UserAccountEntity> entities = Lists.newArrayList();
		for (UserAccountDto account : accounts) {
			List<String> roles = userRoleService.getBy(account
					.getId());
			String officeName = officeService.getBy(account
					.getOfficeId());
			UserAccountEntity userAccountEntity = new UserAccountEntity(
					account, roles, officeName);
			entities.add(userAccountEntity);
		}

		return new UserAccountListEntity(entity.getDraw(), userCountByOfficeId,
				userCountByOfficeId, entities);
	}

	@Override
	@Transactional
	public void insert(
			UserAccountCreationEntity userAccountCreationEntity)
			throws ServiceException {
		String userId = userAccountCreationEntity.getId();

		// Insert user account
		UserAccountDto userAccount = new UserAccountDto(userAccountCreationEntity);
		
		try {
			userDao.insert(userAccount);
		} catch (IOException e) {
			throw new ServiceException("Cannot add user account for userId: "
					+ userAccountCreationEntity.getId(), e);
		}

		// Delete & Insert roles
		userRoleService.deleteBy(userId);
		userRoleService.insert(userId,
				userAccountCreationEntity.getRoles());

	}

	@Override
	public void update(UserAccountCreationEntity userAccountCreationEntity) throws ServiceException {
		String userId = userAccountCreationEntity.getId();
		
		if (StringUtils.isEmpty(userAccountCreationEntity.getPassword())) {
			userAccountCreationEntity.setPassword(null);
		}
		
		// Insert user account
		UserAccountDto userAccount = new UserAccountDto(userAccountCreationEntity);
		
		try {
			userDao.update(userAccount);
		} catch (IOException e) {
			throw new ServiceException("Cannot edit user account for userId: " + userAccountCreationEntity.getId(), e);
		}
		
		// Delete & Insert roles
		userRoleService.deleteBy(userId);
		userRoleService.insert(userId, userAccountCreationEntity.getRoles());
	}

	@Override
	public void deleteBy(String id) throws ServiceException {
		try {
			userDao.deleteBy(id);
			userRoleService.deleteBy(id);
		} catch (IOException e) {
			throw new ServiceException("Cannot delete user account for userId: " + id, e);
		}
	}

	@Override
	public String getLanguage(String id) {
		try {
			return userDao.getLanguage(id);
		} catch (IOException e) {
			throw new ServiceException(
					"Cannot get language for user with id : " + id, e);
		}
	}

	@Override
	public void updateLanguage(String id, String language) {
		try {
			userDao.updateLanguage(id, language);
		} catch (IOException e) {
			throw new ServiceException(
					"Cannot update language for user with id : " + id, e);
		}
	}
}
