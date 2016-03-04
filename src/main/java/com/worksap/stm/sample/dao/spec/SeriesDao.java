package com.worksap.stm.sample.dao.spec;

import java.io.IOException;
import java.util.List;

import com.worksap.stm.sample.dto.SeriesDto;

public interface SeriesDao {
	SeriesDto getBy(int id) throws IOException;

	void insert(SeriesDto series) throws IOException;

	void deleteBy(int id) throws IOException;

	List<SeriesDto> getAll() throws IOException;

	List<SeriesDto> filter(String searchParam) throws IOException;

	SeriesDto getBy(String name) throws IOException;
}
