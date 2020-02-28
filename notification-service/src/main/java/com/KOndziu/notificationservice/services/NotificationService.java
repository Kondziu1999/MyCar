package com.KOndziu.notificationservice.services;

import com.KOndziu.notificationservice.clients.UserClient;
import com.KOndziu.notificationservice.dto.MarketCarDto;
import com.KOndziu.notificationservice.dto.UserDTO;
import com.KOndziu.notificationservice.modules.UserPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class NotificationService {

    private JavaMailSender javaMailSender;
    private UserClient userClient;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender, UserClient userClient) {
        this.javaMailSender = javaMailSender;
        this.userClient = userClient;
    }

    public String prepareMsgForUser(UserPreference userPreference, MarketCarDto marketCarDto){
        //TODO change it
        String marketServiceUrl="http://localhost:8081/market";
        ResponseEntity<UserDTO> userDTO=userClient.getUserById(userPreference.getUserId());
        UserDTO user=userDTO.getBody();

        String message="<p>New Car suitable to your preferences has just been advertised</p><br>"+
                "<p>Check it out here:</p><<br>"+
                "<a href="
                +"\""
                +marketServiceUrl+"/"+marketCarDto.getAnnoId()+"\""+">Market</a>";

        return message;
    }

    public void sendMail(String to, String subject,String text,boolean isHtmlContent) {

        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        try {


            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, isHtmlContent);
            javaMailSender.send(mimeMessage);
        }
        catch (MessagingException e){
            System.out.println(e.getMessage());
        }

    }
}
