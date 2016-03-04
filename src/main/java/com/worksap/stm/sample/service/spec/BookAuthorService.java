package com.worksap.stm.sample.service.spec;

import java.util.List;

import com.worksap.stm.sample.dto.AuthorDto;
import com.worksap.stm.sample.exception.ServiceException;

public interface BookAuthorService {
	List<AuthorDto> getBy(int bookId) throws ServiceException;
	void insert(int bookId, int authorId) throws ServiceException;
	void deleteBy(int bookId) throws ServiceException;
}
