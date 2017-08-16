package com.demo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.nio.entity.NStringEntity;

import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;


import static twitter4j.TwitterFactory.*;

@SpringBootApplication()
@EnableSwagger2
@EnableCaching
public class App {


    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(App.class, args);
    }


    @Bean(name = "twitterClient")
    public Twitter getTwiter() {
        twitter4j.Twitter twitter = getSingleton();
        twitter.setOAuthConsumer("","");
        twitter.setOAuthAccessToken(new AccessToken("-", ""));
        return twitter;
    }

    @Bean(name = "restClient")
    public RestClient getRest() {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")).build();
       return restClient;
    }
    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
        messageBundle.setBasename("classpath:messages/messages");
        messageBundle.setDefaultEncoding("UTF-8");
        return messageBundle;
    }

    @Bean
    public Docket ProductIntegrationApi() {

        Set<String> produceTypes = new HashSet<>();
        produceTypes.add("application/json");
        //produceTypes.add("application/xml");

        Set<String> consumeTypes = new HashSet<>();
        consumeTypes.addAll(produceTypes);
        consumeTypes.add("application/x-www-form-urlencoded");

        return new Docket(DocumentationType.SWAGGER_2)
                .consumes(consumeTypes)
                .produces(produceTypes)
                .groupName("demo")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.demo"))
                .paths(PathSelectors.any())

                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .version("1.0")
                .build();
    }


}
