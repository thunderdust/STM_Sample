package com.worksap.stm.sample.dao.spec;

import java.io.IOException;
import java.util.List;

import com.worksap.stm.sample.dto.UserAccountDto;

public interface UserDao {
	UserAccountDto getBy(String id) throws IOException;

	List<UserAccountDto> getBy(int officeId, int start, int size)
			throws IOException;

	int getTotalCount(int officeId) throws IOException;

	void insert(UserAccountDto userAccount) throws IOException;

	void update(UserAccountDto userAccount) throws IOException;

	void deleteBy(String id) throws IOException;

	String getLanguage(String id) throws IOException;

	void updateLanguage(String id, String language) throws IOException;
}
