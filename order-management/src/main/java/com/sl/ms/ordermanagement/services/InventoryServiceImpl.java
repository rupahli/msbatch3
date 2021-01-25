package com.sl.ms.ordermanagement.services;

import java.util.Date;
import java.util.Map;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@EnableHystrix
@Service
public class InventoryServiceImpl {

    private static final Logger LOGGER = LogManager.getLogger(InventoryServiceImpl.class.getName());

    @Autowired
    RestTemplate restTemplate;

    @Value("${rest.url}")
    private String url;

    @Value("${rest.token}")
    private String token;

    @HystrixCommand(fallbackMethod = "serviceUnavailableError", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
    })
    public Object checkInvProduct(int productId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(url + "/{productid}", HttpMethod.GET, entity, Map.class,
                productId);
        return responseEntity.getBody();

    }
    private String serviceUnavailableError(int productId) {
        LOGGER.info("Looks like service unavailable. Please try later.");
        return "Looks like service unavailable. Please try later.";
    }
}
