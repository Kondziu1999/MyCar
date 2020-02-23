package com.KOndziu.marketservice.controllers;


import com.KOndziu.marketservice.dao.IMarketCarDAO;
import com.KOndziu.marketservice.dao.MarketCarDto;
import com.KOndziu.marketservice.dao.MarketDto;
import com.KOndziu.marketservice.modules.*;

import com.KOndziu.marketservice.repositories.MarketCarPicRepo;
import com.KOndziu.marketservice.repositories.MarketCarRepo;
import com.KOndziu.marketservice.repositories.UserRepository;
import com.KOndziu.marketservice.services.MarketCarPicService;
import com.KOndziu.marketservice.specifications.MarketCarSpecification;
import com.KOndziu.marketservice.specifications.SearchCriteria;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/market")
public class MarketController {
    private final MarketCarRepo marketCarRepo;
    private final MarketCarPicRepo marketCarPicRepo;
    private final MarketCarPicService marketCarPicService;
    private final UserRepository userRepository;
    private IMarketCarDAO marketCarDAO; // simple JPA repo is not enough for searching in this particular task

    @Autowired
    public MarketController(MarketCarRepo marketCarRepo, MarketCarPicRepo marketCarPicRepo, MarketCarPicService marketCarPicService, UserRepository userRepository, IMarketCarDAO marketCarDAO) {
        this.marketCarRepo = marketCarRepo;
        this.marketCarPicRepo = marketCarPicRepo;
        this.marketCarPicService = marketCarPicService;
        this.userRepository = userRepository;
        this.marketCarDAO = marketCarDAO;
    }

    @GetMapping("")
    public List<MarketDto> getMarket(HttpServletRequest request){

        List<MarketCar> marketCars=marketCarRepo.findAll();

        List<MarketCarDto> marketCarDtos=marketCars.stream().map(car ->{
            return MarketCarDto.getDTO(car,null);
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

        return  MarketCarDto.getDTO(marketCar,marketCarPicsURLs);
    }
    @PostMapping("/add")
    public String addMarketCar(String marketCarDtoString, @RequestParam("image") MultipartFile file) throws IOException {
        //map dtoString to object
        MarketCarDto marketCarDto=new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false)
                .readValue(marketCarDtoString,MarketCarDto.class);

        //Convert DTO to normal entity
        MarketCar marketCar=MarketCarDto.getMarket(marketCarDto);
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
    public List<MarketCarDto> findCarByCriteria(){
        MarketCarSpecification spec=new MarketCarSpecification(new SearchCriteria("state",":","nowy"));

        List<MarketCar> marketCars=marketCarRepo.findAll(spec);


        return  marketCars.stream()
                .map(car -> MarketCarDto.getDTO(car,null))
                .collect(Collectors.toList());
    }
    @GetMapping("/search")
    public List<MarketCarDto> searchMarketCars(@RequestParam(value = "search",required = false) String search){
        List<SearchCriteria> params=new ArrayList<>();
        if(search!=null){
            Pattern pattern=Pattern.compile( "(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher=pattern.matcher(search + ",");
            //split search params into key operation value
            while (matcher.find()){
                params.add(new SearchCriteria(matcher.group(1)
                        ,matcher.group(2),matcher.group(3)));
            }
        }
        return marketCarDAO.searchMarketCar(params).stream()
                .map(car -> MarketCarDto.getDTO(car,null))
                .collect(Collectors.toList());
    }

    
}
