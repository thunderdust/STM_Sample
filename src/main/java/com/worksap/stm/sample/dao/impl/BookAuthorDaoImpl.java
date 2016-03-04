package com.worksap.stm.sample.dao.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.worksap.stm.sample.dao.spec.BookAuthorDao;
import com.worksap.stm.sample.dto.BookAuthorDto;

@Repository
public class BookAuthorDaoImpl implements BookAuthorDao {

	private static final String FETCH_BY_BOOKID = "SELECT * FROM BOOK_AUTHOR WHERE book_id = ?";
	private static final String DELETE_BY_BOOKID = "DELETE FROM BOOK_AUTHOR WHERE book_id = ?";
	private static final String INSERT_BOOK_AUTHOR = "INSERT INTO BOOK_AUTHOR(book_id, author_id) VALUES (?, ?)";
	
	@Autowired
	private JdbcTemplate template;
	
	@Override
	public List<BookAuthorDto> getBy(int bookId) throws IOException {
		try {
			return template.query(FETCH_BY_BOOKID, ps -> ps.setInt(1, bookId), (rs, rownum) -> {
				return new BookAuthorDto(rs.getInt("book_id"), rs.getInt("author_id"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
 	}

	@Override
	public void insert(BookAuthorDto bookAuthor) throws IOException {
		try {
			template.update(INSERT_BOOK_AUTHOR, (ps) -> {
				ps.setInt(1, bookAuthor.getBookId());
				ps.setInt(2, bookAuthor.getAuthorId());
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
		
	}

	@Override
	public void deleteBy(int bookId) throws IOException {
		try {
			template.update(DELETE_BY_BOOKID, (ps) -> {
				ps.setInt(1, bookId);
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
		
	}

}
