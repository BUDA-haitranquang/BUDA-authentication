package com.buda.api.login.social.google;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buda.builder.JwtSimple;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin("*")
@RequestMapping(path = "/api/login/social/google")
public class GoogleLoginController {
    private final GoogleLoginService googleLoginService;
    @Autowired
    public GoogleLoginController(GoogleLoginService googleLoginService) {
        this.googleLoginService = googleLoginService;
    }
    @PostMapping
    public ResponseEntity<?> loginWithGoogle(@RequestBody JwtSimple jwtSimple) throws GeneralSecurityException, IOException{
        GoogleUserPayload googleUserPayload = BudaGoogleTokenVerifier.userCustomPayload(jwtSimple.getToken());
        return ResponseEntity.ok().body(this.googleLoginService.processGoogleUserPostLogin(googleUserPayload));
    }
    @GetMapping(value="/credentials")
    public String getMethodName() {
        return BudaGoogleTokenVerifier.getCredentials();
    }
    
}
