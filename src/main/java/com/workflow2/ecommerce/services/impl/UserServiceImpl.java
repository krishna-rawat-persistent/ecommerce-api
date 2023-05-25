package com.workflow2.ecommerce.services.impl;

import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.model.Login;
import com.workflow2.ecommerce.model.Register;
import com.workflow2.ecommerce.repository.UserRepo;
import com.workflow2.ecommerce.response.Response;
import com.workflow2.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Response login(Login login) {
        User user = repo.findByEmail(login.getEmail());
        if(user!=null){
            String plaintextPassword = login.getPassword();
            String encryptedPassword = user.getPassword();

            Boolean passwordMatched = passwordEncoder.matches(plaintextPassword,encryptedPassword);
            if(passwordMatched){
                Optional<User> validUser = repo.findOneByEmailAndPassword(login.getEmail(),encryptedPassword);
                if(validUser.isPresent()){
                    return Response.builder().status(true).message("Login Successful !!").email(login.getEmail()).build();
                }else{
                    return Response.builder().status(true).message("Invalid Credentials !!").build();
                }
            }else {
                return Response.builder().status(false).message("Credentials not Matching!!").build();
            }
        }
        return Response.builder().status(false).message("Email Not Exist !!").build();
    }

    @Override
    public Response register(Register register) {
        User user = repo.findByEmail(register.getEmail());
        if(user==null) {
            if (register.getName().equals("") || register.getEmail().equals("") || register.getPassword().equals("")) {
                return Response.builder().message("Please fill all the values").status(false).build();
            }
            try {
                Calendar calendar = Calendar.getInstance();
                String day = String.valueOf(calendar.get(Calendar.DATE));
                String month = String.valueOf(calendar.get(Calendar.MONTH));
                String hrs = String.valueOf(calendar.get(calendar.HOUR));
                String min = String.valueOf(calendar.get(Calendar.MINUTE));

                String userId = register.getName().substring(0,3)+register.getEmail().substring(0,2)+day+month+hrs+min;
                repo.save(User.builder().id(userId)
                        .email(register.getEmail())
                        .name(register.getName())
                        .phoneNo(register.getPhoneNo())
                        .role(register.getRole())
                        .password(passwordEncoder.encode(register.getPassword()))
                        .build());
            } catch (Exception e) {
                return Response.builder().message("Some exception occurred").status(false).build();
            }
        }else{
            return Response.builder().email(register.getEmail()).message("Email Already Exist !!").status(false).build();
        }
        return Response.builder().email(register.getEmail()).message("User Registered Successfully !!").status(true).build();
    }
}
