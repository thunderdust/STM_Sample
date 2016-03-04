package com.worksap.stm.sample.dto;

import com.worksap.stm.sample.entity.UserAccountCreationEntity;
import com.worksap.stm.sample.utils.PasswordHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAccountDto {
	private String id;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private int officeId;
	
	public UserAccountDto(UserAccountCreationEntity entity) {
		id = entity.getId();
		if (entity.getPassword() != null) {
			PasswordHash hash = new PasswordHash();
			password = hash.encode(entity.getPassword());
		}
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		email = entity.getEmail();
		officeId = entity.getOfficeId();
	}
}
