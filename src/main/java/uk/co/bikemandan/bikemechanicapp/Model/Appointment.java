package uk.co.bikemandan.bikemechanicapp.Model;

import java.util.List;

/**
 * Created by Emmanuel on 26/04/2016.
 */
public class Appointment {
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public void setBike_part_fault(String bike_part_fault) {
        this.bike_part_fault = bike_part_fault;
    }

    public void setProblem_title(String problem_title) {
        this.problem_title = problem_title;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    //@Key
    private String firstname;
    //@Key
    private String surname;
        //@Key
        private String scheduleId;
    //@Key
    private String problem_title;
    //@Key
    private String bike_part_fault;
    //@Key
    private String service_type;
       //@Key
       private String date;
       //@Key
       private String time;
    //@Key
    private String request_id;
    //@Key
    private String appointment_id;

    //@Key
    private String address_line_1;
    //@Key
    private String post_code;


     private List<String> apDates;



    public void setAppointResults(List<Appointment> appointResults) {
        this.appointResults = appointResults;
    }

    public List<Appointment> appointResults;

    public List<Appointment> getAppointResults() {
        return appointResults;
    }




    public String getScheduleId() {
        return scheduleId;
    }

    public String getProblem_title() {
        return problem_title;
    }

    public String getBike_part_fault() {
        return bike_part_fault;
    }

    public String getService_type() {
        return service_type;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getRequest_id() {
        return request_id;
    }

    public String getSurname() {
        return surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getAppointment_id() {
        return appointment_id;


    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

}
