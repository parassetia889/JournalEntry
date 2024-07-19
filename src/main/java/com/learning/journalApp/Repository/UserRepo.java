package com.learning.journalApp.Repository;

import com.learning.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User, ObjectId> {
    User findByUserName(String userName);
}
