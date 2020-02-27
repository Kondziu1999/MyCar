package com.KOndziu.notificationservice.clients;


import com.KOndziu.notificationservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


//TODO change hardcoded service url
@FeignClient(url = "http://localhost:8080/users",value = "http://localhost:8080/users")
public interface UserClient {

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId);
}
