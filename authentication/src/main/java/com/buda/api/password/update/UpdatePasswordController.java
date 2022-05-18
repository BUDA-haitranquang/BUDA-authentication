package com.buda.api.password.update;

import javax.servlet.http.HttpServletRequest;

import com.buda.api.password.UpdatePasswordDTO;
import com.buda.util.RequestResolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/password/update")
@CrossOrigin("*")
public class UpdatePasswordController {
    private final UpdatePasswordService updatePasswordService;
    private final RequestResolver requestResolver;
    @Autowired
    public UpdatePasswordController(UpdatePasswordService updatePasswordService, RequestResolver requestResolver) {
        this.requestResolver = requestResolver;
        this.updatePasswordService = updatePasswordService;
    }
    @PostMapping
    public ResponseEntity<?> updatePassword(HttpServletRequest httpServletRequest, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        Long userID = this.requestResolver.getUserIDGeneral(httpServletRequest);
        this.updatePasswordService.updateUserPassword(userID, updatePasswordDTO);
        return ResponseEntity.ok().body("Update password successfully!");
    }
}
