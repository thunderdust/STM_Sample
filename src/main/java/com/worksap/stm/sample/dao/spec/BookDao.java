package com.worksap.stm.sample.dao.spec;

import java.io.IOException;
import java.util.List;

import com.worksap.stm.sample.dto.BookDto;

public interface BookDao {
	BookDto getBy(int id) throws IOException;
	List<BookDto> getAll(int start, int size) throws IOException;
	List<BookDto> getBySeries(int seriesId, int start, int size) throws IOException;
	List<BookDto> getByPublisher(int publisherId, int start, int size) throws IOException;
	List<BookDto> getBy(List<Integer> authorIds) throws IOException;
	List<BookDto> getBy(String title, int start, int size) throws IOException;
	List<BookDto> filter(String searchParam, int start, int size) throws IOException;
	void insert(BookDto book) throws IOException;

	void update(BookDto book) throws IOException;

	void deleteBy(int id) throws IOException;

	BookDto getBy(String isbn) throws IOException;
	
	int getTotalCount() throws IOException;
	int getTotalCountOfSeries(int seriesId) throws IOException;
	int getTotalCountOfPublisher(int publisherId) throws IOException;
	int getTotalCount(String title) throws IOException;
	int getFilteredCount(String searchParam) throws IOException;
}
