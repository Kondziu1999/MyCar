package com.KOndziu.notificationservice.clients;


import com.KOndziu.notificationservice.dto.ResearchServicePayload.ItemsWrapper;
import com.KOndziu.notificationservice.dto.UserPreferenceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "research-server")
public interface ResearchServerClient {

    @PostMapping("/offers")
    public ResponseEntity<ItemsWrapper> getOffersForUser(@RequestBody UserPreferenceDTO userPreferenceDTO);
}
