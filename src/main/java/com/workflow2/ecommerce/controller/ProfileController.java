package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.ProfileDTO;
import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.repository.UserDao;
import com.workflow2.ecommerce.services.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ProfileController {
    @Autowired
    CartServiceImpl cartService;
    @Autowired
    UserDao userDao;
    @GetMapping("/profile")
    public ProfileDTO getProfile(HttpServletRequest httpServletRequest)
    {
        System.out.println("in profile");
        User user = cartService.getUser(httpServletRequest);
        ProfileDTO user2 = new ProfileDTO();
        user2.setName(user.getName());
        user2.setId(user.getId());
        user2.setEmail(user.getEmail());
        user2.setAddress(user.getAddress());
        user2.setNumber(user.getPhoneNo());
        return user2;
    }

    @PutMapping("/profile")
    public ProfileDTO updateProfile(HttpServletRequest httpServletRequest, @RequestBody User user2)
    {
        User user = cartService.getUser(httpServletRequest);
        user.setName(user2.getName());
        user.setEmail(user2.getEmail());
        user.setPhoneNo(user2.getPhoneNo());
        user.setAddress(user2.getAddress());
        userDao.save(user);
        ProfileDTO user3 = new ProfileDTO();
        user3.setName(user.getName());
        user3.setId(user.getId());
        user3.setEmail(user.getEmail());
        user3.setAddress(user.getAddress());
        user3.setNumber(user.getPhoneNo());
        return user3;
    }
}
