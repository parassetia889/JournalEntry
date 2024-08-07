package com.learning.journalApp.service;

import com.learning.journalApp.Repository.JournalEntryRepo;
import com.learning.journalApp.Repository.UserRepo;
import com.learning.journalApp.entity.JournalEntity;

import com.learning.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveNewUser(User user){
        userRepo.save(user);
    }

    public void saveEntry(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserName(user.getUserName().toLowerCase(Locale.ROOT));
        user.setRoles(Arrays.asList("User"));
        userRepo.save(user);
    }

    public List<User> getAll(){
      return userRepo.findAll();
    }

    public User findByUserName(String userName){
        userName = userName.toLowerCase(Locale.ROOT);
        return userRepo.findByUserName(userName);
    }

    public void deleteById(ObjectId id){
        userRepo.deleteById(id);
    }
}
