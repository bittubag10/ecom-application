package com.app.ecom.DTO.Response;

import com.app.ecom.Enum.UserRole;
import lombok.Data;

@Data

public class UserResponse {
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private UserRole role=UserRole.CUSTOMER;

    private AddressResponse address;

}
