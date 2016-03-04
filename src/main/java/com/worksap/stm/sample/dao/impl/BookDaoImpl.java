package com.worksap.stm.sample.dao.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.worksap.stm.sample.dao.spec.BookDao;
import com.worksap.stm.sample.dto.BookDto;

@Repository
public class BookDaoImpl implements BookDao {
	
	private static final String FETCH_BY_ID = "SELECT * FROM BOOK WHERE id = ?";
	private static final String FETCH_ALL = "SELECT * FROM BOOK LIMIT ? OFFSET ?";
	private static final String FETCH_BY_SERIES = "SELECT * FROM BOOK WHERE series_id = ? LIMIT ? OFFSET ?";
	private static final String FETCH_BY_PUBLISHER = "SELECT * FROM BOOK WHERE publisher_id = ? LIMIT ? OFFSET ?";
	private static final String FETCH_BY_AUTHOR = "SELECT * FROM BOOK WHERE id IN (SELECT DISTINCT book_id FROM BOOK_AUTHOR WHERE author_id IN (%s))";
	private static final String INSERT_BOOK = "INSERT INTO BOOK(isbn, title, price, publisher_id, series_id) VALUES (?, ?, ?, ?, ?)";
	private static final String DELETE_BOOK = "DELETE FROM BOOK WHERE id = ?";
	private static final String UPDATE_BOOK = "UPDATE BOOK SET isbn = ?, title = ?, price = ?, publisher_id = ?, series_id = ? WHERE id = ?";
	private static final String FETCH_BY_ISBN = "SELECT * FROM BOOK WHERE isbn = ?";
	private static final String FETCH_BY_TITLE = "SELECT * FROM BOOK WHERE title LIKE ? LIMIT ? OFFSET ?";
	
	private static final String BOOK_COUNT_BY_TITLE = "SELECT COUNT(*) FROM BOOK WHERE title LIKE ?";
	private static final String BOOK_COUNT_TOTAL = "SELECT COUNT(*) FROM BOOK";
	private static final String BOOK_COUNT_BY_PUBLISHER = "SELECT COUNT(*) FROM BOOK WHERE publisher_id = ?";
	private static final String BOOK_COUNT_BY_SERIES = "SELECT COUNT(*) FROM BOOK WHERE series_id = ?";
	
	private static final String BOOK_FILTER_BY_SEARCH = "SELECT * FROM BOOK WHERE isbn LIKE ? OR title LIKE ? "
			+ "OR id IN (SELECT book_id FROM BOOK_AUTHOR WHERE author_id IN (SELECT id FROM AUTHOR WHERE name LIKE ?)) "
			+ "OR series_id IN (SELECT id FROM series WHERE name LIKE ?) "
			+ "OR publisher_id IN (SELECT id FROM publisher WHERE name LIKE ?)"
			+ "LIMIT ? OFFSET ?";
	
	private static final String BOOK_FILTER_COUNT = "SELECT COUNT(*) FROM BOOK WHERE isbn LIKE ? OR LOWER(title) LIKE ? "
			+ "OR id IN (SELECT book_id FROM BOOK_AUTHOR WHERE author_id IN (SELECT id FROM AUTHOR WHERE LOWER(name) LIKE ?)) "
			+ "OR series_id IN (SELECT id FROM series WHERE LOWER(name) LIKE ?) "
			+ "OR publisher_id IN (SELECT id FROM publisher WHERE LOWER(name) LIKE ?)";
	
	@Autowired
	private JdbcTemplate template;
	
	@Override
	public BookDto getBy(int id) throws IOException {
		try {
			return template.queryForObject(FETCH_BY_ID, (rs, rownum) -> {
				return new BookDto(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"), rs.getBigDecimal("price"), rs.getInt("publisher_id"), rs.getInt("series_id"));
			}, id);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<BookDto> getAll(int start, int size) throws IOException {
		try {
			return template.query(FETCH_ALL, ps -> {
				ps.setInt(1, size);
				ps.setInt(2, start);
			}, (rs, rownum) -> {
				return new BookDto(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"), rs.getBigDecimal("price"), rs.getInt("publisher_id"), rs.getInt("series_id"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<BookDto> getBySeries(int seriesId, int start, int size) throws IOException {
		try {
			return template.query(FETCH_BY_SERIES, ps -> {
				ps.setInt(1, seriesId);
				ps.setInt(2, size);
				ps.setInt(3, start);
			}, (rs, rownum) -> {
				return new BookDto(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"), rs.getBigDecimal("price"), rs.getInt("publisher_id"), rs.getInt("series_id"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<BookDto> getByPublisher(int publisherId, int start, int size) throws IOException {
		try {
			return template.query(FETCH_BY_PUBLISHER, ps -> {
				ps.setInt(1, publisherId);
				ps.setInt(2, size);
				ps.setInt(3, start);
			}, (rs, rownum) -> {
				return new BookDto(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"), rs.getBigDecimal("price"), rs.getInt("publisher_id"), rs.getInt("series_id"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<BookDto> getBy(List<Integer> authorIds) throws IOException {
		String inList = makeSqlInString(authorIds);
		String sql = String.format(FETCH_BY_AUTHOR, inList);
		try {
			return template.query(sql, (rs, rownum) -> {
				return new BookDto(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"), rs.getBigDecimal("price"), rs.getInt("publisher_id"), rs.getInt("series_id"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	
	private String makeSqlInString(List<Integer> list) {
		StringBuffer inString = new StringBuffer("");
		
		if (CollectionUtils.isNotEmpty(list)) {
			boolean addComma = false;
			for (Integer i : list) {
				if (addComma) {
					inString.append(",");
				}
				addComma = true;
				inString.append(Integer.toString(i));
			}
		}
		
		return inString.toString();
	}

	@Override
	public void insert(BookDto book) throws IOException {
		try {
			template.update(INSERT_BOOK, (ps) -> {
				ps.setString(1, book.getIsbn());
				ps.setString(2, book.getTitle());
				ps.setBigDecimal(3, book.getPrice());
				ps.setInt(4, book.getPublisherId());
				ps.setInt(5, book.getSeriesId());
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void update(BookDto book) throws IOException {
		try {
			template.update(UPDATE_BOOK, (ps) -> {
				ps.setString(1, book.getIsbn());
				ps.setString(2, book.getTitle());
				ps.setBigDecimal(3, book.getPrice());
				ps.setInt(4, book.getPublisherId());
				ps.setInt(5, book.getSeriesId());
				ps.setInt(6, book.getId());
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void deleteBy(int id) throws IOException {
		try {
			template.update(DELETE_BOOK, ps -> ps.setInt(1, id));
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public BookDto getBy(String isbn) throws IOException {
		try {
			return template.queryForObject(FETCH_BY_ISBN, (rs, rownum) -> {
				return new BookDto(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"), rs.getBigDecimal("price"), rs.getInt("publisher_id"), rs.getInt("series_id"));
			}, isbn);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<BookDto> getBy(String title, int start, int size) throws IOException {
		try {
			return template.query(FETCH_BY_TITLE, ps -> {
				ps.setString(1, "%" + title + "%");
				ps.setInt(2, size);
				ps.setInt(3, start);
			}, (rs, rownum) -> {
				return new BookDto(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"), rs.getBigDecimal("price"), rs.getInt("publisher_id"), rs.getInt("series_id"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public int getTotalCount() throws IOException {
		try {
			return template.queryForObject(BOOK_COUNT_TOTAL, (rs, rownum) -> {
				return rs.getInt(1);
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public int getTotalCountOfSeries(int seriesId) throws IOException {
		try {
			return template.queryForObject(BOOK_COUNT_BY_SERIES, (rs, rownum) -> {
				return rs.getInt(1);
			}, seriesId);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public int getTotalCountOfPublisher(int publisherId) throws IOException {
		try {
			return template.queryForObject(BOOK_COUNT_BY_PUBLISHER, (rs, rownum) -> {
				return rs.getInt(1);
			}, publisherId);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public int getTotalCount(String title) throws IOException {
		try {
			return template.queryForObject(BOOK_COUNT_BY_TITLE, (rs, rownum) -> {
				return rs.getInt(1);
			}, "%" + title + "%");
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<BookDto> filter(String searchParam, int start, int size) throws IOException {
		try {
			return template.query(BOOK_FILTER_BY_SEARCH, (ps) -> {
				ps.setString(1, "%" + searchParam + "%");
				ps.setString(2, "%" + searchParam + "%");
				ps.setString(3, "%" + searchParam + "%");
				ps.setString(4, "%" + searchParam + "%");
				ps.setString(5, "%" + searchParam + "%");
				ps.setInt(6, size);
				ps.setInt(7, start);
			}, (rs, rownum) -> {
				return new BookDto(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"), rs.getBigDecimal("price"), rs.getInt("publisher_id"), rs.getInt("series_id"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public int getFilteredCount(String searchParam) throws IOException {
		try {
			return template.queryForObject(BOOK_FILTER_COUNT, (rs, rownum) -> {
				return rs.getInt(1);
			}, "%" + searchParam + "%", "%" + searchParam + "%", "%" + searchParam + "%", "%" + searchParam + "%", "%" + searchParam + "%");
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

}
