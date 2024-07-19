package com.learning.journalApp.service;

import com.learning.journalApp.Repository.JournalEntryRepo;
import com.learning.journalApp.Repository.UserRepo;
import com.learning.journalApp.entity.JournalEntity;
import com.learning.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public void saveEntry(User user){
        user.setUserName(user.getUserName().toLowerCase(Locale.ROOT));
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
