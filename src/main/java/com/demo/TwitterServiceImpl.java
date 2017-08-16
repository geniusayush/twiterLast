package com.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TwitterServiceImpl implements TwitterService {

    @Autowired
    Twitter twitter;

    @Autowired
    RestClient client;

    @Override
    public ArrayList<HashMap> getTweetsByName(String name) throws CustomException {
        Response indexResponse = null;
        try {
            Map map = new HashMap<String, String>();
            map.put("q", "name:" + name);
            indexResponse = client.performRequest("POST", "/tweets/tweet/_search", map);

            return getHashMaps(indexResponse);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new CustomException(e);
        }
    }

    private ArrayList<HashMap> getHashMaps(Response indexResponse) throws IOException {
        String result = new BufferedReader(new InputStreamReader(indexResponse.getEntity().getContent()))
                .lines().collect(Collectors.joining("\n"));
        ArrayList list = (ArrayList) ((LinkedHashMap) (new ObjectMapper().readValue(result, Map.class).get("hits"))).get("hits");
        System.out.println("data done");
        ArrayList<HashMap> responseList= new ArrayList();
        for (int i = 0; i <list.size(); i++) {
            responseList.add((HashMap)(((HashMap)(list.get(i))).get("_source")));
        }
        return responseList;
    }

    @Override
    public ArrayList<HashMap> getTweetsBytext(String text) throws CustomException {
        Response indexResponse = null;
        try {
            Map map = new HashMap<String, String>();
            map.put("q", "text:" + text);
            indexResponse = client.performRequest("POST", "/tweets/tweet/_search", map);
            return getHashMaps(indexResponse);        } catch (IOException e) {
            throw new CustomException(e);
        }
    }



    @Override
    public void addMore(long count, String queryString) throws InterruptedException, CustomException {

        Query query = new Query(queryString);
        QueryResult result = null;
        while (count < 100000) {
            try {
                result = twitter.search(query);
            } catch (TwitterException e) {
                throw new CustomException(e);
            }

            for (Status s : result.getTweets()) {
                Map<String, Object> json = new HashMap<String, Object>();
                json.put("name", s.getUser().getName());
                json.put("text", s.getText());
                json.put("id", s.getId() + "");
                json.put("retweets", s.getRetweetCount());
                json.put("favourites", s.getFavoriteCount());
                String mapAsJson = null;
                try {
                    mapAsJson = new ObjectMapper().writeValueAsString(json);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    continue;
                }
                HttpEntity entity = new StringEntity(mapAsJson, ContentType.APPLICATION_JSON);
                Response indexResponse = null;
                try {
                    indexResponse = client.performRequest("POST", "/tweets/tweet/" + count, Collections.<String, String>emptyMap(), entity);
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                if (indexResponse.getStatusLine().getStatusCode() == 200) ;
                count++;

            }
            System.out.println(count);
            int remaining = result.getRateLimitStatus().getRemaining();
            query = result.nextQuery();
            System.out.println(result.nextQuery());

            if (remaining == 0) Thread.sleep(result.getRateLimitStatus().getResetTimeInSeconds() * 1000);
            else Thread.sleep(5000);
        }
    }
}

