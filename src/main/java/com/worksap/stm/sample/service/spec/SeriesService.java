package com.worksap.stm.sample.service.spec;

import java.util.List;

import com.worksap.stm.sample.dto.SeriesDto;
import com.worksap.stm.sample.exception.ServiceException;

public interface SeriesService {
	String getBy(int id) throws ServiceException;

	List<SeriesDto> getAll();

	void insert(SeriesDto series);

	List<SeriesDto> filter(String searchParam);

	SeriesDto getBy(String name) throws ServiceException;
}
