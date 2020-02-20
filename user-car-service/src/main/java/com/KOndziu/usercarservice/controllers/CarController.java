package com.KOndziu.usercarservice.controllers;


import com.KOndziu.usercarservice.modules.FollowCar;
import com.KOndziu.usercarservice.payload.FollowCarDto;
import com.KOndziu.usercarservice.modules.TrackCar;
import com.KOndziu.usercarservice.modules.User;
import com.KOndziu.usercarservice.payload.TrackCarDTO;
import com.KOndziu.usercarservice.repos.TrackCarRepository;
import com.KOndziu.usercarservice.repos.UserRepository;
import com.KOndziu.usercarservice.services.FollowCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RefreshScope
@RequestMapping("/cars")
public class CarController {
    //TODO change it when eureka will be introduced
    @Value("${market-service.port}")
    private String marketServicePort;

    private final FollowCarService followCarService;
    private final UserRepository userRepository;
    private final TrackCarRepository trackCarRepository;

    @Autowired
    public CarController(FollowCarService followCarService, UserRepository userRepository, TrackCarRepository trackCarRepository) {
        this.followCarService = followCarService;

        this.userRepository = userRepository;
        this.trackCarRepository = trackCarRepository;
    }
    @PostMapping("/addTrack")
    public String addFollowCar(@RequestParam Integer announcementId,@RequestParam Integer userId){
        TrackCar trackCar=new TrackCar(announcementId);
        Optional<User> userOptional=userRepository.findById(userId);
        Set<TrackCar> trackCars;
        //check if passed user exists
        if(userOptional.isPresent()){
            User user=userOptional.get();
            trackCars=user.getTrackCars();
            //check if user have got such announcement tracked
            boolean contain=
                    trackCars.stream()
                            .anyMatch(trackCar1 -> trackCar1.getAnnouncementId().equals(announcementId));
            if(!contain){
                trackCar.setUser(user);
                trackCarRepository.save(trackCar);
            }
            return (!contain) ? "track added" : "track already present";
        }
        else{
            return "user not found";
        }
    }
    @GetMapping("/track/{userId}")
    public ResponseEntity<Set<TrackCarDTO>> getTrackCars(@PathVariable Integer userId){
        Optional<User> userOptional=userRepository.findById(userId);
        User user;
        if(userOptional.isPresent()){
            user=userOptional.get();

            Set<TrackCarDTO> trackCars=user.getTrackCars().stream()
                    .map(car-> new TrackCarDTO(car.getTrackId(),
                            "http://localhost:"+marketServicePort+"/market/"+car.getAnnouncementId()))
                    .collect(Collectors.toSet());

            return ResponseEntity.ok().body(trackCars);
        }
        else {
            return ResponseEntity.notFound().build();
        }
        // market/{id}

    }


//    @PostMapping("/follow/add")
//    public UploadFileResponse addCar(@RequestParam("image") MultipartFile file,
//                                     @RequestParam("carId") Integer carId,
//                                     @RequestParam("userId") Integer userId,
//                                     @RequestParam("mark") String mark,
//                                     @RequestParam("carType") String carType,
//                                     @RequestParam("color") String color){
//
//        Optional<User> userOptional= userRepository.findById(userId);
//        User user=userOptional.get();
//        FollowCar followCar=FollowCar.builder()
//                .carId(carId)
//                .carType(carType)
//                .color(color)
//                .mark(mark)
//                //.userId(userId)
//                .user(user)
//                .build();
//
//        user.addFollowCar(followCar);
//        followCarService.storeCar(file,followCar);
//        userRepository.save(user);
//
//
//
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/downloadFile/")
//                .path(String.valueOf(followCar.getCarId()))
//                .toUriString();
//
//        return new UploadFileResponse(followCar.getUserId().toString(), fileDownloadUri,
//                file.getContentType(), file.getSize());
//    }
    @GetMapping("/get/{carId}")
    public FollowCarDto getCarById(HttpServletRequest request, @PathVariable Integer carId){

        FollowCar followCar=followCarService.getCarByCarId(carId);
        String downloadUrl=request.getScheme()+"://"
                +request.getServerName()
                +":"
                +request.getServerPort()
                +"/cars/downloadFile/"+carId;

        return FollowCarDto.builder()
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
