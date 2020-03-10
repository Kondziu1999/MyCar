package com.KOndziu.notificationservice.controllers;

import com.KOndziu.notificationservice.dto.MarketCarDto;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.CarInfo;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.CarsWrapper;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.ItemsWrapper;
import com.KOndziu.notificationservice.dto.UserPreferenceDTO;
import com.KOndziu.notificationservice.dto.UserTrackingOffersWrapper;
import com.KOndziu.notificationservice.modules.UserPreference;
import com.KOndziu.notificationservice.modules.UserTrackingOffers;
import com.KOndziu.notificationservice.repositories.UserTrackingOffersRepository;
import com.KOndziu.notificationservice.services.ComparingService;
import com.KOndziu.notificationservice.services.NotificationService;
import com.KOndziu.notificationservice.services.UserPreferenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    UserPreferenceService userPreferenceService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    UserTrackingOffersRepository userTrackingOffersRepository;
    @Autowired
    ComparingService comparingService;

    private RestTemplate restTemplate=new RestTemplate();

    private final ObjectMapper objectMapper=new ObjectMapper();

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
    @GetMapping("/news")
    public ItemsWrapper checkForNews() throws JsonProcessingException {
        List<UserPreference> userPreferences=userPreferenceService.findAll();
        UserPreferenceDTO dto=UserPreferenceDTO.convertToDTO(userPreferences.get(0));
        //HttpEntity<String> request=new HttpEntity<>(objectMapper.writeValueAsString(dto));
        ResponseEntity<ItemsWrapper> wrapper=
                restTemplate.postForEntity("http://localhost:8000/offers",dto,ItemsWrapper.class);
        if(wrapper.hasBody()) {
            List<String> urls=notificationService.getOfferUrlSuffix(wrapper.getBody());
            String msg=notificationService.prepareMsgForUser(urls);
            notificationService.sendMail("susmekk@gmail.com","auta",msg,true);
        }


        //TODO add sending request to research service in order to get info if the new car meeting the requirements
        // of userPreference has occurred
        return wrapper.getBody();
    }
    @GetMapping("/news2")
    public ItemsWrapper checkForNewsTest() throws JsonProcessingException {
        List<UserPreference> userPreferences=userPreferenceService.findAll();
        //convert to dto in order to send it in body of request to allegro
        List<UserPreferenceDTO> userPreferenceDTOS=userPreferences.stream()
                .map(UserPreferenceDTO::convertToDTO)
                .collect(Collectors.toList());

        List<UserPreferenceDTO> usersToUpdate=new LinkedList<>();
        userPreferenceDTOS.stream().forEach(userPreferenceDTO -> {
            //fetch tracked offers from db
            List<UserTrackingOffers> userTrackingOffers;
            UserTrackingOffersWrapper trackingOffersWrapper=
                    restTemplate.getForObject("http://localhost:8080/users/trackingOffers/"+userPreferenceDTO.getUserId(),
                            UserTrackingOffersWrapper.class);
            //check for offers
            ResponseEntity<ItemsWrapper> wrapper=
                    restTemplate.postForEntity("http://localhost:8000/offers",userPreferenceDTO,ItemsWrapper.class);


        });
        UserPreferenceDTO dto=UserPreferenceDTO.convertToDTO(userPreferences.get(0));
        ResponseEntity<ItemsWrapper> wrapper=
                restTemplate.postForEntity("http://localhost:8000/offers",dto,ItemsWrapper.class);
        if(wrapper.hasBody()) {
            List<String> urls=notificationService.getOfferUrlSuffix(wrapper.getBody());
            String msg=notificationService.prepareMsgForUser(urls);
            notificationService.sendMail("susmekk@gmail.com","auta",msg,true);
        }



        //TODO add sending request to research service in order to get info if the new car meeting the requirements
        // of userPreference has occurred
        return wrapper.getBody();
    }
}
