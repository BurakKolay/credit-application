package com.burakkolay.credit.services;

import com.burakkolay.credit.model.entity.Role;
import com.burakkolay.credit.model.entity.User;
import com.burakkolay.credit.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService  {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signUp(User user){

        return userRepository.save(user);
    }
}
