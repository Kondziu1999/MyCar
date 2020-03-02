package com.KOndziu.researchserver.connectors;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AllegroConnector {



    @GetMapping("/sample")
    public String get(){
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhbGxlZ3JvX2FwaSJdLCJleHAiOjE1ODMxOTk0MjEsImp0aSI6IjcyNDU0ZmU5LTk4NDMtNGY3ZC1iMjI2LTQzZGY1OTA1MWU0MiIsImNsaWVudF9pZCI6IjMxZTJmOWM0MThhOTQzZjhiYzg2NzgyY2YyYWFlYzY0In0.dGccMScUkfXnqPJG-l43dHX1e1zvdb3oobIdJtKgyzal5iZL3glAY22YWwgToVMvqijHJTwFxO-0rvLZgEc6OTjX62Ws1SMZ5QtC2_nLhb6ZDH0wk0T7vE7SDFjcFhr3WSZYw_c2c1EOns-zG9kM0Wg7x0dH6Qni7dvTD33S9YK3daBUjI3yFCKX_Hcxh_sXahwrfpur1CHp1sEQsGEddIOTVfeZBZSL9DKhqHOw5NT0D9Dtxb-0xn4BhfTeGCVbMKfUB5hX2gzUke3fshApymAIHLwCxMpE1GKe1qBtxM7gOcaOMX4lmpXkVuh5dRnu0Wc4L5-rb9ecKDKz-vcy7hhsBqaw8wF0tQzNHhIMs4-zlKGo9Op05M0UJnGKLhtbPuSVpleAfWY6W6yRdDKNbvA72omJWwMp5prV2iue2rFqPHFWxTbBc1wiCaIzkHp3Lj0LAKZcQB-APOZnmeFAaR7cbQU12WHN3lbN6ZPQzElKZOFlcAbB1wuigXQ0SjIQd_rg5I9xdYXpClYvalHdIoFQMvWGS2jmfYj40je7YlIIqjKHLIVXpwm_PrXSj8psCCYlbGN33gqx-_M8nXuqTJPE6umWTWbuK696Azext931v6zywLcb0NB7blBQTf0zbhCrTfw9FKwDGHZqaYh5WR6eUUNJAfBlZ2_p-hE2MDQ");
        httpHeaders.add("Accept","application/vnd.allegro.public.v1+json");

        HttpEntity entity=new HttpEntity(httpHeaders);
        ResponseEntity<String> response= restTemplate.exchange("https://api.allegro.pl.allegrosandbox.pl/sale/categories",HttpMethod.GET,entity,String.class);
        return response.getBody();
    }

    @GetMapping("/oferts")
    public ResponseEntity<String> getOffers(){
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Authorization","Bearer "); //tu token
        httpHeaders.add("Accept","application/vnd.allegro.public.v1+json");

        HttpEntity entity=new HttpEntity(httpHeaders);
        ResponseEntity<String> response= restTemplate.exchange("https://api.allegro.pl.allegrosandbox.pl/sale/categories",HttpMethod.GET,entity,String.class);
        return response;
    }
    @GetMapping("/getModelCategories")
    public void getModelsCategories(){

    }


}
