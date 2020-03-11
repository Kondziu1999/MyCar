package com.KOndziu.notificationservice.services;


import com.KOndziu.notificationservice.dto.ResearchServicePayload.CarInfo;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.ItemsWrapper;
import com.KOndziu.notificationservice.dto.UserTrackingOffersWrapper;
import com.KOndziu.notificationservice.modules.UserTrackingOffers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ComparingService {

    public List<UserTrackingOffers> getUntrackedOffers(UserTrackingOffersWrapper trackingOffersWrapper, ResponseEntity<ItemsWrapper> wrapper) {
        //return list
        List<UserTrackingOffers> untrackedOffers = new LinkedList<>();

        //check if there is any tracking offer
        if (!trackingOffersWrapper.getUserTrackingOffers().isEmpty()) {
            //get and sort user tracking offers
            List<UserTrackingOffers> userTrackingOffers = trackingOffersWrapper.getUserTrackingOffers()
                    .stream().sorted(Comparator.comparing(UserTrackingOffers::getOfferId))
                    .collect(Collectors.toList());
            //if wrapper has status 200 (has body)
            if (wrapper.getStatusCode()== HttpStatus.OK) {
                //get car infos from allegro
                List<CarInfo> carInfosAllegro = Stream.concat(wrapper.getBody().getItems().getPromoted().stream(),
                        wrapper.getBody().getItems().getRegular().stream())
                        .collect(Collectors.toList());
                //TODO ktorej z allegro nie ma w offers z bd

                carInfosAllegro.stream().forEach(allegroOffer->{
                    //if allegro offer isn't in tracking offer add to untracked offers
                    if(!ifAllegroOfferIsTracked(userTrackingOffers,allegroOffer)){
                        untrackedOffers.add(new UserTrackingOffers(allegroOffer.getId(),allegroOffer.getName()));
                    }

                });

                return untrackedOffers;
            }
            //if there is nothing in allegroOffers return empty untrackedOffers
            else { return untrackedOffers; }

        }
        //if there is no tracking offers
        else {
            //if wrapper has body convert body to tracking offer objects
            if(wrapper.getStatusCode()==HttpStatus.OK){
                wrapper.getBody().getItems().getPromoted().stream().forEach(carInfo -> {
                    untrackedOffers.add(new UserTrackingOffers(carInfo.getId(),carInfo.getName()));
                });
                wrapper.getBody().getItems().getRegular().stream().forEach(carInfo -> {
                    untrackedOffers.add(new UserTrackingOffers(carInfo.getId(),carInfo.getName()));
                });
                return untrackedOffers;
            }
            //return empty list
            else { return untrackedOffers; }
        }


    }
    public boolean ifAllegroOfferIsTracked(List<UserTrackingOffers> userTrackingOffers,CarInfo allegroCarInfo){

        Optional<UserTrackingOffers> offersOptional=userTrackingOffers.stream()
                .filter(offer->offer.getOfferId().equals(allegroCarInfo.getId()))
                .findFirst();


        //if offer is present return false
        return offersOptional.isPresent();
    }


}


