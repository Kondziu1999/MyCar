package com.KOndziu.notificationservice.controllers;

import com.KOndziu.notificationservice.dto.MarketCarDto;
import com.KOndziu.notificationservice.modules.UserPreference;
import com.KOndziu.notificationservice.services.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/notification")
public class NotificationController {

    @Autowired
    UserPreferenceService userPreferenceService;

    @PostMapping("/send")
    public ResponseEntity<String> getAddedCarInfo(@RequestBody MarketCarDto marketCarDto){

        String params=userPreferenceService.compileSearchString(marketCarDto);

        List<UserPreference > userPreferences=userPreferenceService.findPreferences(params);

        return new ResponseEntity<>("new market car recived", HttpStatus.OK);
    }
}
