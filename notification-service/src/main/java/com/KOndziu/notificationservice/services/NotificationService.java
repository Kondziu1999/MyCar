package com.KOndziu.notificationservice.services;

import com.KOndziu.notificationservice.clients.UserClient;
import com.KOndziu.notificationservice.dto.MarketCarDto;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.CarInfo;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.CarsWrapper;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.ItemsWrapper;
import com.KOndziu.notificationservice.dto.UserDTO;
import com.KOndziu.notificationservice.modules.UserPreference;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;


@Service
@NoArgsConstructor
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
    //TODO test it
    public String prepareMsgForUser(List<String>urls){
        String prefix="https://allegro.pl/ogloszenie/";
        StringBuilder builder=new StringBuilder();
        builder.append("<p>New Car suitable to your preferences has just been advertised</p><br>");
        builder.append("<p>Check it out here:</p><<br>");
        for(String url:urls){
            builder.append("<a href=");
            builder.append("\"");
            builder.append(prefix+url+"\"");
            builder.append(">Market</a>");
        }
        return builder.toString();
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
    public List<String> parseOfferInfoToURL(String word){
        String uri=word.toLowerCase();
        String delimiters="\\s+|,\\s*|\\.\\s*|\\+\\s*|-\\s*";
        //String pattern="[a-z]{1,}|[1-9]{1,}";
        String [] tokens=uri.split(delimiters);
        List<String> tokensList=Arrays.asList(tokens);

        return tokensList.stream().map(StringUtils::stripAccents).collect(Collectors.toList());
    }
    //TODO test it
    public List<String> getOfferURL(ItemsWrapper itemsWrapper){
        StringBuilder builder=new StringBuilder();
        List<CarInfo> carInfosPromoted=itemsWrapper.getItems().getPromoted();
        List<CarInfo> carInfos=itemsWrapper.getItems().getRegular();

        carInfosPromoted.addAll(carInfos); //add another infos

        List<String> urls=carInfosPromoted.stream().map(carInfo -> {
            builder.setLength(0); //clear builder every time
            List<String> tokens=parseOfferInfoToURL(carInfo.getName());
            for(String token:tokens){
                builder.append(token);
                builder.append("-");
            }
            builder.append(carInfo.getId());
            return builder.toString();
        }).collect(Collectors.toList());

        return urls;
    }
}
