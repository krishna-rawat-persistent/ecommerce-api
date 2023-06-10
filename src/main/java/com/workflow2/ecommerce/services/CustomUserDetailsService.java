package com.workflow2.ecommerce.services;


import com.workflow2.ecommerce.entity.User;

import com.workflow2.ecommerce.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to identify the role so we can use PreAuthorize annotation
 * @author krishna_rawat
 * @version v0.0.1
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    /**
     * This method loads user or find a user by its username
     * @param name it takes name as username
     * @return user whose name is provided in the parameter
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userDao.findByEmail(name);
        List<SimpleGrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), role);
    }
}
