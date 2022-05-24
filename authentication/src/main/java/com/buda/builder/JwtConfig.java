package com.buda.builder;

import java.util.UUID;

import com.buda.util.SHA256Encoder;

public class JwtConfig {
    // PROD
    public final static String url = "http://103.173.228.124:8080/";
    public final static String secretKey = SHA256Encoder.encode(UUID.randomUUID().toString());
    public static int HoursAccessToken = 1;
    public static int HoursRefreshToken = 480;

    //  DEV
    // public final static String url = "http://localhost:8080";
    // public final static String secretKey = "HelloWeAreBuda";
    // public static int HoursAccessToken = 5;
    // public static int HoursRefreshToken = 48;
}
