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
import com.worksap.stm.sample.dto.AuthorDto;
import com.worksap.stm.sample.entity.AuthorCreationEntity;
import com.worksap.stm.sample.service.spec.AuthorService;

@Controller
public class AuthorController {

	@Autowired
	private AuthorService authorService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/authors", method = RequestMethod.GET)
	@ResponseBody
	public List<AuthorDto> getAuthors(@RequestParam(required=false) String q) {
		if (Strings.isNullOrEmpty(q)) {
			return authorService.getAll();
		} else {
			return authorService.filter(q);
		}
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/author", method = RequestMethod.POST)
	@ResponseBody
	public AuthorDto addAuthor(@RequestBody AuthorCreationEntity author) {
		AuthorDto newAuthor = new AuthorDto();
		String name = author.getName();
		newAuthor.setName(name);
		authorService.insert(newAuthor);
		
		return authorService.getBy(name);
	}
}
