package com.workflow2.ecommerce.filter;


import com.workflow2.ecommerce.services.impl.CustomUserDetailsService;
import com.workflow2.ecommerce.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class have all the functionality to filter requests
 * @author Tejas_Badjate
 * @Refrence https://www.youtube.com/watch?v=rBNOc4ymd1E
 * @version v0.0.1
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

//    @Autowired
//    private JwtUtil JwtUtil;
    @Autowired
    private CustomUserDetailsService service;

    /**
     * This method filter requests based on the access rights given to the user
     * @param httpServletRequest This parameter contains request attributes for the API call
     * @param httpServletResponse This parameter contains response attribute for the API call
     * @param filterChain This parameter have all the filter chains that we need validate according to provided conditions
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String token = null;
        String name = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            name = JwtUtil.extractUsername(token);
        }

        if (name != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = service.loadUserByUsername(name);

            if (JwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


}

