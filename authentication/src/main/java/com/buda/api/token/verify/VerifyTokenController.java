package com.buda.api.token.verify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/token/verify")
@CrossOrigin("*")
public class VerifyTokenController {
    private final VerifyTokenService verifyTokenService;
    @Autowired
    public VerifyTokenController(VerifyTokenService verifyTokenService) {
        this.verifyTokenService = verifyTokenService;
    }
    @PostMapping("/premium")
    public ResponseEntity<?> verifyPremiumUser(TokenDTO tokenDTO) {
        return ResponseEntity.ok().body("arg0");
    }
    @PostMapping("/pro")
    public ResponseEntity<?> verifyProUser(TokenDTO tokenDTO) {
        return ResponseEntity.ok().body("");
    }
    @PostMapping("/normal")
    public ResponseEntity<?> verifyNormalUser(TokenDTO tokenDTO) {
        return ResponseEntity.ok().body("");
    }
}
