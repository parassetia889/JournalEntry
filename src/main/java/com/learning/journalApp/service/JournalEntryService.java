package com.learning.journalApp.service;

import com.learning.journalApp.Repository.JournalEntryRepo;
import com.learning.journalApp.entity.JournalEntity;
import com.learning.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    UserService userService;

    @Transactional
    public void saveEntry(JournalEntity journalEntity, String userName){
        User user = userService.findByUserName(userName);

        journalEntity.setDate(LocalDateTime.now());
        JournalEntity savedEntity = journalEntryRepo.save(journalEntity);

        user.getJournalEntities().add(savedEntity);
//        user.setUserName(null);
        userService.saveEntry(user );
    }

    public void saveEntry(JournalEntity journalEntity){
        journalEntryRepo.save(journalEntity);
    }

    public List<JournalEntity> getAll(){
      return journalEntryRepo.findAll();
    }

    public Optional<JournalEntity> findById(ObjectId id){
        return journalEntryRepo.findById(id);
    }

    public void deleteById(ObjectId id, String userName){

        User user = userService.findByUserName(userName);
        user.getJournalEntities().removeIf(x -> x.getId().equals(id));
        userService.saveEntry(user);
        journalEntryRepo.deleteById(id);

    }
}
