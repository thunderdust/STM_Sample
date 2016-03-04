package com.worksap.stm.sample.dao.spec;

import java.io.IOException;
import java.util.List;

import com.worksap.stm.sample.dto.PublisherDto;

public interface PublisherDao {
	PublisherDto getBy(int id) throws IOException;

	List<PublisherDto> getAll() throws IOException;

	void insert(PublisherDto publisher) throws IOException;

	void deleteBy(int id) throws IOException;

	List<PublisherDto> filter(String searchParam) throws IOException;

	PublisherDto getBy(String name) throws IOException;
}
