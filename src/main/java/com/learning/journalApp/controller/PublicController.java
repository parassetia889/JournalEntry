package com.learning.journalApp.controller;

import com.learning.journalApp.entity.User;
import com.learning.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {


    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }

    @PostMapping("/create-user")
    private ResponseEntity<?> createUser(@RequestBody User user){
        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
