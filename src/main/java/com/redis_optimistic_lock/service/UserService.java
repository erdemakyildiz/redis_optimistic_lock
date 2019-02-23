package com.redis_optimistic_lock.service;

import com.redis_optimistic_lock.entity.User;
import com.redis_optimistic_lock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Erdem Akyıldız on 22.02.2019.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean save(User user){
        final User dbUser = getUser(user.getId());

        if (dbUser == null){
            user.setUuid(UUID.randomUUID().toString());
        }else{
            if (!user.getUuid().equalsIgnoreCase(dbUser.getUuid())){
                return false;
            }else {
                user.setUuid(UUID.randomUUID().toString());
            }
        }

        userRepository.save(user);
        return true;
    }

    public Iterable<User> getAll(){
        return userRepository.findAll();
    }

    public User getUser(long id){
        final Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

}
