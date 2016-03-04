package com.worksap.stm.sample.service.spec;

import java.util.List;

import com.worksap.stm.sample.dto.AuthorDto;
import com.worksap.stm.sample.exception.ServiceException;

public interface AuthorService {
	String getBy(int id) throws ServiceException;

	void insert(AuthorDto author) throws ServiceException;

	List<AuthorDto> getAll();

	List<AuthorDto> filter(String searchParam);

	AuthorDto getBy(String name) throws ServiceException;
}
