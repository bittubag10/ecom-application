package com.app.ecom.Controller;

import com.app.ecom.DTO.Request.UserRequest;
import com.app.ecom.DTO.Response.UserResponse;
import com.app.ecom.Model.User;
import com.app.ecom.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private final UserService userService;
   // private long nextId=1;

    @GetMapping("")
   // @RequestMapping(value = "/api/users",method = RequestMethod.GET)
    public ResponseEntity< List<UserResponse>> getAllUser(){

        return new ResponseEntity<>( userService.userList(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id){
//        User user=userService.fetchUser(id);
//        if (user==null){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(user,HttpStatus.OK);

        return userService.fetchUser(id)
                .map(ResponseEntity:: ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }


    @PostMapping("")
    public ResponseEntity<String> createUsers(@RequestBody UserRequest userRequest){
        //user.setId(nextId++);
       userService.addUsers(userRequest);
       return new ResponseEntity<>("user add Successfully",HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<String>updateUser(@RequestBody UserRequest updateuserRequest,@PathVariable Long id){
        boolean update= userService.updateUser(updateuserRequest,id);
        if (update){
            return ResponseEntity.ok("User update successfully");
        }
        return ResponseEntity.notFound().build();

    }
}
