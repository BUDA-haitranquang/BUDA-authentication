package com.buda.api.login;

import java.util.Optional;

import com.buda.builder.JwtTokenBuilder;
import com.buda.entities.User;
import com.buda.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoginService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenBuilder jwtTokenBuilder;

    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.jwtTokenBuilder = new JwtTokenBuilder();
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public JwtResponseDTO correctLogin(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        Optional<User> userOptional = this.userRepository.findUserByEmail(email);
        if ((userOptional.isPresent())
                && (bCryptPasswordEncoder.matches(loginDTO.getPassword(), userOptional.get().getPassword()))) {
                    JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
                    jwtResponseDTO.setAccessToken(jwtTokenBuilder.generateAccessToken(userOptional.get()));
                    jwtResponseDTO.setRefreshToken(jwtTokenBuilder.generateRefreshToken(userOptional.get()));
                    return jwtResponseDTO;

        }
        
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
}
