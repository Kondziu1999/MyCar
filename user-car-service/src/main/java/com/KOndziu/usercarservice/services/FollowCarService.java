package com.KOndziu.usercarservice.services;

import com.KOndziu.usercarservice.modules.FollowCar;
import com.KOndziu.usercarservice.repos.FollowCarRepository;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Supplier;

@Service
@Log
public class FollowCarService {

    private final FollowCarRepository followCarRepository;
    private Supplier<FollowCar> fallbackCarSupp=() -> FollowCar.builder().carId(0).build();

    public FollowCarService(FollowCarRepository followCarRepository) {
        this.followCarRepository = followCarRepository;
    }

    public FollowCar storeCar(MultipartFile carImage, FollowCar followCar)  {
        String fileName = StringUtils.cleanPath(carImage.getOriginalFilename());

        try {
            if(fileName.contains("..")){
                throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
            }
            FollowCar followCarWithImage=FollowCar.builder()
                    .carId(followCar.getCarId())
                    .carType(followCar.getCarType())
                    .color(followCar.getColor())
                    .mark(followCar.getColor())
                    .userId(followCar.getUserId())
                    .image(carImage.getBytes())
                    .build();

            return followCarRepository.save(followCarWithImage);

        } catch (Exception e) {
            System.out.println("log = " + log);
            return fallbackCarSupp.get();
        }
    }

    public FollowCar getCarByCarId(Integer carId){
        return followCarRepository.findById(carId)
                .orElseGet(fallbackCarSupp);
    }
}


