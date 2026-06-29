package com.sari.system.dto;

import com.sari.system.domain.BusinessRole;
import lombok.Data;

@Data
public class RegisterRequest {


    private String firstName;

    private String lastName;

    private String professionalTitle;

    private String email;

    private String password;

    private BusinessRole businessRole;

}
