package com.buda.api.password.update;

import java.util.Optional;

import com.buda.api.password.UpdatePasswordDTO;
import com.buda.entities.User;
import com.buda.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UpdatePasswordService {
    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UpdatePasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public void updateUserPassword(Long userID, UpdatePasswordDTO userUpdatePassword) {
        Optional<User> user = userRepository.findUserByUserID(userID);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        String rawPassword = userUpdatePassword.getCurrentPassword();
        if (bCryptPasswordEncoder.matches(rawPassword, user.get().getPassword()) && user.get().getEnabled()) {
            user.get().setPassword(bCryptPasswordEncoder.encode(userUpdatePassword.getNewPassword()));
            this.userRepository.save(user.get());
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");
    }
}
