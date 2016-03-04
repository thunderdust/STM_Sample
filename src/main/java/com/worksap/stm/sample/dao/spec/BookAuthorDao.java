package com.worksap.stm.sample.dao.spec;

import java.io.IOException;
import java.util.List;

import com.worksap.stm.sample.dto.BookAuthorDto;

public interface BookAuthorDao {
	List<BookAuthorDto> getBy(int bookId) throws IOException;
	void insert(BookAuthorDto bookAuthor) throws IOException;
	void deleteBy(int bookId) throws IOException;
}
