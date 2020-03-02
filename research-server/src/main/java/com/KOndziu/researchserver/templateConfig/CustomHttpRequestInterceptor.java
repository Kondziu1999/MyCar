package com.KOndziu.researchserver.templateConfig;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

enum AllegroEnum {
    ACCEPT ("application/vnd.allegro.public.v1+json");
    String acceptHeader;
    AllegroEnum(String ACCEPT) {
        acceptHeader = ACCEPT;
    }
}

public class CustomHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("ACCEPT", AllegroEnum.ACCEPT.acceptHeader);
        return execution.execute(request, body);
    }
}