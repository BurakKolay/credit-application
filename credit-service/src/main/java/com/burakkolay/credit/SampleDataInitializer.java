package com.burakkolay.credit;


import com.burakkolay.credit.model.entity.Role;
import com.burakkolay.credit.model.entity.User;
import com.burakkolay.credit.repository.UserRepository;
import com.burakkolay.credit.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SampleDataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    private final UserService userService;


    @Override
    public void run(ApplicationArguments args) {

        // Creating a sample Admin USER
        User adminUser = new User(null,"admin",null,null, "$2a$12$dTXyPaKwWAaM5mClRz1F...WxkuYKjcXgSHj0QJCJY3aXvTknI2HC", Role.ROLE_ADMIN);
        if (!userRepository.existsByUserName(adminUser.getUserName())) {
            userService.signUp(adminUser);
        }

    }
}