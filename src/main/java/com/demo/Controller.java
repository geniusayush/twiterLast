package com.demo;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ayush on 13/04/17.
 */
@RestController
@RequestMapping("/search")

@CrossOrigin
public class Controller {
    @Autowired
    private TwitterService twitterService;


    @ApiOperation(value = "searchTweets")
    @RequestMapping(value = "/user/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ArrayList<HashMap> getSites(@PathVariable(value = "name") String name) throws Exception, CustomException {
        name = URLDecoder.decode(name, "UTF-8");
        return twitterService.getTweetsByName(name);
    }

    @ApiOperation(value = "searchTweets")
    @RequestMapping(value = "/tweet", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ArrayList<HashMap> getSitesText(@RequestParam(value = "text") String text) throws Exception, CustomException {
        text = URLDecoder.decode(text, "UTF-8");
        return twitterService.getTweetsBytext(text);
    }

}
