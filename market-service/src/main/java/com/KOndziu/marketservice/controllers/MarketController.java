package com.KOndziu.marketservice.controllers;


import com.KOndziu.marketservice.modules.*;

import com.KOndziu.marketservice.repositories.MarketCarPicRepo;
import com.KOndziu.marketservice.repositories.MarketCarRepo;
import com.KOndziu.marketservice.repositories.UserRepository;
import com.KOndziu.marketservice.services.MarketCarPicService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/market")
public class MarketController {
    private final MarketCarRepo marketCarRepo;
    private final MarketCarPicRepo marketCarPicRepo;
    private final MarketCarPicService marketCarPicService;
    private final UserRepository userRepository;

    @Autowired
    public MarketController(MarketCarRepo marketCarRepo, MarketCarPicRepo marketCarPicRepo, MarketCarPicService marketCarPicService, UserRepository userRepository) {
        this.marketCarRepo = marketCarRepo;
        this.marketCarPicRepo = marketCarPicRepo;
        this.marketCarPicService = marketCarPicService;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public List<MarketDto> getMarket(HttpServletRequest request){

        List<MarketCar> marketCars=marketCarRepo.findAll();

        List<MarketCarDto> marketCarDtos=marketCars.stream().map(car ->{
            return getMarketCarDto(car,null);
        }).collect(Collectors.toList());

        return marketCarDtos.stream().map(dto->{
            return MarketDto.builder()
                    .carType(dto.getCarType())
                    .price(dto.getPrice())
                    .state(dto.getState())
                    .announcementId(dto.getAnnoId())
                    .endpoint(request.getScheme() + "://"
                            + request.getServerName()
                            + ":"
                            + request.getServerPort()
                            + "/market/"+dto.getAnnoId())
                    .build();
        }).collect(Collectors.toList());

    }
    @GetMapping("/{announcementId}")
    public MarketCarDto getMarketCar(HttpServletRequest request,  @PathVariable Integer announcementId){
        //TODO add optional handling
        Optional<MarketCar> marketCarOptional=marketCarRepo.findById(announcementId);
        MarketCar marketCar=marketCarOptional.get();

        //convert pictures to URLs
        Set<MarketCarPic> marketCarPics=marketCar.getMarketCarPics();
        Set<String> marketCarPicsURLs=marketCarPics.stream().map(pic ->{
            return request.getScheme() + "://"
                    + request.getServerName()
                    + ":"
                    + request.getServerPort()
                    + "/market/downloadFile/" + pic.getPhotoId();
        }).collect(Collectors.toSet());

        return  getMarketCarDto(marketCar,marketCarPicsURLs);
    }
    @PostMapping("/add")
    public String addMarketCar(String marketCarDtoString, @RequestParam("image") MultipartFile file) throws IOException {
        //map dtoString to object
        MarketCarDto marketCarDto=new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false)
                .readValue(marketCarDtoString,MarketCarDto.class);
        //Convert DTO to normal entity
        MarketCar marketCar=getMarketCar(marketCarDto);
        //TODO   handle this optional
        Optional<User> user=userRepository.findById(marketCarDto.getUserId());

        MarketCarPic marketCarPic=MarketCarPic.builder().marketCar(marketCar).photo(file.getBytes()).build();
        marketCar.addMarketCarPic(marketCarPic);
        marketCar.setUser(user.get());

        marketCarRepo.save(marketCar);

        return "added";
    }

    //controller to download resource
    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId) {
        Optional<MarketCarPic> marketCarPic=marketCarPicRepo.findById(fileId);

        return marketCarPic
                .map(pic -> {
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(String.valueOf((MediaType.IMAGE_JPEG_VALUE))))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pic.getPhotoId()+ "\".jpg")
                            .body(new ByteArrayResource(pic.getPhoto()));
                }).orElse(ResponseEntity.noContent().headers(HttpHeaders.EMPTY).build());

    }
    @GetMapping("/find")
    public List<MarketCarDto> findCarByCriteria(@RequestBody MarketCarDto marketCarDto){

        return null;
    }

    private MarketCarDto getMarketCarDto(MarketCar marketCar,Set<String> marketCarPicsURLs){
        return  MarketCarDto.builder()
                .annoId(marketCar.getAnnoId())
                .carType(marketCar.getCarType())
                //.userId(marketCar.getUserId())
                .color(marketCar.getColor())
                .engForce(marketCar.getEngForce())
                .engSize(marketCar.getEngSize())
                .locality(marketCar.getLocality())
                .mileage(marketCar.getMileage())
                .picURLs(marketCarPicsURLs)
                .origin(marketCar.getOrigin())
                .price(marketCar.getPrice())
                .prodDate(marketCar.getProdDate())
                .province(marketCar.getProvince())
                .seats(marketCar.getSeats())
                .state(marketCar.getState())
                .build();
    }

    //convert dto to entity(without pics)
    private MarketCar getMarketCar(MarketCarDto marketCarDto){
        return MarketCar.builder()
                .annoId(marketCarDto.getAnnoId())
                .carType(marketCarDto.getCarType())
                //.userId(marketCarDto.getUserId())
                .color(marketCarDto.getColor())
                .engForce(marketCarDto.getEngForce())
                .engSize(marketCarDto.getEngSize())
                .locality(marketCarDto.getLocality())
                .mileage(marketCarDto.getMileage())
                .origin(marketCarDto.getOrigin())
                .price(marketCarDto.getPrice())
                .prodDate(marketCarDto.getProdDate())
                .province(marketCarDto.getProvince())
                .seats(marketCarDto.getSeats())
                .state(marketCarDto.getState())
                // here could be pics
                .build();
    }

}
