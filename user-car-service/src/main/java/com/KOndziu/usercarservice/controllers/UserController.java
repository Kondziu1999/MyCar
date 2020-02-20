package com.KOndziu.usercarservice.controllers;


import com.KOndziu.usercarservice.modules.User;
import com.KOndziu.usercarservice.payload.UserDTO;
import com.KOndziu.usercarservice.repos.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Supplier;

@RefreshScope
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private Supplier<User> emptyUserSupp = () -> new User("not found", "not found");

    @Value("${my.app}")
    private String app;
    @Value("${market-service.port}")
    private String marketServicePort;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseGet(emptyUserSupp);

        return new UserDTO(user.getId(), user.getName(), user.getSecondName(),
                "http://localhost:8080/users/identities" + user.getId(),
                "http://localhost:8080/cars/" + user.getId(),
                "http://localhost:" + marketServicePort + "/market/" + user.getId()
        );

    }

    @GetMapping("/find/{name}/{surname}")
    public UserDTO findUser(@PathVariable String name, @PathVariable String surname) {
        Optional<User> userOptional = userRepository.findByNameAndAndSecondName(name, surname);
        User user = userOptional.orElseGet(emptyUserSupp);

        return new UserDTO(user.getId(), user.getName(), user.getSecondName(),
                "http://localhost:8080/users/identities" + user.getId(),
                "http://localhost:8080/cars/" + user.getId(),
                "http://localhost:" + marketServicePort + "/market/" + user.getId()
        );
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDTO user1) {
        Optional<User> userOptional = userRepository.findByNameAndAndSecondName(user1.getName(), user1.getSecondName());

        if (!userOptional.isPresent()) {
            User user = userRepository.save(new User(user1.getName(), user1.getSecondName()));

            UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getSecondName(),
                    "http://localhost:8080/users/identities" + user.getId(),
                    "http://localhost:8080/cars/" + user.getId(),
                    "http://localhost:" + marketServicePort + "/market/" + user.getId()
            );
            return ResponseEntity.ok().body(userDTO);
        } else {
            return ResponseEntity.badRequest().body("User already exists");
        }
    }
}
