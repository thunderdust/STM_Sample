package com.worksap.stm.sample.dao.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.worksap.stm.sample.dao.spec.SeriesDao;
import com.worksap.stm.sample.dto.SeriesDto;

@Repository
public class SeriesDaoImpl implements SeriesDao {

	private static final String FETCH_BY_ID = "SELECT * FROM SERIES WHERE id = ?";
	private static final String FETCH_BY_NAME = "SELECT * FROM SERIES WHERE name = ?";
	private static final String FETCH_ALL = "SELECT * FROM SERIES";
	private static final String INSERT_SERIES = "INSERT INTO SERIES(name) VALUES(?)";
	private static final String DELETE_SERIES = "DELETE FROM SERIES WHERE id = ?";
	private static final String FILTER_SERIES = "SELECT * FROM SERIES WHERE name LIKE ?";
	
	@Autowired
	private JdbcTemplate template;
	
	@Override
	public SeriesDto getBy(int id) throws IOException {
		try {
			return template.queryForObject(FETCH_BY_ID, (rs, rownum) -> {
				return new SeriesDto(rs.getInt("id"), rs.getString("name"));
			} ,id);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public SeriesDto getBy(String name) throws IOException {
		try {
			return template.queryForObject(FETCH_BY_NAME, (rs, rownum) -> {
				return new SeriesDto(rs.getInt("id"), rs.getString("name"));
			} ,name);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public void insert(SeriesDto series) throws IOException {
		try {
			template.update(INSERT_SERIES, (ps) -> {
				ps.setString(1, series.getName());
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
		
	}

	@Override
	public void deleteBy(int id) throws IOException {
		try {
			template.update(DELETE_SERIES, (ps) -> {
				ps.setInt(1, id);
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<SeriesDto> getAll() throws IOException {
		try {
			return template.query(FETCH_ALL, (rs, rownum) -> {
				return new SeriesDto(rs.getInt("id"), rs.getString("name"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public List<SeriesDto> filter(String searchParam) throws IOException {
		try {
			return template.query(FILTER_SERIES, ps -> {
				ps.setString(1, "%" + searchParam + "%");
			}, (rs, rownum) -> {
				return new SeriesDto(rs.getInt("id"), rs.getString("name"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

}
