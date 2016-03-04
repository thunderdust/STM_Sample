package com.worksap.stm.sample.service.spec;

import com.worksap.stm.sample.entity.OfficeEntity;
import com.worksap.stm.sample.entity.OfficeFetchEntity;
import com.worksap.stm.sample.exception.ServiceException;

public interface OfficeService {
	String getBy(int officeId) throws ServiceException;

	OfficeEntity getBy(OfficeFetchEntity entity) throws ServiceException;

	void insert(String name) throws ServiceException;

	boolean deleteBy(int id) throws ServiceException;
}
