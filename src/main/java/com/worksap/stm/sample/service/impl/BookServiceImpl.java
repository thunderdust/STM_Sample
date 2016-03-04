package com.worksap.stm.sample.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worksap.stm.sample.dao.spec.BookDao;
import com.worksap.stm.sample.dto.AuthorDto;
import com.worksap.stm.sample.dto.BookDto;
import com.worksap.stm.sample.entity.BookCreationEntity;
import com.worksap.stm.sample.entity.BookFetchEntity;
import com.worksap.stm.sample.entity.BookListEntity;
import com.worksap.stm.sample.entity.BookPresentationEntity;
import com.worksap.stm.sample.exception.ServiceException;
import com.worksap.stm.sample.service.spec.AuthorService;
import com.worksap.stm.sample.service.spec.BookAuthorService;
import com.worksap.stm.sample.service.spec.BookService;
import com.worksap.stm.sample.service.spec.PublisherService;
import com.worksap.stm.sample.service.spec.SeriesService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookDao bookDao;
	
	@Autowired
	private PublisherService publisherService;
	
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private SeriesService seriesService;
	
	@Autowired
	private BookAuthorService bookAuthorService;
	
	@Override
	public BookListEntity getAll(BookFetchEntity entity) throws ServiceException {
		List<BookDto> bookList = null;
		int bookCount = 0;
		try {
			bookList = bookDao.getAll(entity.getStart(), entity.getLength());
			bookCount = bookDao.getTotalCount();
		} catch (IOException e) {
			throw new ServiceException("Cannot fetch book list", e);
		}
		
		return createBookListEntity(entity, bookList, bookCount);
	}

	@Override
	public BookPresentationEntity getBy(int id) throws ServiceException {
		BookDto book = null;
		try {
			book = bookDao.getBy(id);
		} catch (IOException e) {
			throw new ServiceException("Cannot find book by id " + id, e);
		}
		
		BookPresentationEntity bookDto = null;
		if (book != null) {
			bookDto = makeBookDto(book);
		}
		
		return bookDto;
	}

	private BookPresentationEntity makeBookDto(BookDto book) {
		BookPresentationEntity bookDto = new BookPresentationEntity();
		
		bookDto.setId(book.getId());
		bookDto.setIsbn(book.getIsbn());
		bookDto.setPrice(book.getPrice());
		bookDto.setTitle(book.getTitle());

		bookDto.setPublisherId(book.getPublisherId());
		String publisher = publisherService.getBy(book.getPublisherId());
		bookDto.setPublisher(publisher);

		bookDto.setSeriesId(book.getSeriesId());
		String series = seriesService.getBy(book.getSeriesId());
		bookDto.setSeries(series);
		
		List<AuthorDto> authorList = bookAuthorService.getBy(book.getId());
		bookDto.setAuthors(authorList);
		
		return bookDto;
	}
	
	@Override
	@Transactional
	public void deleteBy(int id) throws ServiceException {
		try {
			bookDao.deleteBy(id);
			bookAuthorService.deleteBy(id);
		} catch (IOException e) {
			throw new ServiceException("Unable to delete book", e);
		}
	}

	@Override
	@Transactional
	public void insert(BookCreationEntity book) throws ServiceException {
		try {
			BookDto bookDto = new BookDto();
			bookDto.setIsbn(book.getIsbn());
			bookDto.setPrice(book.getPrice());
			bookDto.setPublisherId(book.getPublisherId());
			bookDto.setSeriesId(book.getSeriesId());
			bookDto.setTitle(book.getTitle());
			
			bookDao.insert(bookDto);
			
			bookDto = bookDao.getBy(book.getIsbn());
			
			for (Integer authorId : book.getAuthorIdList()) {
				bookAuthorService.insert(bookDto.getId(), authorId);
			}
		} catch (IOException e) {
			throw new ServiceException("Unable to add book", e);
		}
	}

	@Override
	public BookListEntity getByTitle(BookFetchEntity entity) throws ServiceException {
		List<BookDto> bookList = null;
		int bookCount = 0;
		try {
			bookList = bookDao.getBy(entity.getTitle(), entity.getStart(), entity.getLength());
			bookCount = bookDao.getTotalCount(entity.getTitle());
		} catch (IOException e) {
			throw new ServiceException("Unable to fetch by title", e);
		}
		
		return createBookListEntity(entity, bookList, bookCount);
	}

	@Override
	public BookListEntity getByPublisher(BookFetchEntity entity) throws ServiceException {
		List<BookDto> bookList = null;
		int bookCount = 0;
		try {
			bookList = bookDao.getByPublisher(entity.getPublisherId(), entity.getStart(), entity.getLength());
			bookCount = bookDao.getTotalCountOfPublisher(entity.getPublisherId());
		} catch (IOException e) {
			throw new ServiceException("Unable to fetch by publisher", e);
		}
		
		return createBookListEntity(entity, bookList, bookCount);
	}

	@Override
	public BookListEntity getBySeries(BookFetchEntity entity) throws ServiceException {
		List<BookDto> bookList = null;
		int bookCount = 0;
		try {
			bookList = bookDao.getBySeries(entity.getSeriesId(), entity.getStart(), entity.getLength());
			bookCount = bookDao.getTotalCountOfSeries(entity.getSeriesId());
		} catch (IOException e) {
			throw new ServiceException("Unable to fetch by series", e);
		}
		
		return createBookListEntity(entity, bookList, bookCount);
	}

	@Override
	public BookListEntity filter(BookFetchEntity entity) throws ServiceException {
		List<BookDto> bookList = null;
		int bookCount = 0;
		try {
			bookList = bookDao.filter(entity.getSearchParam().toLowerCase(), entity.getStart(), entity.getLength());
			bookCount = bookDao.getFilteredCount(entity.getSearchParam().toLowerCase());
		} catch (IOException e) {
			throw new ServiceException("Error occured in filtering", e);
		}
		
		return createBookListEntity(entity, bookList, bookCount);
	}

	private BookListEntity createBookListEntity(BookFetchEntity entity, List<BookDto> bookList, int bookCount) {
		List<BookPresentationEntity> bookDtoList = new ArrayList<BookPresentationEntity>();
		if (CollectionUtils.isNotEmpty(bookList)) {
			for (BookDto book : bookList) {
				bookDtoList.add(makeBookDto(book));
			}
		}
		
		return new BookListEntity(entity.getDraw(), bookCount, bookCount, bookDtoList);
	}

	@Override
	@Transactional
	public void update(BookCreationEntity book) throws ServiceException {
		try {
			BookDto bookDto = new BookDto();
			bookDto.setId(book.getId());
			bookDto.setIsbn(book.getIsbn());
			bookDto.setPrice(book.getPrice());
			bookDto.setPublisherId(book.getPublisherId());
			bookDto.setSeriesId(book.getSeriesId());
			bookDto.setTitle(book.getTitle());
			
			bookDao.update(bookDto);
			bookAuthorService.deleteBy(book.getId());
			
			for (Integer authorId : book.getAuthorIdList()) {
				bookAuthorService.insert(bookDto.getId(), authorId);
			}
		} catch (IOException e) {
			throw new ServiceException("Unable to update book ", e);
		}
	}
}
