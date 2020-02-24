package com.KOndziu.usercarservice.controllers;


import com.KOndziu.usercarservice.exceptions.UserAlreadyExists;
import com.KOndziu.usercarservice.exceptions.UserNotFoundException;
import com.KOndziu.usercarservice.modules.User;
import com.KOndziu.usercarservice.modules.UserPreference;
import com.KOndziu.usercarservice.payload.UserDTO;
import com.KOndziu.usercarservice.payload.UserPreferenceDTO;
import com.KOndziu.usercarservice.repos.UserPreferencesRepository;
import com.KOndziu.usercarservice.repos.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Supplier;

@RefreshScope
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private Supplier<User> emptyUserSupp = () -> new User("not found", "not found");

    @Value("${my.app}")
    private String app;
    @Value("${market-service.port}")
    private String marketServicePort;

    public UserController(UserRepository userRepository, UserPreferencesRepository userPreferencesRepository) {
        this.userRepository = userRepository;
        this.userPreferencesRepository = userPreferencesRepository;
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserDTO userDTO=UserDTO.getDTO(user,marketServicePort);
        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping("/find/{name}/{surname}")
    public ResponseEntity<UserDTO> findUser(@PathVariable String name, @PathVariable String surname) {
        User user = userRepository.findByNameAndAndSecondName(name, surname).orElseThrow(()->new UserNotFoundException(name,surname));

        UserDTO userDTO=UserDTO.getDTO(user,marketServicePort);
        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDTO user1) {
        Optional<User> userOptional = userRepository.findByNameAndAndSecondName(user1.getName(), user1.getSecondName());
        if (!userOptional.isPresent()) { //if no such user
            User user = userRepository.save(new User(user1.getName(), user1.getSecondName()));
            UserDTO userDTO=UserDTO.getDTO(user,marketServicePort);
            return ResponseEntity.ok().body(userDTO);
        }
        //if user already exists
        else {
            throw new UserAlreadyExists(userOptional.get());
           // return ResponseEntity.badRequest().body("User already exists");
        }
    }
    @PostMapping("/preferences/add")
    public ResponseEntity<String> addUserPreference(@RequestBody UserPreferenceDTO userPreferenceDTO){
        User user=userRepository.findById(userPreferenceDTO.getUserId())
                .orElseThrow(()->new UserNotFoundException(userPreferenceDTO.getUserId()));

        UserPreference userPreference=UserPreferenceDTO.convertFromDTO(userPreferenceDTO,user);
        userPreferencesRepository.save(userPreference);
        return new ResponseEntity<>("preferences updated", HttpStatus.OK);
    }
}
