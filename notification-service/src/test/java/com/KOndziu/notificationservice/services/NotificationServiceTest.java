package com.KOndziu.notificationservice.services;

import com.KOndziu.notificationservice.clients.UserClient;
import com.KOndziu.notificationservice.dto.MarketCarDto;
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
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class NotificationServiceTest {


    @Autowired
    UserClient userClient;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    NotificationService notificationService;

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
}