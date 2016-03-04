package com.worksap.stm.sample.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

import com.worksap.stm.sample.dto.AuthorDto;
import com.worksap.stm.sample.dto.BookDto;

@Data
public class BookPresentationEntity {
	private int id;
	private String isbn;
	private String title;
	private BigDecimal price;
	private int publisherId;
	private String publisher;
	private int seriesId;
	private String series;
	private List<Integer> authorIds;
	private List<String> authorList;
	
	public BookPresentationEntity() {
		
	}
	
	public BookPresentationEntity(BookDto book, String series, String publisher, List<String> authors) {
		this.id = book.getId();
		this.isbn = book.getIsbn();
		this.title = book.getTitle();
		this.price = book.getPrice();
		this.publisher = publisher;
		this.series = series;
		this.authorList = authors;
	}
	
	public void setAuthors(List<AuthorDto> authorDtoList) {
		this.authorIds = authorDtoList.stream().map(author -> author.getId()).collect(Collectors.toList());
		this.authorList = authorDtoList.stream().map(author -> author.getName()).collect(Collectors.toList());
	}
	
}
