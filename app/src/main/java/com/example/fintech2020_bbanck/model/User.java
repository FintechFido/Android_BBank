package com.example.fintech2020_bbanck.model;

public class User {
    private static String sessionKey;
    private static int runningCode;
    private static String myImei;
    private static User user = new User();

    // Singleton
    private User()
    {
        runningCode = 2;
    }

    public static User getInstance(){
        return user;
    }

    public void setInfo(String sessionKey, String imei)
    {
        this.sessionKey = sessionKey;
        this.myImei = imei;
        System.out.println(sessionKey+" "+imei);
    }

    public String getSessionKey()
    {
        return sessionKey;
    }

    public int getRunningCode()
    {
        return runningCode;
    }

    public String getImei()
    {
        return myImei;
    }

}
