package com.workflow2.ecommerce.services;


import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo repository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = repository.findByEmail(name);
        List<SimpleGrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), role);
    }
}
