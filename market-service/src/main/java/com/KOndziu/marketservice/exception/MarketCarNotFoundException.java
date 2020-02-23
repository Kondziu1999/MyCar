package com.KOndziu.marketservice.exception;


public class MarketCarNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE="Item with given id not found: ";
    public MarketCarNotFoundException(Integer announcementId){
        super(ERROR_MESSAGE+announcementId);
    }
}
