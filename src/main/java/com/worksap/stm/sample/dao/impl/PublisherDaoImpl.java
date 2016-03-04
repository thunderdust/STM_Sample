package com.worksap.stm.sample.dao.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.worksap.stm.sample.dao.spec.PublisherDao;
import com.worksap.stm.sample.dto.PublisherDto;

@Repository
public class PublisherDaoImpl implements PublisherDao {

	private static final String FETCH_BY_ID = "SELECT * FROM PUBLISHER WHERE id = ?";
	private static final String FETCH_BY_NAME = "SELECT * FROM PUBLISHER WHERE name = ?";
	private static final String FETCH_ALL = "SELECT * FROM PUBLISHER";
	private static final String DELETE_PUBLISHER = "DELETE FROM PUBLISHER WHERE id = ?";
	private static final String INSERT_PUBLISHER = "INSERT INTO PUBLISHER(name) VALUES(?)";
	private static final String FILTER_PUBLISHER = "SELECT * FROM PUBLISHER WHERE name LIKE ?";
	
	@Autowired
	private JdbcTemplate template;
	
	@Override
	public PublisherDto getBy(int id) throws IOException {
		try {
			return template.queryForObject(FETCH_BY_ID, (rs, rownum) -> {
				return new PublisherDto(rs.getInt("id"), rs.getString("name"));
			} , id);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public PublisherDto getBy(String name) throws IOException {
		try {
			return template.queryForObject(FETCH_BY_NAME, (rs, rownum) -> {
				return new PublisherDto(rs.getInt("id"), rs.getString("name"));
			} , name);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void insert(PublisherDto publisher) throws IOException {
		try {
			template.update(INSERT_PUBLISHER, (ps) -> {
				ps.setString(1, publisher.getName());
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
		
	}

	@Override
	public void deleteBy(int id) throws IOException {
		try {
			template.update(DELETE_PUBLISHER, (ps) -> {
				ps.setInt(1, id);
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<PublisherDto> getAll() throws IOException {
		try {
			return template.query(FETCH_ALL, (rs, rownum) -> {
				return new PublisherDto(rs.getInt("id"), rs.getString("name"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public List<PublisherDto> filter(String searchParam) throws IOException {
		try {
			return template.query(FILTER_PUBLISHER, ps -> {
				ps.setString(1, "%" + searchParam + "%");
			}, (rs, rownum) -> {
				return new PublisherDto(rs.getInt("id"), rs.getString("name"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

}
