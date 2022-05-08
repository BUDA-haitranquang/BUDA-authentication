package com.buda.api.token.refresh;
import java.util.Optional;

import com.buda.api.login.JwtResponseDTO;
import com.buda.builder.JwtTokenBuilder;
import com.buda.entities.User;
import com.buda.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
public class RefreshTokenService{
    private final UserRepository userRepository;
    private final JwtTokenBuilder jwtTokenUtil;
    @Autowired
    public RefreshTokenService(UserRepository userRepository, JwtTokenBuilder jwtTokenBuilder)
    {
        this.jwtTokenUtil = jwtTokenBuilder;
        this.userRepository = userRepository;
    }
    public JwtResponseDTO generateNewToken(String token)
    {
        if ((jwtTokenUtil.isValid(token)) && (jwtTokenUtil.getTokenTypeFromToken(token).equals("Refresh")))
        {
            Optional<User> mailUser = this.userRepository.findUserByUserID(jwtTokenUtil.getUserIDFromToken(token));
            if (mailUser.isPresent())
            {
                String jwtaccessToken = jwtTokenUtil.generateAccessToken(mailUser.get());
                String jwtrefreshToken = jwtTokenUtil.generateRefreshToken(mailUser.get());
                return new JwtResponseDTO(jwtaccessToken, jwtrefreshToken);
            }
            else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
    }
}
