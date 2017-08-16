package com.demo;

import java.util.ArrayList;
import java.util.HashMap;

public interface TwitterService {
    public ArrayList<HashMap> getTweetsByName(String name) throws CustomException;

    ArrayList<HashMap> getTweetsBytext(String text) throws CustomException;

    void addMore(long count, String queryString) throws InterruptedException, CustomException;
}
