package com.KOndziu.usercarservice.controllers;


import com.KOndziu.usercarservice.exceptions.UserAlreadyExists;
import com.KOndziu.usercarservice.exceptions.UserNotFoundException;
import com.KOndziu.usercarservice.modules.User;
import com.KOndziu.usercarservice.modules.UserIdentities;
import com.KOndziu.usercarservice.modules.UserPreference;
import com.KOndziu.usercarservice.modules.UserTrackingOffers;
import com.KOndziu.usercarservice.payload.*;
import com.KOndziu.usercarservice.repos.UserIdentitiesRepository;
import com.KOndziu.usercarservice.repos.UserPreferencesRepository;
import com.KOndziu.usercarservice.repos.UserRepository;
import com.KOndziu.usercarservice.repos.UserTrackingOffersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RefreshScope
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final UserIdentitiesRepository userIdentitiesRepository;
    private Supplier<User> emptyUserSupp = () -> new User("not found", "not found");
    private final UserTrackingOffersRepository userTrackingOffersRepository;

    @Value("${my.app}")
    private String app;
    @Value("${market-service.port}")
    private String marketServicePort;

    public UserController(UserRepository userRepository, UserPreferencesRepository userPreferencesRepository, UserIdentitiesRepository userIdentitiesRepository, UserTrackingOffersRepository userTrackingOffersRepository) {
        this.userRepository = userRepository;
        this.userPreferencesRepository = userPreferencesRepository;
        this.userIdentitiesRepository = userIdentitiesRepository;
        this.userTrackingOffersRepository = userTrackingOffersRepository;
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

    @PostMapping("/identities/add")
    public ResponseEntity<String> addUserIdentities(@RequestBody UserIdentitiesDto userIdentitiesDto){
        User user=userRepository.findById(userIdentitiesDto.getUserId())
                .orElseThrow(()->new UserNotFoundException(userIdentitiesDto.getUserId()));

        UserIdentities userIdentities=new UserIdentities();
        userIdentities.setUser(user);
        userIdentities.setEmail(userIdentitiesDto.getEmail());
        userIdentities.setHouseNr(userIdentitiesDto.getHouseNr());
        userIdentities.setLocality(userIdentitiesDto.getLocality());
        userIdentities.setPostalCode(userIdentitiesDto.getPostalCode());
        userIdentitiesRepository.save(userIdentities);

        return new ResponseEntity<>("identities updated", HttpStatus.OK);
    }
    @GetMapping("/identities/{userId}")
    public ResponseEntity<UserIdentitiesDto> getUserIdentities(@PathVariable Integer userId){
        UserIdentities userIdentities=userIdentitiesRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException(userId));

        return new ResponseEntity<>(UserIdentitiesDto.convertToDTO(userIdentities),HttpStatus.OK);
    }
    @GetMapping("/trackingOffers/{userId}")
    public UserTrackingOffersWrapper getUserTrackingOffers(@PathVariable Integer userId){
        //userTrackingOffersRepository
        User user=userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException(userId));

        List<UserTrackingOfferDTO> userTrackingOfferDTOS=user.getUserTrackingOffers().stream()
                .map(UserTrackingOfferDTO::convertToDTO)
                .collect(Collectors.toList());

        UserTrackingOffersWrapper wrapper=new UserTrackingOffersWrapper();
        wrapper.setUserTrackingOffers(userTrackingOfferDTOS);
        return wrapper;
    }
}
