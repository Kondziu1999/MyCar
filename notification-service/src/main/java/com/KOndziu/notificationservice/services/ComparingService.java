package com.KOndziu.notificationservice.services;


import com.KOndziu.notificationservice.dto.ResearchServicePayload.CarInfo;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.ItemsWrapper;
import com.KOndziu.notificationservice.dto.UserTrackingOffersWrapper;
import com.KOndziu.notificationservice.modules.UserTrackingOffers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ComparingService {

    public List<UserTrackingOffers> getUntrackedOffers(UserTrackingOffersWrapper trackingOffersWrapper, ResponseEntity<ItemsWrapper> wrapper) {
        List<UserTrackingOffers> untrackedOffers = new LinkedList<>();

        //check if there is any tracking offer
        if (trackingOffersWrapper.userTrackingOffers != null) {
            //get and sort offers
            List<UserTrackingOffers> userTrackingOffers = trackingOffersWrapper.getUserTrackingOffers()
                    .stream().sorted(Comparator.comparing(UserTrackingOffers::getOfferId))
                    .collect(Collectors.toList());
            //if wrapper has body compare to it
            if (wrapper.hasBody()) {
                //get ar infos
                List<CarInfo> carInfos = Stream.concat(wrapper.getBody().getItems().getPromoted().stream(),
                        wrapper.getBody().getItems().getRegular().stream())
                        .collect(Collectors.toList());
                //TODO ktorej z allegro nie ma w offers z bd
                //check which car info are not present in offer
//                return userTrackingOffers.stream()
//                        .filter(offer -> !ifSetContain(offer,carInfos))
//                        .collect(Collectors.toList());
//
                userTrackingOffers.stream().forEach(offers -> {
                    //get info which are not in offers
                    Optional<CarInfo> carInfo=ifSetContain(offers,carInfos);
                    if(carInfo.isPresent()){
                        untrackedOffers.add(new UserTrackingOffers(carInfo.get().getId(),carInfo.get().getName()));
                    }
                });

                return untrackedOffers;
            }
            //if there is no body return empty list
            else { return untrackedOffers; }

        }
        //if there is no tracking offers
        else {
            //if wrapper has body convert body to tracking offer objects
            if(wrapper.hasBody()){
                wrapper.getBody().getItems().getPromoted().stream().forEach(carInfo -> {
                    untrackedOffers.add(new UserTrackingOffers(carInfo.getId(),carInfo.getName()));
                });
                return untrackedOffers;
            }
            //return empty list
            else { return untrackedOffers; }
        }


    }

    public Optional<CarInfo> ifSetContain(UserTrackingOffers offer,List<CarInfo> carInfos){

        Optional<CarInfo> optionalCarInfo=carInfos.stream()
                .filter(carInfo ->carInfo.getId().equals(offer.getOfferId()))
                .findFirst();

        return optionalCarInfo;
    }
}


