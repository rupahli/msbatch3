package com.sl.ms.ordermanagement.services;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InventoryServiceImpl {

    @Autowired
    RestTemplate restTemplate;

    @Value("${rest.url}")
    private String url;

    @Value("${rest.token}")
    private String token;

    public Object checkInvProduct(int productId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(url + "/{productid}", HttpMethod.GET, entity, Map.class,
                productId);
        return responseEntity.getBody();

    }
}
