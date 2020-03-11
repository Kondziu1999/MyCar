package com.KOndziu.notificationservice.services;

import com.KOndziu.notificationservice.controllers.NotificationController;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.flogger.Flogger;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OffersUpdater {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    NotificationController notificationController;

    @Scheduled(fixedDelay = 100000,initialDelay = 10000)
    public void offersUpdater() throws JsonProcessingException {
        logger.info("execute offersUpdater");
        notificationController.checkForNewsTest();
    }

}
