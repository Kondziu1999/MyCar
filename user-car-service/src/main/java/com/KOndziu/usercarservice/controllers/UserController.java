package com.KOndziu.usercarservice.controllers;


import com.KOndziu.usercarservice.modules.User;
import com.KOndziu.usercarservice.repos.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Supplier;

@RefreshScope
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private Supplier<User> emptyUserSupp=()-> new User("not found","not found");

    @Value("${my.app}")
    private String app;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/test")
    public String getTest(){
        return app;
    }
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId){
        Optional<User> userOptional=userRepository.findById(userId);
        return userOptional.orElseGet(emptyUserSupp);
    }
    @GetMapping("/find/{name}/{surname}")
    public User findUser(@PathVariable String name,@PathVariable String surname){
        Optional<User> userOptional=userRepository.findByNameAndAndSecondName(name,surname);

        return userOptional.orElseGet(emptyUserSupp);
    }
    @PostMapping("/add")
    public User addUser(@RequestBody User user){
        User updatedUser=userRepository.save(user);
        return updatedUser;
    }
}