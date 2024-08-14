package com.learning.journalApp.controller;

import com.learning.journalApp.entity.JournalEntity;
import com.learning.journalApp.entity.User;
import com.learning.journalApp.service.JournalEntryService;
import com.learning.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userName = userName.toLowerCase(Locale.ROOT);
        User user = userService.findByUserName(userName);

        List<JournalEntity> all = user.getJournalEntities();
        if(all != null && !all.isEmpty())
            return new ResponseEntity<>(all, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<JournalEntity> createEntity(@RequestBody JournalEntity entry) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            userName = userName.toLowerCase(Locale.ROOT);
            journalEntryService.saveEntry(entry, userName);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntity> getJournalById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntity> collect = user.getJournalEntities().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());

        if( !collect.isEmpty()) {
            Optional<JournalEntity> journalEntity = journalEntryService.findById(myId);

            if (journalEntity.isPresent()) {
                return new ResponseEntity<>(journalEntity.get(), HttpStatus.OK);
            }
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean isRemoved = journalEntryService.deleteById(myId, userName);
        if(isRemoved)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId myId, @RequestBody JournalEntity newEntry ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();;
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntity> collect = user.getJournalEntities().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if( !collect.isEmpty()){
            Optional<JournalEntity> journalEntity = journalEntryService.findById(myId);

            if(journalEntity.isPresent()){
                JournalEntity oldEntry = journalEntity.get();
                oldEntry.setTitle(!newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent()!= null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());

                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
