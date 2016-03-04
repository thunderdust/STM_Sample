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
import com.worksap.stm.sample.dto.PublisherDto;
import com.worksap.stm.sample.entity.PublisherCreationEntity;
import com.worksap.stm.sample.service.spec.PublisherService;

@Controller
public class PublisherController {

	@Autowired
	private PublisherService publisherService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/publishers", method = RequestMethod.GET)
	@ResponseBody
	public List<PublisherDto> getPublishers(@RequestParam(required=false) String q) {
		if (Strings.isNullOrEmpty(q)) {
			return publisherService.getAll();
		} else {
			return publisherService.filter(q);
		}
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/publisher", method = RequestMethod.POST)
	@ResponseBody
	public PublisherDto addPublisher(@RequestBody PublisherCreationEntity publisher) {
		PublisherDto newPublisher = new PublisherDto();
		String name = publisher.getName();
		newPublisher.setName(name);
		publisherService.insert(newPublisher);
		
		return publisherService.getBy(name);
	}

}
