package uk.co.bikemandan.bikemechanicapp.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 15/03/2016.
 */
public class User {

    private static final long serialVersionUID = -4041502421563593320L;

    //@Key
    private static String id;

    public String getID_() {
        return ID_;
    }

    //@Key
    private String ID_;
    private static String sId;
    //@Key
    private String firstname;
    //@Key
    private String surname;
    //@Key
    private String username;
    //@Key
    private String email;

    //@Key
    private String post_code;
    //@Key
    private String address_line_1;
    //@Key
    private String address_line_2;
    //@Key
    private String address_line_3;
    //@Key
    private String contact_number;
    //@Key
    private String response;
    //@Key
    private String error;
    //@Key
    private int isAdmin;


    public String getEmail() {
        return email;
    }

    public static String getId() {
        return id;
    }

    public static  String getsId() {
        return sId;
    }
    public static void setsId(String s) {
        User.sId = s;
    }

    public String getPost_code() {
        return post_code;
    }
    public String getFirstname() {
        return firstname;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public String getError() {
        return error;
    }
    public String getSurname() {
        return surname;
    }

    public String getResponse() {
        return response;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public String getAddress_line_3() {
        return address_line_3;
    }

    public String getContact_number() {
        return contact_number;
    }
    public static void resetsId(){
        sId = "";
    }

    public static void setId(String i){
        id = i;
    }





}
