package com.ecomm.productservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class TokenService
{
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);


    @Autowired
    @Qualifier("authValidateWebClient")
    WebClient authValidateWebClient;

    public String validateToken(String token, String userType) throws WebClientResponseException
    {
        logger.info("TokenService.validateToken() called with token: " + token);
        return authValidateWebClient.get()
                .header("Authorization", token)
                .header("UserType", userType)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Assuming the token is valid for demonstration purposes
    }


}
