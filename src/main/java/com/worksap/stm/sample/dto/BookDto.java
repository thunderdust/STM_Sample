package com.worksap.stm.sample.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDto {
	private int id;
	private String isbn;
	private String title;
	private BigDecimal price;
	private int publisherId;
	private int seriesId;
}
