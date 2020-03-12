package com.KOndziu.notificationservice.controllers;

import com.KOndziu.notificationservice.clients.ResearchServerClient;
import com.KOndziu.notificationservice.clients.UserClient;
import com.KOndziu.notificationservice.dto.MarketCarDto;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.CarInfo;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.CarsWrapper;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.ItemsWrapper;
import com.KOndziu.notificationservice.dto.UserIdentitiesDto;
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
import org.springframework.scheduling.annotation.Scheduled;
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
    @Autowired
    UserClient userClient;
    @Autowired
    ResearchServerClient researchServerClient;

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
    public List<UserTrackingOffers> checkForNewsTest() throws JsonProcessingException {
        List<UserTrackingOffers> listForTestPurpose=new LinkedList<>();
        List<UserPreference> userPreferences=userPreferenceService.findAll();
        //convert to dto in order to send it in body of request to allegro
        List<UserPreferenceDTO> userPreferenceDTOS=userPreferences.stream()
                .map(UserPreferenceDTO::convertToDTO)
                .collect(Collectors.toList());

        userPreferenceDTOS.stream().forEach(userPreferenceDTO -> {
            //fetch tracked offers from db
            UserTrackingOffersWrapper trackingOffersWrapper=userClient.getUserTrackingOffers(userPreferenceDTO.getUserId());
            //fetch carInfos from allegro
            ResponseEntity<ItemsWrapper> itemsWrapper=researchServerClient.getOffersForUser(userPreferenceDTO);
            //check untracked offers
            List<UserTrackingOffers> untrackedOffers=comparingService.getUntrackedOffers(trackingOffersWrapper,itemsWrapper);
            untrackedOffers.forEach(offers -> offers.setUserId(userPreferenceDTO.getUserId()));

            //if there is untracked offer
            if(!untrackedOffers.isEmpty()){
                ResponseEntity<UserIdentitiesDto> userIdentitiesDto=userClient
                        .getUserIdentities(userPreferenceDTO.getUserId());
                //if there is such a user
                if(userIdentitiesDto.getStatusCode()==HttpStatus.OK){
                    List<String> urls=notificationService.getOfferUrlSuffix(untrackedOffers);
                    String msg=notificationService.prepareMsgForUser(urls);
                    notificationService.sendMail(userIdentitiesDto.getBody().getEmail(),"auta",msg,true);
                    //TODO delete it
                    untrackedOffers.stream().forEach(offers -> listForTestPurpose.add(offers));

                    userTrackingOffersRepository.saveAll(untrackedOffers);
                }
            }

            //save untrackedOffers into db after sending mail in case of error

        });

        return listForTestPurpose;
    }



}
