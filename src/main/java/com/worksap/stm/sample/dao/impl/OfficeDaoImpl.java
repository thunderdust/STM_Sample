package com.worksap.stm.sample.dao.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.worksap.stm.sample.dao.spec.OfficeDao;
import com.worksap.stm.sample.dto.OfficeDto;

@Repository
public class OfficeDaoImpl implements OfficeDao {
	private static final String FETCH_BY_ID = "SELECT * FROM OFFICE WHERE id = ?";

	private static final String FETCH_OFFICES = "SELECT * FROM OFFICE LIMIT ? OFFSET ?";

	private static final String INSERT_OFFICE = "INSERT INTO OFFICE (name) VALUES(?)";

	private static final String CHECK_OFFICE_EMPLOYEES = "SELECT COUNT(*) FROM OFFICE, USER_ACCOUNT"
			+ " WHERE OFFICE.id = office_id AND office_id = ?";

	private static final String DELETE_OFFICE = "DELETE FROM OFFICE WHERE id = ?";

	private static final String OFFICE_TABLE_SIZE = "SELECT COUNT(*) FROM OFFICE";

	@Autowired
	private JdbcTemplate template;

	@Override
	public OfficeDto getBy(int officeId) throws IOException {
		try {
			return template.queryForObject(FETCH_BY_ID, (rs, rownum) -> {
				return new OfficeDto(rs.getInt("id"), rs.getString("name"));
			}, officeId);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<OfficeDto> getAll(int start, int size) throws IOException {
		try {
			return template.query(FETCH_OFFICES, ps -> {
				ps.setInt(1, size);
				ps.setInt(2, start);
			}, (rs, rownum) -> {
				return new OfficeDto(rs.getInt("id"), rs.getString("name"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void insert(String name) throws IOException {
		try {
			template.update(INSERT_OFFICE, ps -> ps.setString(1, name));
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public boolean deleteBy(int id) throws IOException {
		try {
			int numOfficeEmployees = template.queryForObject(
					CHECK_OFFICE_EMPLOYEES, (rs, rowNum) -> {
						return rs.getInt(1);
					}, id);
			if (numOfficeEmployees > 0) {
				return false;
			}
			int numDelete = template.update(DELETE_OFFICE,
					ps -> ps.setInt(1, id));
			if (numDelete == 0) {
				return false;
			}
			return true;
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public int getTotalCount() throws IOException {
		return template.queryForObject(OFFICE_TABLE_SIZE, (rs, rownum) -> {
			return rs.getInt(1);
		});
	}
}
