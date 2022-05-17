package com.buda.api.register.confirm;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import com.buda.api.register.SendConfirmRegisterMailService;
import com.buda.entities.MailConfirmationToken;
import com.buda.entities.User;
import com.buda.entities.enumeration.MailTokenType;
import com.buda.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConfirmAccountActivationService {
    private final UserRepository userRepository;
    @Autowired
    private SendConfirmRegisterMailService confirmRegisterMailService;

    @Autowired
    public ConfirmAccountActivationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public User confirmAccountActivation(String token) {
        MailConfirmationToken confirmationToken = confirmRegisterMailService.getToken(token);
        if (!confirmationToken.getMailTokenType().equals(MailTokenType.REGISTER)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token type");
        }
        if (confirmationToken.getConfirmedAt() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Confirmation token expired");
        }

        confirmRegisterMailService.setConfirmedAt(token);

        User user = confirmationToken.getUser();
        this.enableUser(confirmationToken.getUser().getEmail());
        return user;
    }

    private void enableUser(String email) {
        // System.out.println(email);
        userRepository.enableUserByEmail(email);
    }
}
