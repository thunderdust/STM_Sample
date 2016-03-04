package com.worksap.stm.sample.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.worksap.stm.sample.dto.OfficeDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficeEntity {
	private int draw;
	private int recordsTotal;
	private int recordsFiltered;
	private List<OfficeDto> offices;
}
