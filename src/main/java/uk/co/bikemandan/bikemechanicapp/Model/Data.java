package uk.co.bikemandan.bikemechanicapp.Model;

import java.util.List;

/**
 * Created by Emmanuel on 25/04/2016.
 */
public class Data {
    public List<User> userResults;
    public List<Bike> bikeResults;

    public void setAppointResults(List<Appointment> appointResults) {
        this.appointResults = appointResults;
    }

    public List<Appointment> appointResults;
    //@Key
    public String response;
    //@Key
    public String error;
    //@Key
    public static String token;

    public String getTok() {
        return tok;
    }

    public String tok;


    public List<User> getUserResults() {
        return userResults;
    }

    public List<Bike> getBikeResults() {
        return bikeResults;
    }
    public List<Appointment> getAppointResults() {
        return appointResults;
    }


 /*   public void addAppointResult(Appointment a) {
        appointResults.add(a);

    }*/
    public void addResult(User a) {
        userResults.add(a);

    }

    public String getResponse() {
        return response;
    }
    public String getError() {
        return error;
    }

    public static void setToken(String s) {
        token = s;
    }

    public static String getToken() {
        return token;
    }

   // public static void setToken(String token) {
   //      Data.token = token;
   // }

    public static void resetToken(){
        token = "";
    }

}
