package com.KOndziu.notificationservice.services;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Spy;

import org.mockito.junit.MockitoJUnitRunner;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest2 {

    @Spy
    public NotificationService notificationService=new NotificationService();

    @Test
    public void should_split_name_of_offers_into_tokens(){
        //given
        String testString=getTestString();
        List<String>tokens=notificationService.parseOfferInfoToURL(testString);
        List<String> expectedTokens= Arrays.asList(
                "audi","a3","1","6","tdi","lift","bose","gwarancja","przebiegu","ksia"
        );

        assertEquals(tokens.get(0),expectedTokens.get(0));



    }
    public String getTestString(){
        //return "Audi A3 Serwis AUDI + Automat + Navi + XENON + LED";
        //return "AUDI A3 8V SPORTBACK 1.6 TDI USZKODZONY 2015r";
        return "Audi A3 1.6 TDI Lift Bose gwarancja przebiegu ksią";
    }
}
