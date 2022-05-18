package com.buda.api.password.forgot;

import com.buda.api.password.UpdatePasswordDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/password/forgot")
@CrossOrigin("*")
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;
    @Autowired
    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }
    @PostMapping
    public ResponseEntity<?> forgotPasswordRequest(@RequestBody SimpleEmailDTO simpleEmailDTO) {
        this.forgotPasswordService.forgotPasswordRequest(simpleEmailDTO);
        return ResponseEntity.ok().body("Please confirm your email");
    }
    @PutMapping(path = "/confirm")
    public ResponseEntity<?> confirmUpdatePassword(@RequestParam("token") String token, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        this.forgotPasswordService.confirmUpdatePassword(token, updatePasswordDTO);
        return ResponseEntity.ok().body("Password updated!");
    }

}
