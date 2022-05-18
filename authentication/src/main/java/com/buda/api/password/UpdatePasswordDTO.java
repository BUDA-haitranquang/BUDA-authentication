package com.buda.api.password;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDTO {
    private String currentPassword;
    @NotNull
    private String newPassword;

    
}
