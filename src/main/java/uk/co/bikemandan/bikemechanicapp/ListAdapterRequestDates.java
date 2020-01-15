package uk.co.bikemandan.bikemechanicapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uk.co.bikemandan.bikemechanicapp.Model.Appointment;
import uk.co.bikemandan.bikemechanicapp.Model.Data;
import uk.co.bikemandan.bikemechanicapp.Model.User;

/**
 * Created by Emmanuel on 28/04/2016.
 */
public class ListAdapterRequestDates extends ArrayAdapter<Appointment> {

    public Context context;
    public List<Appointment> values;
    public String ID;
    public String reqId;
    public Date formatDate;
    public Date formatTime;

    public ListAdapterRequestDates(Context context, List<Appointment> values) {
        super(context, R.layout.admin_request_dates_custom_list, values);
        this.context = context;
        this.values = values;

    }
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get each google place int the list through position
        final Appointment appoint = values.get(position);
        final View view = inflater.inflate(R.layout.admin_request_dates_custom_list, parent, false);
        TextView date = (TextView) view.findViewById(R.id.req_date);
        TextView time = (TextView) view.findViewById(R.id.req_time);

       String dateString1 = "16-04-2011";
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
                Toast.makeText(getContext(), values.get(position).getDate(),
                        Toast.LENGTH_LONG).show();
                Log.i("PLACES_EXAMPLE", values.get(position).getDate());
                Log.i("PLACES_EXAMPLE", values.get(position).getScheduleId());
                ID = values.get(position).getScheduleId();
                reqId = values.get(position).getRequest_id();
                String request = "http://159.203.93.1/v1/confirmApDateTime";
                SendDataToServer process = new SendDataToServer();
                process.execute(new String[]{request});

            }
        });

        return view;
    }

    protected void reportBack(String data) {
        Gson gson = new Gson();
        Data results = gson.fromJson(data, Data.class);


        if(results.getResponse().equals("success")){

            Log.i("Laravel", "Results" + results);

            if(results.getResponse().equals("success")){
                Toast.makeText(getContext(), "Appointment date confirmed",
                        Toast.LENGTH_LONG).show();

                context.startActivity(new Intent(context, AdminRequestActivity.class));

            }else if(results.getResponse().equals("failed")){
                Toast.makeText(getContext(), "Error: Something went wrong, please try again",
                        Toast.LENGTH_LONG).show();
                Log.i("Error", "Results" + results.getError());
            }



        }else{

            Toast.makeText(getContext(), "Error: Something went wrong, please try again",
                    Toast.LENGTH_LONG).show();

        }


    }


    private class SendDataToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonResults = new StringBuilder();
            HttpURLConnection urlConnection = null;

            try {
                int[] i = new int[5];
                URL url = new URL(urls[0]);
                Log.i("EFA", url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                //  urlConnection.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("token", Data.getToken())
                        .appendQueryParameter("id", User.getId())
                        .appendQueryParameter("scheduleId", ID)
                        .appendQueryParameter("requestId", reqId);
                String query = builder.build().getEncodedQuery();
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                InputStream inStream;
                if (urlConnection.getResponseCode() >= 400) {
                    inStream = urlConnection.getErrorStream();
                } else {
                    inStream = urlConnection.getInputStream();

                }
                BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
                Log.i("EFA", "RESULTS: " + jsonResults);
                String input = jsonResults.toString();
                Log.i("EFA", "Input: " + input);
                return input;
            } catch (MalformedURLException e) {
                Log.i("Laravel", "Error processing API URL");
                return null;
            } catch (IOException e) {
                Log.i("Laravel", "Error connecting to API");
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("PLACES_EXAMPLE", e.getMessage());
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }


        }

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onPostExecute(String s) {

            reportBack(s);
            Log.i("RES", s);
        }

    }



}
