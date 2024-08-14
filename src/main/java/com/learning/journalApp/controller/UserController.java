package com.learning.journalApp.controller;

import com.learning.journalApp.entity.User;
import com.learning.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUserName(userName);

        if(userInDb != null){
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            userService.saveNewUser(user);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PostMapping
    public void createUser(@RequestBody User user){
        userService.saveNewUser(user);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUserById(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
