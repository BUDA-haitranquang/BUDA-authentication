package com.buda.api.token.verify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/token/verify")
@CrossOrigin(origins = {"*"})
public class VerifyTokenController {
    private final VerifyTokenService verifyTokenService;
    @Autowired
    public VerifyTokenController(VerifyTokenService verifyTokenService) {
        this.verifyTokenService = verifyTokenService;
    }
    @GetMapping
    public String hello() {
        return "Hello";
    }
    @PostMapping("/premium")
    public ResponseEntity<?> verifyPremiumUser(@RequestBody TokenDTO tokenDTO) {
        return ResponseEntity.ok().body(this.verifyTokenService.verifyPremiumUser(tokenDTO));
    }
    @PostMapping("/pro")
    public ResponseEntity<?> verifyProUser(@RequestBody TokenDTO tokenDTO) {
        return ResponseEntity.ok().body(this.verifyTokenService.verifyProUser(tokenDTO));
    }
    @PostMapping("/normal")
    public ResponseEntity<?> verifyNormalUser(@RequestBody TokenDTO tokenDTO) {
        return ResponseEntity.ok().body(this.verifyTokenService.verifyNormalUser(tokenDTO));
    }
    @PostMapping("/staff")
    public ResponseEntity<?> verifyStaff(@RequestBody TokenDTO tokenDTO) {
        return ResponseEntity.ok().body(this.verifyTokenService.verifyStaff(tokenDTO));
    }
}
