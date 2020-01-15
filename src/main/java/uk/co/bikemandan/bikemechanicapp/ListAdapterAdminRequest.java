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

import java.util.List;

import uk.co.bikemandan.bikemechanicapp.Model.Appointment;

/**
 * Created by Emmanuel on 26/04/2016.
 */
public class ListAdapterAdminRequest extends ArrayAdapter<Appointment> {

    public Context context;
    public List<Appointment> values;
    public static String appointId;


    public ListAdapterAdminRequest(Context context, List<Appointment> values) {
        super(context, R.layout.admin_appointment_list, values);
        this.context = context;
        this.values = values;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get each google place int the list through position
        final Appointment appoint = values.get(position);
        final View view = inflater.inflate(R.layout.admin_appointment_list, parent, false);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView problemTitle = (TextView) view.findViewById(R.id.prob_title);
      //  TextView bikeFault = (TextView) view.findViewById(R.id.bike_fault);
        TextView service = (TextView) view.findViewById(R.id.service);

        name.setText(appoint.getFirstname()+ " "+ appoint.getSurname());
        problemTitle.setText(appoint.getProblem_title());
        //bikeFault.setText(appoint.getBike_part_fault());
        service.setText(appoint.getService_type());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Item clicked", "list");
                // GooglePlace item = (GooglePlace) list.getItemAtPosition(position);
                Toast.makeText(getContext(), values.get(position).getFirstname(),
                        Toast.LENGTH_LONG).show();
                Log.i("PLACES_EXAMPLE", values.get(position).getFirstname());

                appointId = values.get(position).getAppointment_id();
                context.startActivity(new Intent(context,AdminRequestDatesActivity.class ));
                notifyDataSetChanged();
            }
        });
        //TextView
        return view;
    }




}
