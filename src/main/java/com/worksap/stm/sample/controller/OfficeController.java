package com.worksap.stm.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.worksap.stm.sample.entity.OfficeCreationEntity;
import com.worksap.stm.sample.entity.OfficeEntity;
import com.worksap.stm.sample.entity.OfficeFetchEntity;
import com.worksap.stm.sample.service.spec.OfficeService;

@Controller
public class OfficeController {

	@Autowired
	private OfficeService officeService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/offices", method = RequestMethod.POST)
	@ResponseBody
	public OfficeEntity getOffices(
			@RequestBody OfficeFetchEntity officeFetchEntity) {
		return officeService.getBy(officeFetchEntity);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/office", method = RequestMethod.POST)
	@ResponseBody
	public void addOffice(@RequestBody OfficeCreationEntity office) {
		officeService.insert(office.getName());
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/office", method = RequestMethod.DELETE)
	@ResponseBody
	public boolean deleteOffice(@RequestParam int id) {
		// return false if any user in this office or id does not exist
		return officeService.deleteBy(id);
	}
	
	// page
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/officemanagement")
	public String officemanagement() {
		return "office-management";
	}
}
