package com.worksap.stm.sample.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worksap.stm.sample.dao.spec.SeriesDao;
import com.worksap.stm.sample.dto.SeriesDto;
import com.worksap.stm.sample.exception.ServiceException;
import com.worksap.stm.sample.service.spec.SeriesService;

@Service
public class SeriesServiceImpl implements SeriesService {

	@Autowired
	private SeriesDao seriesDao;
	
	@Override
	public String getBy(int id) throws ServiceException {
		SeriesDto series = null;
		try {
			series = seriesDao.getBy(id);
		} catch (IOException e) {
			throw new ServiceException("Cannot find series for id " + id, e);
		}
		
		String seriesName = null;
		if (series != null) {
			seriesName = series.getName();
		}
		
		return seriesName;
	}
	
	@Override
	public SeriesDto getBy(String name) throws ServiceException {
		SeriesDto series = null;
		try {
			series = seriesDao.getBy(name);
		} catch (IOException e) {
			throw new ServiceException("Cannot find series for name " + name, e);
		}
		
		return series;
	}
	
	@Override
	public List<SeriesDto> getAll() {
		try {
			return seriesDao.getAll();
		} catch (IOException e) {
			throw new ServiceException("Failed to fetch all publishers ", e);
		}
	}

	@Override
	public void insert(SeriesDto series) {
		try {
			seriesDao.insert(series);
		} catch (IOException e) {
			throw new ServiceException("Failed to add new series.", e);
		}
	}
	
	@Override
	public List<SeriesDto> filter(String searchParam) {
		try {
			return seriesDao.filter(searchParam);
		} catch (IOException e) {
			throw new ServiceException("Failed to filter series.", e);
		}
	}

}
