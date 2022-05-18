package com.buda.util;


import javax.servlet.http.HttpServletRequest;

import com.buda.builder.JwtTokenBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RequestResolver {
    private final JwtTokenBuilder jwtTokenBuilder;
    @Autowired
    public RequestResolver(JwtTokenBuilder jwtTokenBuilder) {
        this.jwtTokenBuilder = jwtTokenBuilder;
    }
    public Long getUserIDGeneral(HttpServletRequest httpServletRequest) {
        final String token = httpServletRequest.getHeader("Authorization").substring(7);
        if (jwtTokenBuilder.isValid(token)){
            Long userID = this.jwtTokenBuilder.getUserIDFromToken(token);
            return userID;
        }
        
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");

    }
}
