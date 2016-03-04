package com.worksap.stm.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookAuthorDto {
	private int bookId;
	private int authorId;
}
