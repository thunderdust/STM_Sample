package com.worksap.stm.sample.entity;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookCreationEntity {
	private int id;
	private String isbn;
	private String title;
	private BigDecimal price;
	private int publisherId;
	private int seriesId;
	private List<Integer> authorIdList;
}
