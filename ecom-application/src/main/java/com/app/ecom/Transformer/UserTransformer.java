package com.app.ecom.Transformer;

import com.app.ecom.DTO.Request.UserRequest;
import com.app.ecom.DTO.Response.AddressResponse;
import com.app.ecom.DTO.Response.UserResponse;
import com.app.ecom.Model.Address;
import com.app.ecom.Model.User;
import org.springframework.stereotype.Component;

@Component
public class UserTransformer {

    // Entity se Response DTO banane ke liye
    public UserResponse toResponse(User user) {
        if (user == null) return null;

        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());

        if (user.getAddress() != null) {
            AddressResponse addrRes = new AddressResponse();
            addrRes.setStreet(user.getAddress().getStreet());
            addrRes.setCity(user.getAddress().getCity());
            addrRes.setState(user.getAddress().getState());
            addrRes.setCountry(user.getAddress().getCountry());
            addrRes.setZipcode(user.getAddress().getZipcode());
            response.setAddress(addrRes);
        }
        return response;
    }

    // Request DTO se Entity update karne ke liye
    public void updateEntityFromRequest(User user, UserRequest request) {
        if (request == null) return;

        user.setFirstName(request.getFirstName()); // Fix: request se data lein
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        if (request.getAddress() != null) {
            Address address = (user.getAddress() == null) ? new Address() : user.getAddress();
            address.setStreet(request.getAddress().getStreet());
            address.setCity(request.getAddress().getCity());
            address.setState(request.getAddress().getState());
            address.setCountry(request.getAddress().getCountry());
            address.setZipcode(request.getAddress().getZipcode());
            user.setAddress(address);
        }
    }
}