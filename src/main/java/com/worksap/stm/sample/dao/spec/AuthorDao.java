package com.worksap.stm.sample.dao.spec;

import java.io.IOException;
import java.util.List;

import com.worksap.stm.sample.dto.AuthorDto;

public interface AuthorDao {
	AuthorDto getBy(int id) throws IOException;

	List<AuthorDto> getAll() throws IOException;

	void insert(AuthorDto author) throws IOException;

	void deleteBy(int id) throws IOException;

	List<AuthorDto> filter(String searchParam) throws IOException;

	AuthorDto getBy(String name) throws IOException;
}
