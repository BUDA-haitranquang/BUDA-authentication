package com.buda.api.token.refresh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/token/refresh")
@CrossOrigin("*")
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public RefreshTokenController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping
    public ResponseEntity<?> generateNewAccessToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        return ResponseEntity.ok().body(this.refreshTokenService.generateNewToken(refreshTokenDTO.getToken()));
    }
}