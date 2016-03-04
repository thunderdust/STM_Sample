package com.worksap.stm.sample.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worksap.stm.sample.dao.spec.PublisherDao;
import com.worksap.stm.sample.dto.PublisherDto;
import com.worksap.stm.sample.exception.ServiceException;
import com.worksap.stm.sample.service.spec.PublisherService;

@Service
public class PublisherServiceImpl implements PublisherService {

	@Autowired
	private PublisherDao publisherDao;

	@Override
	public String getBy(int id) throws ServiceException {
		PublisherDto publisher = null;
		try {
			publisher = publisherDao.getBy(id);
		} catch (IOException e) {
			throw new ServiceException("Cannot find publisher for id " + id, e);
		}

		String publisherName = null;
		if (publisher != null) {
			publisherName = publisher.getName();
		}

		return publisherName;
	}
	
	@Override
	public PublisherDto getBy(String name) throws ServiceException {
		PublisherDto publisher = null;
		try {
			publisher = publisherDao.getBy(name);
		} catch (IOException e) {
			throw new ServiceException("Cannot find publisher for name " + name, e);
		}

		return publisher;
	}

	@Override
	public List<PublisherDto> getAll() {
		try {
			return publisherDao.getAll();
		} catch (IOException e) {
			throw new ServiceException("Failed to fetch all publishers ", e);
		}
	}

	@Override
	public void insert(PublisherDto publisher) {
		try {
			publisherDao.insert(publisher);
		} catch (IOException e) {
			throw new ServiceException("Failed to fetch all publishers ", e);
		}
	}
	
	@Override
	public List<PublisherDto> filter(String searchParam) {
		try {
			return publisherDao.filter(searchParam);
		} catch (IOException e) {
			throw new ServiceException("Failed to filter publishers ", e);
		}
	}

}
