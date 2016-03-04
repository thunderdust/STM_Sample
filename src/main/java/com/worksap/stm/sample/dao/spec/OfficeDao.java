package com.worksap.stm.sample.dao.spec;

import java.io.IOException;
import java.util.List;

import com.worksap.stm.sample.dto.OfficeDto;

public interface OfficeDao {
	OfficeDto getBy(int officeId) throws IOException;

	int getTotalCount() throws IOException;

	List<OfficeDto> getAll(int start, int size) throws IOException;

	void insert(String name) throws IOException;

	boolean deleteBy(int id) throws IOException;
}
