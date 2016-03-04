package com.worksap.stm.sample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.worksap.stm.sample.dto.SeriesDto;
import com.worksap.stm.sample.entity.SeriesCreationEntity;
import com.worksap.stm.sample.service.spec.SeriesService;

@Controller
public class SeriesController {

	@Autowired
	private SeriesService seriesService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/series", method = RequestMethod.GET)
	@ResponseBody
	public List<SeriesDto> getSeries(@RequestParam(required=false) String q) {
		if (Strings.isNullOrEmpty(q)) {
			return seriesService.getAll();
		} else {
			return seriesService.filter(q);
		}
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/series", method = RequestMethod.POST)
	@ResponseBody
	public SeriesDto addSeries(@RequestBody SeriesCreationEntity series) {
		SeriesDto seriesDto = new SeriesDto();
		String name = series.getName();
		seriesDto.setName(name);
		seriesService.insert(seriesDto);
		
		return seriesService.getBy(name);
	}

}
