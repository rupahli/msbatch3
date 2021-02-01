package com.sl.ms.ordermanagement;

import com.sl.ms.ordermanagement.security.JWTAuthorizationFilter;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.apache.logging.log4j.LogManager;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableCaching
@RestController
@EnableSwagger2
public class OrderManagementApplication {

    private static final Logger LOGGER= LogManager.getLogger(OrderManagementApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(OrderManagementApplication.class, args);
    }
    @RequestMapping("/Sleuth")
    public String index() {
        LOGGER.info("Sleuth API is calling");
        return "Welcome Sleuth!";
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/user").permitAll()
                    .antMatchers("/swagger-ui.html**").permitAll()
                    .antMatchers("/v2/api-docs**").permitAll()
                    .antMatchers("/", "/csrf", "/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**").permitAll()
                    .anyRequest().authenticated();
        }
    }
}
