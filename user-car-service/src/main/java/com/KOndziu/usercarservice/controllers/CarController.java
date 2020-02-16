package com.KOndziu.usercarservice.controllers;


import com.KOndziu.usercarservice.modules.FollowCar;
import com.KOndziu.usercarservice.services.FollowCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RefreshScope
@RequestMapping("/cars")
public class CarController {


    private final FollowCarService followCarService;
    @Autowired
    public CarController(FollowCarService followCarService) {
        this.followCarService = followCarService;
    }

    @PostMapping("/follow/add")
    public ResponseEntity addCar(@RequestParam("image") MultipartFile file,
                                 @RequestParam("carId") Integer carId,
                                 @RequestParam("userId") Integer userId,
                                 @RequestParam("mark") String mark,
                                 @RequestParam("carType") String carType,
                                 @RequestParam("color") String color){

        FollowCar followCar=FollowCar.builder()
                .carId(carId)
                .carType(carType)
                .color(color)
                .mark(mark)
                .userId(userId)
                .build();
        followCarService.storeCar(file,followCar);

    }


}
