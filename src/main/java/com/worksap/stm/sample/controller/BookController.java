package com.worksap.stm.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.base.Strings;
import com.worksap.stm.sample.entity.BookCreationEntity;
import com.worksap.stm.sample.entity.BookFetchEntity;
import com.worksap.stm.sample.entity.BookListEntity;
import com.worksap.stm.sample.service.spec.BookService;

@Controller
public class BookController {

	@Autowired
	private BookService bookService;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping (value="/books/add", method=RequestMethod.POST)
	@ResponseBody
	public void addBook(@RequestBody BookCreationEntity book) {
		bookService.insert(book);
	}
	
	@RequestMapping (value="/books/books", method=RequestMethod.POST)
	@ResponseBody
	public BookListEntity books(@RequestBody BookFetchEntity entity) {
		if (Strings.isNullOrEmpty(entity.getSearchParam())) {
			return bookService.getAll(entity);
		} else {
			return bookService.filter(entity);
		}
	}
	
	@RequestMapping (value="/books/searchTitle", method=RequestMethod.GET)
	@ResponseBody
	public BookListEntity searchByTitle(@RequestBody BookFetchEntity entity) {
		return bookService.getByTitle(entity);
	}
	
	@RequestMapping (value="/books/searchPublisher", method=RequestMethod.GET)
	@ResponseBody
	public BookListEntity searchByPublisher(@RequestBody BookFetchEntity entity) {
		return bookService.getByPublisher(entity);
	}
	
	@RequestMapping (value="/books/searchSeries", method=RequestMethod.GET)
	@ResponseBody
	public BookListEntity searchBySeries(@RequestBody BookFetchEntity entity) {
		return bookService.getBySeries(entity);
	}
	
	@RequestMapping (value="/books/filter", method=RequestMethod.GET)
	@ResponseBody
	public BookListEntity filterBooks(@RequestBody BookFetchEntity entity) {
		return bookService.filter(entity);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/books", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBook(@RequestParam int id) {
		bookService.deleteBy(id);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping (value="/books/update", method=RequestMethod.POST)
	@ResponseBody
	public void updateBook(@RequestBody BookCreationEntity book) {
		bookService.update(book);
	}
	
	@RequestMapping(value = "/bookmanagement")
	public String bookmanagement() {
		return "book-management";
	}
	
}
