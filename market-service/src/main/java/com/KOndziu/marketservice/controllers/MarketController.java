package com.KOndziu.marketservice.controllers;


import com.KOndziu.marketservice.modules.MarketCar;
import com.KOndziu.marketservice.modules.MarketCarDto;

import com.KOndziu.marketservice.modules.MarketCarPic;
import com.KOndziu.marketservice.repositories.MarketCarPicRepo;
import com.KOndziu.marketservice.repositories.MarketCarRepo;
import com.KOndziu.marketservice.services.MarketCarPicService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Autowired
    public MarketController(MarketCarRepo marketCarRepo, MarketCarPicRepo marketCarPicRepo, MarketCarPicService marketCarPicService) {
        this.marketCarRepo = marketCarRepo;
        this.marketCarPicRepo = marketCarPicRepo;
        this.marketCarPicService = marketCarPicService;
    }
    @GetMapping("/{announcementId}")
    public MarketCarDto getMarketCar(HttpServletRequest request,  @PathVariable Integer announcementId){
        Optional<MarketCar> marketCarOptional=marketCarRepo.findById(announcementId);
        MarketCar marketCar=marketCarOptional.get();
        Set<MarketCarPic> marketCarPics=marketCar.getMarketCarPics();
        //List<MarketCarPic> marketCarPics=marketCarPicRepo.findAllByAnnouncementId(announcementId);
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
    public String addMarketCar(String marketCarDtoString, @RequestParam("image") MultipartFile file) throws JsonProcessingException {

        MarketCarDto marketCarDto=new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false)
                .readValue(marketCarDtoString,MarketCarDto.class);
        //Convert DTO to normal entity
        MarketCar marketCar=getMarketCar(marketCarDto);
        //save marketCar in order to get announcementID
        MarketCar savedMarketCar=marketCarRepo.save(marketCar);
        System.out.println(savedMarketCar);
        //store Image and get it back
        //NOTE photoId=0 bcs it will be set by db
        MarketCarPic marketCarPic=marketCarPicService.storeMarketCarPic(file,0,savedMarketCar.getAnnoId(),savedMarketCar);
        //add MarketCarPic to set of pics
        savedMarketCar.addMarketCarPic(marketCarPic);
        //update savedMarketPic(set pic)
        marketCarRepo.save(savedMarketCar);

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



    }

    private MarketCarDto getMarketCarDto(MarketCar marketCar,Set<String> marketCarPicsURLs){
        return  MarketCarDto.builder()
                .annoId(marketCar.getAnnoId())
                .carType(marketCar.getCarType())
                .userId(marketCar.getUserId())
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
                .userId(marketCarDto.getUserId())
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
