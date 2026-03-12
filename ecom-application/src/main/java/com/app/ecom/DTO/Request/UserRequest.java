package com.app.ecom.DTO.Request;

import com.app.ecom.DTO.Response.AddressResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private AddressResponse address;
}
