package com.workflow2.ecommerce.util;

import com.workflow2.ecommerce.repository.UserDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.workflow2.ecommerce.entity.User;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This class contains all the Utility functionality for JWT authentication and token generation
 * @author Tejas_Badjate
 * @version v0.0.1
 */
@UtilityClass
public class JwtUtil {
    @Autowired
    UserDao userDao;


    private static  final String secret = "secretkey";

    /**
     * This method takes token as parameter and extract Username for that token
     * @param token This is a JWT token that we need to extract username
     * @return This returns name of user
     */
    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * This method return expiration date of token
     * @param token This is a JWT token that we need to extract expiration date
     * @return It returns token expiration date
     */
    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * This method extract claims over the token
     * @param token This is a JWT token
     * @param claimsResolver It is a function which follows functional programming approach to extract claim
     * @param <T> This is  a Type parameter for above function
     * @return This return a type<T> object that have all the claims
     */
    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * This method help us find all possible claims over a JWT token
     * @param token This is a JWT token for extracting all the claims
     * @return It returns all the clain for given token
     */
    private static  Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * This method validate that token is expired or not
     * @param token This is a JWT token
     * @return It return a boolean result true if token is expired otherwise false
     */
    private static Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * This method generate toke using username
     * @param username It is the username given by user to generate token
     * @return It returns token which is String type
     */
    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * This method help to create token using claims and subject(username)
     * @param claims This parameter is a Hashmap of String and Object are type of key and value pair
     * @param subject It is the username which we use to generate token
     * @return It returns token which is String type
     */
    private static String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    /**
     * This method Validate that token is valid or not
     * @param token This is the JWT token which we have to validate
     * @param userDetails This have userDetails of user whose token is going to validate
     * @return It return true if token is valid otherwise it returns false
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractUsername(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * This method checks role from the authorization header
     * @param authorizationHeader It is the authorization header passed by user which have token inside that
     * @return It returns true if user is Admin else it return false for all the users
     */
    public static boolean check_role(String authorizationHeader){
        String token = authorizationHeader.substring(7);
        String name = extractUsername(token);
        System.out.println(name);
        User user = userDao.findByEmail(name);
        System.out.println(user.getRole());
        if ((user.getRole()).equals("Admin"))
            return true;
        else {
            return false;
        }
    }
}

