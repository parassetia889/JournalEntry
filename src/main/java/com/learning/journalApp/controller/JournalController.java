package com.learning.journalApp.controller;

import com.learning.journalApp.entity.JournalEntity;
import com.learning.journalApp.entity.User;
import com.learning.journalApp.service.JournalEntryService;
import com.learning.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {

        userName = userName.toLowerCase(Locale.ROOT);
        User user = userService.findByUserName(userName);

        List<JournalEntity> all = user.getJournalEntities();
        if(all != null && !all.isEmpty())
            return new ResponseEntity<>(all, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntity> createEntity(@RequestBody JournalEntity entry, @PathVariable String userName) {
        try{
            userName = userName.toLowerCase(Locale.ROOT);
            journalEntryService.saveEntry(entry, userName);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntity> getJournalById(@PathVariable ObjectId myId) {
        Optional<JournalEntity> journalEntity = journalEntryService.findById(myId);

        if(journalEntity.isPresent()){
            return new ResponseEntity<>(journalEntity.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId myId, @PathVariable String userName) {
        journalEntryService.deleteById(myId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{userName}/{myId}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId myId, @RequestBody JournalEntity newEntry, @PathVariable String userName ) {
        JournalEntity oldEntry = journalEntryService.findById(myId).orElse(null);

        if(oldEntry != null){
            oldEntry.setTitle(!newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(newEntry.getContent()!= null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());

            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
