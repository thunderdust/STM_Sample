package com.worksap.stm.sample.service.spec;

import com.worksap.stm.sample.entity.UserAccountCreationEntity;
import com.worksap.stm.sample.entity.UserAccountEntity;
import com.worksap.stm.sample.entity.UserAccountFetchEntity;
import com.worksap.stm.sample.entity.UserAccountListEntity;
import com.worksap.stm.sample.exception.ServiceException;

public interface UserService {
	UserAccountEntity getBy(String id) throws ServiceException;

	UserAccountListEntity getBy(UserAccountFetchEntity entity)
			throws ServiceException;

	void insert(UserAccountCreationEntity userAccountCreationEntity)
			throws ServiceException;

	void update(UserAccountCreationEntity userAccountCreationEntity)
			throws ServiceException;

	void deleteBy(String id) throws ServiceException;

	String getLanguage(String id);

	void updateLanguage(String id, String language);
}
