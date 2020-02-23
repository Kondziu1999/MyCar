package com.KOndziu.usercarservice.controllers;

import com.KOndziu.usercarservice.exceptions.UserNotFoundException;
import com.KOndziu.usercarservice.modules.TrackCar;
import com.KOndziu.usercarservice.modules.User;
import com.KOndziu.usercarservice.payload.UserDTO;
import com.KOndziu.usercarservice.repos.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTestUnit {


    @Spy
    UserController userController;


    @Test
    public void should_properly_convert_user_to_dto() throws Exception{
        //given
        Integer sampleId=99;
        Optional<User> optionalUser=getTestUser(sampleId);
        UserRepository userRepositoryMock=mock(UserRepository.class);

        when(userRepositoryMock.findById(sampleId)).thenReturn(optionalUser);
        //when
        ResponseEntity<UserDTO> userResponseEntity=userController.getUserById(sampleId);

        //then
        UserDTO user=userResponseEntity.getBody();
        assertEquals(user.getUserTrackCarsURL(),"http://localhost:8081/market/"+optionalUser.get().getId());
    }




    public Optional<User> getTestUser(Integer sampleId){
        TrackCar trackCar=new TrackCar();
        trackCar.setAnnouncementId(666);
        trackCar.setTrackId(666);
        Set<TrackCar> trackCarSet=new HashSet<>();
        trackCarSet.add(trackCar);
        User user=new User("Konrad","Podgóski");
        user.addTrackCar(trackCar);
        return Optional.ofNullable(user);

    }
}