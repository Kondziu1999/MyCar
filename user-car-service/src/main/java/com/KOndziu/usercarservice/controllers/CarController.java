package com.KOndziu.usercarservice.controllers;


import com.KOndziu.usercarservice.modules.FollowCar;
import com.KOndziu.usercarservice.modules.FollowCarDto;
import com.KOndziu.usercarservice.modules.User;
import com.KOndziu.usercarservice.payload.UploadFileResponse;
import com.KOndziu.usercarservice.repos.UserRepository;
import com.KOndziu.usercarservice.services.FollowCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RefreshScope
@RequestMapping("/cars")
public class CarController {
    private final FollowCarService followCarService;
    private final UserRepository userRepository;
    @Autowired
    public CarController(FollowCarService followCarService, UserRepository userRepository) {
        this.followCarService = followCarService;
        this.userRepository = userRepository;
    }
    @PostMapping("/follow/add")
    public UploadFileResponse addCar(@RequestParam("image") MultipartFile file,
                                     @RequestParam("carId") Integer carId,
                                     @RequestParam("userId") Integer userId,
                                     @RequestParam("mark") String mark,
                                     @RequestParam("carType") String carType,
                                     @RequestParam("color") String color){

        Optional<User> userOptional= userRepository.findById(userId);
        User user=userOptional.get();
        FollowCar followCar=FollowCar.builder()
                .carId(carId)
                .carType(carType)
                .color(color)
                .mark(mark)
                .userId(userId)
                .user(user)
                .build();

        user.addFollowCar(followCar);
        followCarService.storeCar(file,followCar);
        userRepository.save(user);



        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(String.valueOf(followCar.getCarId()))
                .toUriString();

        return new UploadFileResponse(followCar.getUserId().toString(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }
    @GetMapping("/get/{carId}")
    public FollowCarDto getCarById(HttpServletRequest request, @PathVariable Integer carId){

        FollowCar followCar=followCarService.getCarByCarId(carId);
        String downloadUrl=request.getScheme()+"://"
                +request.getServerName()
                +":"
                +request.getServerPort()
                +"/cars/downloadFile/"+carId;

        return FollowCarDto.builder()
                .userId(followCar.getUserId())
                .carId(followCar.getCarId())
                .carType(followCar.getCarType())
                .color(followCar.getColor())
                .mark(followCar.getMark())
                .imageURI(downloadUrl)
                .build();
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId) {
        // Load file from database
        FollowCar dbFile = followCarService.getCarByCarId(fileId);

        //from SO no clue what is that :D
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(String.valueOf((MediaType.IMAGE_JPEG_VALUE))))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getCarId()+ "\".jpg")
                .body(new ByteArrayResource(dbFile.getImage()));
    }


}
