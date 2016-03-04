package com.worksap.stm.sample.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worksap.stm.sample.dao.spec.OfficeDao;
import com.worksap.stm.sample.dto.OfficeDto;
import com.worksap.stm.sample.entity.OfficeEntity;
import com.worksap.stm.sample.entity.OfficeFetchEntity;
import com.worksap.stm.sample.exception.ServiceException;
import com.worksap.stm.sample.service.spec.OfficeService;

@Service
public class OfficeServiceImpl implements OfficeService {

	@Autowired
	private OfficeDao officeDao;

	@Override
	public String getBy(int officeId) throws ServiceException {
		OfficeDto office = null;
		try {
			office = officeDao.getBy(officeId);
		} catch (IOException e) {
			throw new ServiceException("Cannot find office for office Id: "
					+ officeId, e);
		}
		String name = null;
		if (office != null) {
			name = office.getName();
		}
		return name;
	}

	@Override
	public OfficeEntity getBy(OfficeFetchEntity entity)
			throws ServiceException {
		try {
			int officeSize = officeDao.getTotalCount();
			List<OfficeDto> officeData = officeDao.getAll(
					entity.getStart(), entity.getLength());
			return new OfficeEntity(entity.getDraw(), officeSize, officeSize,
					officeData);
		} catch (IOException e) {
			throw new ServiceException("Could not search offices", e);
		}
	}

	@Override
	public void insert(String name) throws ServiceException {
		try {
			officeDao.insert(name);
		} catch (IOException e) {
			throw new ServiceException("Could not add office with name: "
					+ name, e);
		}
	}

	@Override
	public boolean deleteBy(int id) throws ServiceException {
		try {
			return officeDao.deleteBy(id);
		} catch (IOException e) {
			throw new ServiceException(
					"Could not delete office with id: " + id, e);
		}
	}
}
