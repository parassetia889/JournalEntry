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
        userService.saveUser(user );
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

    @Transactional
    public boolean deleteById(ObjectId myId, String userName){
        boolean removed = false;
        try{
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntities().removeIf(x -> x.getId().equals(myId));

            if (removed) {
                userService.saveUser(user);
                journalEntryRepo.deleteById(myId);
            }
            return removed;
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occurred while deleting the entry",e);
        }
    }

//    public List<JournalEntity> findByUserName(String userName){
//
//    }
}
