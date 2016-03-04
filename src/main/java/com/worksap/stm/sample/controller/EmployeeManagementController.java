package com.worksap.stm.sample.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.worksap.stm.sample.entity.UserAccountCreationEntity;
import com.worksap.stm.sample.entity.UserAccountEntity;
import com.worksap.stm.sample.entity.UserAccountFetchEntity;
import com.worksap.stm.sample.entity.UserAccountListEntity;
import com.worksap.stm.sample.service.spec.UserService;

@Controller
public class EmployeeManagementController {
	
	@Autowired
	private UserService userService;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/profilemanagement")
	public String profilemanagement() {
		return "profile-management";
	}
	

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/employeemanagement")
	public String usermanagement() {
		return "employee-management";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/employeemanagement/findUserById", method = RequestMethod.GET)
	@ResponseBody
	public UserAccountEntity findUserById(@RequestParam("id") String id) {
		return userService.getBy(id);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/employeemanagement/findUserByOfficeId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserAccountListEntity findUserByOfficeId(@RequestBody UserAccountFetchEntity entity) {
		return userService.getBy(entity);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/employeemanagement/addUserAccount", method = RequestMethod.POST)
	@ResponseBody
	public void addUserAccount(@RequestBody UserAccountCreationEntity userAccountCreationEntity) {
		userService.insert(userAccountCreationEntity);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/employeemanagement/updateUserAccount", method = RequestMethod.POST)
	@ResponseBody
	public void updateUserAccount(@RequestBody UserAccountCreationEntity userAccountCreationEntity) {
		userService.update(userAccountCreationEntity);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/employeemanagement/deleteUserAccount", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteUserAccount(@RequestParam String id) {
		userService.deleteBy(id);
	}

	@RequestMapping(value = "/employeemanagement/updateLanguage", method = RequestMethod.POST)
	@ResponseBody
	public void updateLanguage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String language) throws IOException {
		userService.updateLanguage(request.getUserPrincipal().getName(),
				language);
		response.sendRedirect(request.getHeader("referer"));
	}
}
