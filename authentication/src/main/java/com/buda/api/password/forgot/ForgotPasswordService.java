package com.buda.api.password.forgot;

import java.time.LocalDateTime;
import java.util.Optional;

import com.buda.api.password.UpdatePasswordDTO;
import com.buda.entities.MailConfirmationToken;
import com.buda.entities.User;
import com.buda.repository.MailConfirmationTokenRepository;
import com.buda.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ForgotPasswordService {
    private final UserRepository userRepository;
    private final MailConfirmationTokenRepository mailConfirmationTokenRepository;
    private final SendConfirmForgotPasswordEmailService sendConfirmForgotPasswordEmailService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ForgotPasswordService(UserRepository userRepository,
            MailConfirmationTokenRepository mailConfirmationTokenRepository,
            SendConfirmForgotPasswordEmailService sendConfirmForgotPasswordEmailService) {
        this.sendConfirmForgotPasswordEmailService = sendConfirmForgotPasswordEmailService;
        this.userRepository = userRepository;
        this.mailConfirmationTokenRepository = mailConfirmationTokenRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public void forgotPasswordRequest(SimpleEmailDTO simpleEmailDTO) {
        this.sendConfirmForgotPasswordEmailService.sendMailForgotPassword(simpleEmailDTO.getEmail());
    }

    public void confirmUpdatePassword(String token, UpdatePasswordDTO updatePasswordDTO) {
        Optional<MailConfirmationToken> mailConfirmationTokenOptional = this.mailConfirmationTokenRepository
                .findByToken(token);
        if (mailConfirmationTokenOptional.isPresent()) {
            MailConfirmationToken mailConfirmationToken = mailConfirmationTokenOptional.get();
            User user = mailConfirmationToken.getUser();
            if ((user.getUserID() != null) && (mailConfirmationToken.getExpiredAt().isAfter(LocalDateTime.now()))) {
                user.setPassword(bCryptPasswordEncoder.encode(updatePasswordDTO.getNewPassword()));
                mailConfirmationToken.setConfirmedAt(LocalDateTime.now());
                mailConfirmationToken.setExpiredAt(LocalDateTime.now().minusSeconds(5));
                this.mailConfirmationTokenRepository.save(mailConfirmationToken);
                this.userRepository.save(user);
            } else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token does not exist");
    }
}
