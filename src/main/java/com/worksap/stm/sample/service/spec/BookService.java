package com.worksap.stm.sample.service.spec;

import com.worksap.stm.sample.entity.BookCreationEntity;
import com.worksap.stm.sample.entity.BookFetchEntity;
import com.worksap.stm.sample.entity.BookListEntity;
import com.worksap.stm.sample.entity.BookPresentationEntity;
import com.worksap.stm.sample.exception.ServiceException;

public interface BookService {
	BookListEntity getAll(BookFetchEntity entity) throws ServiceException;
	BookPresentationEntity getBy(int id) throws ServiceException;
	void insert(BookCreationEntity book) throws ServiceException;
	BookListEntity getByTitle(BookFetchEntity entity) throws ServiceException;
	BookListEntity getByPublisher(BookFetchEntity entity) throws ServiceException;
	BookListEntity getBySeries(BookFetchEntity entity) throws ServiceException;
	BookListEntity filter(BookFetchEntity entity) throws ServiceException;
	void deleteBy(int id) throws ServiceException;
	void update(BookCreationEntity book) throws ServiceException;
}
