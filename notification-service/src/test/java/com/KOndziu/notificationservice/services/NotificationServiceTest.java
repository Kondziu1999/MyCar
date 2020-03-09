package com.KOndziu.notificationservice.services;

import com.KOndziu.notificationservice.clients.UserClient;
import com.KOndziu.notificationservice.dto.MarketCarDto;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.CarInfo;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.CarsWrapper;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.ItemsWrapper;
import com.KOndziu.notificationservice.dto.UserDTO;
import com.KOndziu.notificationservice.modules.UserPreference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;
import javax.validation.constraints.AssertTrue;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class NotificationServiceTest {


    @Autowired
    UserClient userClient;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    NotificationService notificationService;

    @MockBean
    ItemsWrapper itemsWrapper;

    MarketCarDto marketCarDto;
    UserDTO userDTO;
    UserPreference userPreference;
    @BeforeEach
    public void setUp() {
        marketCarDto = marketCarDto.builder().annoId(1).build();
        userDTO = UserDTO.builder().id(1).build();
        userPreference = UserPreference.builder().userId(1).build();
        javaMailSender=new JavaMailSenderImpl();


    }

    @Test
    public void  should_return_properly_email_message(){
        //given
        Integer id=userDTO.getId();
        //Mockito.when(userClient.getUserById(id)).thenReturn(new ResponseEntity<>(userDTO, HttpStatus.OK));
        //when
        String msg=notificationService.prepareMsgForUser(userPreference,marketCarDto);
        //then
        assertEquals(msg,
                "<p>New Car suitable to your preferences has just been advertised</p><br>"+
                        "<p>Check it out here:</p><<br>"+
                        "<a href="
                        +"\""
                        +"http://localhost:8081/market"+"/"+marketCarDto.getAnnoId()+"\""+">Market</a>"
                );
    }

    @Test
    public void should_return_properly_announcement_url_(){
        //given
        Mockito.when(itemsWrapper.getItems()).thenReturn(getTestItemsWrapper().getItems());
        List<String> urls=notificationService.getOfferUrlSuffix(itemsWrapper);

        //then
        assertEquals(urls.get(0),"promoted-2");
        assertEquals(urls.get(2),"audi-a3-1-6-tdi-lift-bose-gwarancja-przebiegu-ksia-3");
    }
    public ItemsWrapper getTestItemsWrapper(){
        ItemsWrapper itemsWrapper=new ItemsWrapper();
        CarsWrapper carsWrapper=new CarsWrapper();

        CarInfo carInfo1=new CarInfo();
        carInfo1.setId("1");
        carInfo1.setName("test");
        CarInfo carInfo12=new CarInfo();
        carInfo12.setId("3");
        carInfo12.setName("Audi A3 1.6 TDI Lift Bose gwarancja przebiegu ksią");

        CarInfo carInfo2=new CarInfo();
        carInfo2.setName("promoted");
        carInfo2.setId("2");

        carsWrapper.setRegular(Arrays.asList(carInfo1,carInfo12));
        carsWrapper.setPromoted(Arrays.asList(carInfo2));

        itemsWrapper.setItems(carsWrapper);

        return itemsWrapper;
    }


}