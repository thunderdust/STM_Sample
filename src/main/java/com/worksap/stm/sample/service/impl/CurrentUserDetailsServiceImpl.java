package com.worksap.stm.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.worksap.stm.sample.entity.CurrentUser;
import com.worksap.stm.sample.entity.UserAccountEntity;
import com.worksap.stm.sample.service.spec.CurrentUserDetailsService;
import com.worksap.stm.sample.service.spec.UserService;

@Service
public class CurrentUserDetailsServiceImpl implements CurrentUserDetailsService {

    private final UserService userService;

    @Autowired
    public CurrentUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CurrentUser loadUserByUsername(String id) throws UsernameNotFoundException {
        UserAccountEntity user = userService.getBy(id);
        if (user == null) {
        	throw new UsernameNotFoundException(String.format("User with id=%s was not found", id));
        }
        
        return new CurrentUser(user);
    }

}
