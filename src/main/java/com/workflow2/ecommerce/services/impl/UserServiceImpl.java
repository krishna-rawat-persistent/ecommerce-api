package com.workflow2.ecommerce.services.impl;

import com.workflow2.ecommerce.entity.User;
import com.workflow2.ecommerce.dto.Login;
import com.workflow2.ecommerce.dto.Register;
import com.workflow2.ecommerce.repository.UserRepo;
import com.workflow2.ecommerce.dto.Response;
import com.workflow2.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Response> login(Login login) {
        User user = repo.findByEmail(login.getEmail());
        if(user!=null){
            String plaintextPassword = login.getPassword();
            String encryptedPassword = user.getPassword();

            Boolean passwordMatched = passwordEncoder.matches(plaintextPassword,encryptedPassword);
            if(passwordMatched){
                Optional<User> validUser = repo.findOneByEmailAndPassword(login.getEmail(),encryptedPassword);
                if(validUser.isPresent()){
                    return ResponseEntity.ok().body(Response.builder().status(true).message("Login Successful !!").email(login.getEmail()).build());
                }else{
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.builder().status(true).message("Invalid Credentials !!").build());
                }
            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.builder().status(false).message("Credentials not Matching!!").build());
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.builder().status(false).message("Email Not Exist !!").build());
    }

    @Override
    public ResponseEntity<Response> register(Register register) {
        User user = repo.findByEmail(register.getEmail());
        if(user==null) {
            if (register.getName().equals("") || register.getEmail().equals("") || register.getPassword().equals("")) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Response.builder().message("Please fill all the values").status(false).build());
            }
            try {
                Calendar calendar = Calendar.getInstance();
                String day = String.valueOf(calendar.get(Calendar.DATE));
                String month = String.valueOf(calendar.get(Calendar.MONTH));
                String hrs = String.valueOf(calendar.get(calendar.HOUR));
                String min = String.valueOf(calendar.get(Calendar.MINUTE));
                String userId = register.getName().substring(0,3)+register.getEmail().substring(0,2)+day+month+hrs+min;
                repo.save(User.builder().id(UUID.randomUUID())
                        .email(register.getEmail())
                        .name(register.getName())
                        .phoneNo(register.getPhoneNo())
                        .role("User")
                        .password(passwordEncoder.encode(register.getPassword()))
                        .cart(register.getCart())
                        .build());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.builder().message("Some exception occurred").status(false).build());
            }
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Response.builder().email(register.getEmail()).message("Email Already Exist !!").status(false).build());
        }
        return ResponseEntity.status(HttpStatus.OK).body(Response.builder().email(register.getEmail()).message("User Registered Successfully !!").status(true).build());

    }
}
