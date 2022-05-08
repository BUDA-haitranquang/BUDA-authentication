package com.buda.api.token.verify;

import java.util.Optional;

import com.buda.builder.JwtTokenBuilder;
import com.buda.entities.User;
import com.buda.entities.enumeration.PlanType;
import com.buda.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VerifyTokenService {
    private final JwtTokenBuilder jwtTokenBuilder;
    private final UserRepository userRepository;

    @Autowired
    public VerifyTokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.jwtTokenBuilder = new JwtTokenBuilder();
    }

    public UserIDDTO verifyPremiumUser(TokenDTO tokenDTO) {
        String token = tokenDTO.getAccessToken();
        if (this.jwtTokenBuilder.isValid(token)) {
            UserIDDTO userIDDTO = new UserIDDTO();
            Long userID = jwtTokenBuilder.getUserIDFromToken(token);
            userIDDTO.setUserID(userID);
            Optional<User> userOptional = this.userRepository
                    .findUserByUserID(userID);
            if ((userOptional.isPresent()) && (userOptional.get().getPlanType().equals(PlanType.PREMIUM)))
            {
                return userIDDTO;
            }
            else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not a premium user");            
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");

    }

    public UserIDDTO verifyProUser(TokenDTO tokenDTO) {
        String token = tokenDTO.getAccessToken();
        if (this.jwtTokenBuilder.isValid(token)) {
            UserIDDTO userIDDTO = new UserIDDTO();
            Long userID = jwtTokenBuilder.getUserIDFromToken(token);
            userIDDTO.setUserID(userID);
            Optional<User> userOptional = this.userRepository
                    .findUserByUserID(userID);
            if ((userOptional.isPresent()) && ((userOptional.get().getPlanType().equals(PlanType.PREMIUM))
            || (userOptional.get().getPlanType().equals(PlanType.PRO))))
            {
                return userIDDTO;
            }
            else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not a premium user");            
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
    }

    public UserIDDTO verifyNormalUser(TokenDTO tokenDTO) {
        String token = tokenDTO.getAccessToken();
        if (this.jwtTokenBuilder.isValid(token)) {
            UserIDDTO userIDDTO = new UserIDDTO();
            Long userID = jwtTokenBuilder.getUserIDFromToken(token);
            userIDDTO.setUserID(userID);
            Optional<User> userOptional = this.userRepository
                    .findUserByUserID(userID);
            if (userOptional.isPresent())
            {
                return userIDDTO;
            }
            else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not a premium user");            
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
    }

    public UserIDDTO verifyStaff(TokenDTO tokenDTO) {
        return null;
    }
}
