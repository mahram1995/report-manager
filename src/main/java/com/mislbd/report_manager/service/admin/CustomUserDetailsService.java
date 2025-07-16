package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.exception.UsernameNotFoundException;

import com.mislbd.report_manager.repository.admin.SecuUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private SecuUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(user.getUserName(), user.getPassword(), new ArrayList<>());
    }
}

