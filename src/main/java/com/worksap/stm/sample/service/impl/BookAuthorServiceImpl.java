package com.worksap.stm.sample.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.worksap.stm.sample.dao.spec.BookAuthorDao;
import com.worksap.stm.sample.dto.AuthorDto;
import com.worksap.stm.sample.dto.BookAuthorDto;
import com.worksap.stm.sample.exception.ServiceException;
import com.worksap.stm.sample.service.spec.AuthorService;
import com.worksap.stm.sample.service.spec.BookAuthorService;

@Service
public class BookAuthorServiceImpl implements BookAuthorService {

	@Autowired
	private BookAuthorDao bookAuthorDao;
	
	@Autowired
	private AuthorService authorService;
	
	@Override
	public List<AuthorDto> getBy(int bookId) throws ServiceException {
		List<BookAuthorDto> bookAuthors = null;
		try {
			bookAuthors = bookAuthorDao.getBy(bookId);
		} catch (IOException e) {
			throw new ServiceException("Cannot find book authors for book id " + bookId, e);
		}
		
		List<AuthorDto> authors = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(bookAuthors)) {
			for (BookAuthorDto bookAuthor : bookAuthors) {
				int authorId = bookAuthor.getAuthorId();
				String authorName = authorService.getBy(authorId);
				authors.add(new AuthorDto(authorId, authorName));
			}
		}
		
		return authors;
	}

	@Override
	public void insert(int bookId, int authorId) throws ServiceException {
		try {
			bookAuthorDao.insert(new BookAuthorDto(bookId, authorId));
		} catch (IOException e) {
			throw new ServiceException("Unable to add author for book id " + bookId, e);
		}
	}

	@Override
	public void deleteBy(int bookId) throws ServiceException {
		try {
			bookAuthorDao.deleteBy(bookId);
		} catch (IOException e) {
			throw new ServiceException("Unable to delete authors with bookId :" + bookId, e);
		}
	}

}
