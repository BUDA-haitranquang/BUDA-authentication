package com.buda.api.register;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/register")
@CrossOrigin("*")
public class RegisterUserController {
    private final RegisterUserService registerUserService;
    @Autowired
    public RegisterUserController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegister userRegister) {
        this.registerUserService.registerNewUser(userRegister);
        return ResponseEntity.ok().body("Please confirm your email");
    }
}
