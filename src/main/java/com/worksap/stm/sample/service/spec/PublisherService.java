package com.worksap.stm.sample.service.spec;

import java.util.List;

import com.worksap.stm.sample.dto.PublisherDto;
import com.worksap.stm.sample.exception.ServiceException;

public interface PublisherService {
	String getBy(int id) throws ServiceException;

	List<PublisherDto> getAll();

	void insert(PublisherDto publisher);

	List<PublisherDto> filter(String searchParam);

	PublisherDto getBy(String name) throws ServiceException;
}
