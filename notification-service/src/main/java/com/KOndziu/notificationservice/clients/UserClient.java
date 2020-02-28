package com.KOndziu.notificationservice.clients;


import com.KOndziu.notificationservice.dto.UserDTO;
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
}
