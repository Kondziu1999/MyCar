package com.KOndziu.usercarservice.payload;

import com.KOndziu.usercarservice.modules.TrackCar;
import com.KOndziu.usercarservice.modules.User;
import com.KOndziu.usercarservice.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class UserDTOTest {

    @Spy
    private UserDTO userDTO;

    @Test
    public void should_properly_convert_user_to_dto() throws Exception{
        //given
        Integer sampleId=99;
        String samplePort="8081";
        Optional<User> optionalUser=getTestUser(sampleId);

        //when
        UserDTO userDTO=UserDTO.getDTO(optionalUser.get(),samplePort);

        //then
        assertEquals(userDTO.getUserTrackCarsURL(),"http://localhost:8081/market/"+optionalUser.get().getId());
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