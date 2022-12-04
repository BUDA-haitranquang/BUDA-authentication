package com.buda.api.login.staff;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buda.api.login.StaffLoginDTO;

@RestController
@RequestMapping("api/staff/login")
@CrossOrigin("*")

public class StaffLoginController {
    private final StaffLoginService staffLoginService;
    @Autowired
    public StaffLoginController(StaffLoginService staffLoginService){
        this.staffLoginService = staffLoginService;
    }
    @PostMapping
    public ResponseEntity<?> correctLogin(@RequestBody @Valid StaffLoginDTO staffLogin)
    {
        String account = staffLogin.getAccount();
        String password = staffLogin.getPassword();
        return ResponseEntity.ok(this.staffLoginService.correctLogin(account, password));
    }
}
