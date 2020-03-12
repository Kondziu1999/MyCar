package com.KOndziu.notificationservice.clients;


import com.KOndziu.notificationservice.dto.UserDTO;
import com.KOndziu.notificationservice.dto.UserIdentitiesDto;
import com.KOndziu.notificationservice.dto.UserTrackingOffersWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


//TODO change hardcoded service url
@FeignClient( name = "user-car-service")
public interface UserClient {

    @RequestMapping("/users/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId);

    @RequestMapping("/users/trackingOffers/{userId}")
    public UserTrackingOffersWrapper getUserTrackingOffers(@PathVariable Integer userId);

    @RequestMapping("/users/identities/{userId}")
    public  ResponseEntity<UserIdentitiesDto> getUserIdentities(@PathVariable Integer userId);
}
