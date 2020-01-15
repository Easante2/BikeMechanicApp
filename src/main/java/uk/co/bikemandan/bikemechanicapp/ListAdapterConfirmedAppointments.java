package uk.co.bikemandan.bikemechanicapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uk.co.bikemandan.bikemechanicapp.Model.Appointment;

/**
 * Created by Emmanuel on 28/04/2016.
 */
public class ListAdapterConfirmedAppointments extends ArrayAdapter<Appointment> {

    public Context context;
    public List<Appointment> values;
    public Date formatDate;
    public Date formatTime;



    public ListAdapterConfirmedAppointments(Context context, List<Appointment> values) {
        super(context, R.layout.admin_confirmed_appoint, values);
        this.context = context;
        this.values = values;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get each google place int the list through position
        final Appointment appoint = values.get(position);
        final View view = inflater.inflate(R.layout.admin_confirmed_appoint, parent, false);
        TextView name = (TextView) view.findViewById(R.id.c_name);
        TextView problemTitle = (TextView) view.findViewById(R.id.c_prob_title);
        TextView service = (TextView) view.findViewById(R.id.c_service);
        TextView date = (TextView) view.findViewById(R.id.cDate);
        TextView time = (TextView) view.findViewById(R.id.cTime);

        name.setText(appoint.getFirstname()+ " "+ appoint.getSurname());
        problemTitle.setText(appoint.getProblem_title());
        service.setText(appoint.getService_type());
/*
        date.setText(appoint.getDate());

        time.setText(appoint.getTime());
*/

        try {
            formatDate = new SimpleDateFormat("yyyy-MM-dd").parse(appoint.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String strFormatDate = new SimpleDateFormat("dd/MM/yyyy").format(formatDate);
        Log.i("Date", strFormatDate);


        try {
            formatTime = new SimpleDateFormat("HH:mm:ss").parse(appoint.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String strFormatTime = new SimpleDateFormat("HH:mm").format(formatTime);
        Log.i("Time", strFormatTime);




        date.setText(strFormatDate);
        time.setText(strFormatTime);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Item clicked", "list");
                // GooglePlace item = (GooglePlace) list.getItemAtPosition(position);

            }
        });
        //TextView
        return view;
    }




}
