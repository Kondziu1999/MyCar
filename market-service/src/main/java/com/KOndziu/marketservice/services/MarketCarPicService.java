package com.KOndziu.marketservice.services;

import com.KOndziu.marketservice.modules.MarketCar;
import com.KOndziu.marketservice.modules.MarketCarPic;
import com.KOndziu.marketservice.repositories.MarketCarPicRepo;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Supplier;

@Service
@Log
public class MarketCarPicService {

    private final MarketCarPicRepo marketCarPicRepo;
    private Supplier<MarketCarPic> fallbackCarSupp=() -> MarketCarPic.builder().photoId(0).build();

    public MarketCarPicService(MarketCarPicRepo marketCarPicRepo) {
        this.marketCarPicRepo = marketCarPicRepo;
    }

    public MarketCarPic storeMarketCarPic(MultipartFile image, Integer photoId, Integer announcementId, MarketCar marketCar)  {
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());

        try {
            if(fileName.contains("..")){
                throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
            }
            MarketCarPic marketCarPic=MarketCarPic.builder()
                    .photoId(0)
                    .announcementId(announcementId)
                    .photo(image.getBytes())
                    .marketCar(marketCar)
                    .build();

            return marketCarPicRepo.save(marketCarPic);

        } catch (Exception e) {
            System.out.println("log = " + log);
            return fallbackCarSupp.get();
        }
    }

    public MarketCarPic getMarketCarPicById(Integer photoId){
        return marketCarPicRepo.findById(photoId)
                .orElseGet(fallbackCarSupp);
    }
}


