package com.demo;

import twitter4j.TwitterException;

public class CustomException extends Throwable {
    public CustomException(Throwable e) {
        super(e);
    }
}
