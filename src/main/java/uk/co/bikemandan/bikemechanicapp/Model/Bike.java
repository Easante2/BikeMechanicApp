package uk.co.bikemandan.bikemechanicapp.Model;

import java.util.List;

/**
 * Created by Emmanuel on 21/04/2016.
 */
public class Bike {
    private List<Bike> results;
    private static final long serialVersionUID = -4041502421563593320L;
    //@Key
    private String model;
    //@Key
    private String make;
    //@Key
    private String year;
    //@Key
    private String changes_since_new;
    //@Key
    private String tyre_size;
    //@Key
    private String no_of_front_cogs;
    //@Key
    private String 	no_of_rear_cogs;
    //@Key
    private String 	no_of_gears;
    //@Key
    private String 	brake_type;
    //@Key
    private int id;

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }

    public String getYear() {
        return year;
    }

    public String getChanges_since_new() {
        return changes_since_new;
    }

    public String getTyre_size() {
        return tyre_size;
    }

    public String getNo_of_front_cogs() {
        return no_of_front_cogs;
    }

    public String getNo_of_rear_cogs() {
        return no_of_rear_cogs;
    }

    public String getNo_of_gears() {
        return no_of_gears;
    }

    public String getBrake_type() {
        return brake_type;
    }

    public int getId() {
        return id;
    }




    public List<Bike> getResults() {
        return results;
    }


}
