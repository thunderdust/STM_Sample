package com.worksap.stm.sample.service.spec;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.worksap.stm.sample.entity.CurrentUser;
import com.worksap.stm.sample.exception.ServiceException;


public interface CurrentUserDetailsService extends UserDetailsService {
	
	CurrentUser loadUserByUsername(String id) throws ServiceException;
}
