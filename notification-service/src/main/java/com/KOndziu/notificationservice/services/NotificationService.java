package com.KOndziu.notificationservice.services;

import com.KOndziu.notificationservice.clients.UserClient;
import com.KOndziu.notificationservice.dto.MarketCarDto;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.CarInfo;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.CarsWrapper;
import com.KOndziu.notificationservice.dto.ResearchServicePayload.ItemsWrapper;
import com.KOndziu.notificationservice.dto.UserDTO;
import com.KOndziu.notificationservice.modules.UserPreference;
import io.micrometer.core.instrument.step.StepCounter;
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
import java.util.stream.Stream;

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
        //prepare msg from local db
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
    //prepare msg from allegro api
    public String prepareMsgForUser(List<String>urls){
        String prefix="https://allegro.pl/ogloszenie/";
        StringBuilder builder=new StringBuilder();
        builder.append("<p>New Car suitable to your preferences has just been advertised</p><br>");
        builder.append("<p>Check it out here:</p><br>");
        for(String url:urls){
            builder.append("<a href=");
            builder.append("\"");
            builder.append(prefix+url+"\"");
            builder.append(">Super Auto</a><br>");
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
    //to search in local db
    public List<String> parseOfferInfoToURL(String word){
        String uri=word.toLowerCase();
        String delimiters="\\s+|,\\s*|\\.\\s*|\\+\\s*|-\\s*";
        //String pattern="[a-z]{1,}|[1-9]{1,}";
        String [] tokens=uri.split(delimiters);
        List<String> tokensList=Arrays.asList(tokens);

        List<String> filteredTokens=tokensList.stream().map(token ->{
            //code crash on length 0
            if(token.length()==0){
                return token;
            }
            String returnVal=token;
            String signBeginning=token.substring(0,1);
            String signEnd=token.substring(token.length()-1);
            //delete beginning sign
            if(signBeginning.matches("[-+.^:,!]")){
                returnVal=returnVal.substring(1);
            }
            //delete ending sign
            if(signEnd.matches("[-+.^:,!]")){
                returnVal=returnVal.substring(0,returnVal.length()-1);
            }
            return returnVal;
        }).collect(Collectors.toList());

        return filteredTokens.stream().map(StringUtils::stripAccents).collect(Collectors.toList());
    }

    //create announcement url suffix
    public List<String> getOfferUrlSuffix(ItemsWrapper itemsWrapper){
        StringBuilder builder=new StringBuilder();
        //List<CarInfo> carInfosPromoted=itemsWrapper.getItems().getPromoted();
        //List<CarInfo> carInfos=itemsWrapper.getItems().getRegular();
        List<CarInfo> infos= Stream.concat(itemsWrapper.getItems().getPromoted().stream(),
                                            itemsWrapper.getItems().getRegular().stream())
                .collect(Collectors.toList());

       // carInfosPromoted.addAll(carInfos); //add another infos

        List<String> urls=infos.stream().map(carInfo -> {
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
