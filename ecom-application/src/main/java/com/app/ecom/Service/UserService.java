package com.app.ecom.Service;

import com.app.ecom.DTO.Request.UserRequest;
import com.app.ecom.DTO.Response.AddressResponse;
import com.app.ecom.DTO.Response.UserResponse;
import com.app.ecom.Model.Address;
import com.app.ecom.Model.User;
import com.app.ecom.Repository.UserRepository;
import com.app.ecom.Transformer.UserTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTransformer userTransformer;
    //private List<User> userList=new ArrayList<>();

    public List<UserResponse> userList(){
        return userRepository.findAll().stream()
                .map(userTransformer::toResponse)
                .collect(Collectors.toList());

    }

    public void addUsers(@RequestBody UserRequest userRequest){
//        userList.add(user);
//        return userList;
        User user=new User();
        //updateuserFromRequest(user,userRequest);
        userTransformer.updateEntityFromRequest(user, userRequest);
        userRepository.save(user);

    }



    public Optional<UserResponse> fetchUser(Long id) {
        return userRepository.findById(id)
                //.map(this::mapToUserResponse);
                .map(userTransformer::toResponse);


    }

    @Transactional
    public boolean updateUser(UserRequest updatedUserRequest, Long id) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    userTransformer.updateEntityFromRequest(existingUser, updatedUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

//    private UserResponse mapToUserResponse(User user){
//        UserResponse response=new UserResponse();
//        response.setId(String.valueOf(user.getId()));
//        response.setFirstName(user.getFirstName());
//        response.setLastName(user.getLastName());
//        response.setEmail(user.getEmail());
//        response.setPhone(user.getPhone());
//        if (user.getAddress() !=null){
//            AddressResponse addressResponse=new AddressResponse();
//            addressResponse.setStreet(user.getAddress().getStreet());
//            addressResponse.setCity(user.getAddress().getCity());
//            addressResponse.setState(user.getAddress().getState());
//            addressResponse.setCountry(user.getAddress().getCountry());
//            addressResponse.setZipcode(user.getAddress().getZipcode());
//            response.setAddress(addressResponse);
//        }
//        return response;
//    }
//
//    private void updateuserFromRequest(User user, UserRequest userRequest) {
//        user.setFirstName(user.getFirstName());
//        user.setLastName(user.getLastName());
//        user.setEmail(user.getEmail());
//        user.setPhone(user.getPhone());
//        if (user.getAddress() != null){
//            Address address=new Address();
//            address.setStreet(userRequest.getAddress().getStreet());
//            address.setCity(userRequest.getAddress().getCity());
//            address.setState(userRequest.getAddress().getState());
//            address.setCountry(userRequest.getAddress().getCountry());
//            address.setZipcode(userRequest.getAddress().getZipcode());
//
//        }
//    }
}
