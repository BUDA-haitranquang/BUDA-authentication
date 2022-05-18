package com.buda.api.password.forgot;

import java.time.LocalDateTime;
import java.util.UUID;

import com.buda.entities.MailConfirmationToken;
import com.buda.entities.User;
import com.buda.entities.enumeration.MailTokenType;
import com.buda.repository.MailConfirmationTokenRepository;
import com.buda.repository.UserRepository;
import com.buda.services.EmailService;
import com.buda.util.SHA256Encoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
@Service
public class SendConfirmForgotPasswordEmailService {
    private final MailConfirmationTokenRepository mailConfirmationTokenRepository;
    @Value("${authentication.url}")
    private String authenticationURL;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    public SendConfirmForgotPasswordEmailService(MailConfirmationTokenRepository mailConfirmationTokenRepository) {
        this.mailConfirmationTokenRepository = mailConfirmationTokenRepository;
    }

    @Transactional
    public void sendMailForgotPassword(String email) {
        User user = userRepository
                .findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        MailConfirmationToken confirmationToken = this.buildConfirmationTokenFor(user);
        confirmationToken.setMailTokenType(MailTokenType.UPDATE_PASSWORD);
        String confirmUrl = authenticationURL + "/api/password/forgot/confirm?token=" + confirmationToken.getToken();
        emailService.send(email, "Forgot password", this.buildAccountConfirmationEmail(confirmUrl));
        this.save(confirmationToken);
    }

    @Transactional
    public void save(MailConfirmationToken token) {
        this.mailConfirmationTokenRepository.save(token);
    }

    public MailConfirmationToken getToken(String token) {
        return mailConfirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Confirmation token not found"));
    }

    @Transactional
    public void setConfirmedAt(String token) {
        mailConfirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    private MailConfirmationToken buildConfirmationTokenFor(User user) {
        String confirmEmailToken = SHA256Encoder.encode(UUID.randomUUID().toString());
        return new MailConfirmationToken(
                confirmEmailToken,
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(10),
                user);
    }

    private String buildAccountConfirmationEmail(String link) {
        return link;
    }
}
