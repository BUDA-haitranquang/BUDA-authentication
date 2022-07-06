package com.buda.api.login.social.google;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buda.api.login.JwtResponseDTO;
import com.buda.builder.JwtTokenBuilder;
import com.buda.entities.User;
import com.buda.entities.enumeration.PlanType;
import com.buda.repository.RoleRepository;
import com.buda.repository.UserRepository;
@Service
public class GoogleLoginService {
    private final UserRepository userRepository;
    private final JwtTokenBuilder jwtTokenBuilder;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public GoogleLoginService(UserRepository userRepository, JwtTokenBuilder jwtTokenUtil, RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.jwtTokenBuilder = jwtTokenUtil;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }
    @Transactional
    public JwtResponseDTO processGoogleUserPostLogin(GoogleUserPayload googleUserPayload)
    {
        String email = googleUserPayload.getEmail();
        Optional<User> mailUser = this.userRepository.findUserByEmail(email);
        //Da tung dang nhap bang email giong voi gmail dang dung de dang nhap
        if (mailUser.isPresent())
        {
            User user = mailUser.get();
            String jwtAccessToken = jwtTokenBuilder.generateAccessToken(user);
            String jwtRefreshToken = jwtTokenBuilder.generateRefreshToken(user);
            return new JwtResponseDTO(jwtAccessToken, jwtRefreshToken);
        }
        //Chua tung dang nhap bang email nao giong voi gmail dang dung de dang nhap
        else
        {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(googleUserPayload.getFamilyName());
            newUser.setLastName(googleUserPayload.getGivenName());
            newUser.addRole(roleRepository.findRoleByName("USER").get());
            UUID uuid = UUID.randomUUID();
            newUser.setPassword(bCryptPasswordEncoder.encode(uuid.toString()));
            newUser.setPlanType(PlanType.BASIC);
            userRepository.save(newUser);
            String jwtAccessToken = jwtTokenBuilder.generateAccessToken(newUser);
            String jwtRefreshToken = jwtTokenBuilder.generateRefreshToken(newUser);
            return new JwtResponseDTO(jwtAccessToken, jwtRefreshToken);
        }
    }
}
