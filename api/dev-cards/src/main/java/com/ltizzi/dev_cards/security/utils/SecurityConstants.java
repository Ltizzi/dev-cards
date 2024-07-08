package com.ltizzi.dev_cards.security.utils;

/**
 * @author Leonardo Terlizzi
 */


public interface SecurityConstants {

    public static final String JWT_KEY = System.getenv("SECRET_KEY");
    public static final String JWT_HEADER = "Authorization";
}
