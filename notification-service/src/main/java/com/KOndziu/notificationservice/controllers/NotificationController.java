package com.KOndziu.notificationservice.controllers;

import com.KOndziu.notificationservice.dto.MarketCarDto;
import com.KOndziu.notificationservice.modules.UserPreference;
import com.KOndziu.notificationservice.services.NotificationService;
import com.KOndziu.notificationservice.services.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    UserPreferenceService userPreferenceService;
    @Autowired
    NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> getAddedCarInfo(@RequestBody MarketCarDto marketCarDto){

        String params=userPreferenceService.compileSearchString(marketCarDto);

        List<UserPreference> userPreferences=userPreferenceService.findPreferences(params);
        userPreferences.stream().forEach(userPreference -> {
            String msg=notificationService.prepareMsgForUser(userPreference,marketCarDto);
            //notificationService.sendMail("susmekk@gmail.com","nowe auto",msg,true);
        });

        return new ResponseEntity<>(String.valueOf("new market car recived"), HttpStatus.OK);
    }
    @PostMapping("/send")
    public ResponseEntity<String> checkForNews(){
        List<UserPreference> userPreferences=userPreferenceService.findAll();


        //TODO add sending request to research service in order to get info if the new car meeting the requirements
        // of userPreference has occurred
        return null;
    }
}
