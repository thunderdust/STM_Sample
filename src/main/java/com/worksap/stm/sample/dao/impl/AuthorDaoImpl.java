package com.worksap.stm.sample.dao.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.worksap.stm.sample.dao.spec.AuthorDao;
import com.worksap.stm.sample.dto.AuthorDto;

@Repository
public class AuthorDaoImpl implements AuthorDao {
	
	private static final String FETCH_BY_ID = "SELECT * FROM AUTHOR WHERE id = ?";
	private static final String FETCH_BY_NAME = "SELECT * FROM AUTHOR WHERE name = ?";
	private static final String DELETE_AUTHOR = "DELETE FROM AUTHOR WHERE id = ?";
	private static final String INSERT_AUTHOR = "INSERT INTO AUTHOR(name) VALUES(?)";
	private static final String FETCH_ALL = "SELECT * FROM AUTHOR";
	private static final String FILTER = "SELECT * FROM AUTHOR WHERE name LIKE ?";
	
	@Autowired
	private JdbcTemplate template;
	
	@Override
	public AuthorDto getBy(int id) throws IOException {
		try {
			return template.queryForObject(FETCH_BY_ID, (rs, rownum) -> {
				return new AuthorDto(rs.getInt("id"), rs.getString("name"));
			}, id);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public AuthorDto getBy(String name) throws IOException {
		try {
			return template.queryForObject(FETCH_BY_NAME, (rs, rownum) -> {
				return new AuthorDto(rs.getInt("id"), rs.getString("name"));
			}, name);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}


	@Override
	public void insert(AuthorDto author) throws IOException {
		try {
			template.update(INSERT_AUTHOR, (ps) -> {
				ps.setString(1, author.getName());
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void deleteBy(int id) throws IOException {
		try {
			template.update(DELETE_AUTHOR, (ps) -> {
				ps.setInt(1, id);
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<AuthorDto> getAll() throws IOException {
		try {
			return template.query(FETCH_ALL, (rs, rownum) -> {
				return new AuthorDto(rs.getInt("id"), rs.getString("name"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public List<AuthorDto> filter(String searchParam) throws IOException {
		try {
			return template.query(FILTER, ps -> {
				ps.setString(1, "%" + searchParam + "%");
			}, (rs, rownum) -> {
				return new AuthorDto(rs.getInt("id"), rs.getString("name"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
}
