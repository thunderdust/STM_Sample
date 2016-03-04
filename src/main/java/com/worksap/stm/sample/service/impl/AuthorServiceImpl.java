package com.worksap.stm.sample.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worksap.stm.sample.dao.spec.AuthorDao;
import com.worksap.stm.sample.dto.AuthorDto;
import com.worksap.stm.sample.exception.ServiceException;
import com.worksap.stm.sample.service.spec.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {

	@Autowired
	private AuthorDao authorDao;

	@Override
	public String getBy(int id) throws ServiceException {
		AuthorDto author = null;
		try {
			author = authorDao.getBy(id);
		} catch (IOException e) {
			throw new ServiceException("Cannot find author for id " + id, e);
		}

		String authorName = null;
		if (author != null) {
			authorName = author.getName();
		}

		return authorName;
	}
	
	@Override
	public AuthorDto getBy(String name) throws ServiceException {
		AuthorDto author = null;
		try {
			author = authorDao.getBy(name);
		} catch (IOException e) {
			throw new ServiceException("Cannot find author for name " + name, e);
		}

		return author;
	}

	@Override
	public void insert(AuthorDto author) throws ServiceException {
		try {
			authorDao.insert(author);
		} catch (IOException e) {
			throw new ServiceException("Failed to add author ", e);
		}
	}

	@Override
	public List<AuthorDto> getAll() {
		try {
			return authorDao.getAll();
		} catch (IOException e) {
			throw new ServiceException("Failed to fetch all authors ", e);
		}
	}
	
	@Override
	public List<AuthorDto> filter(String searchParam) {
		try {
			return authorDao.filter(searchParam);
		} catch (IOException e) {
			throw new ServiceException("Failed to filter authors ", e);
		}
	}
}
