package com.worksap.stm.sample.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BookFetchEntity {
	private String searchParam;
	private String title;
	private int publisherId;
	private int seriesId;
	private int draw;
	private int start;
	private int length;
}
