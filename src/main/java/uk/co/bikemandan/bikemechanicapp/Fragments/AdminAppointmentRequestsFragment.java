package uk.co.bikemandan.bikemechanicapp.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.List;

import uk.co.bikemandan.bikemechanicapp.AdminHomeActivity;
import uk.co.bikemandan.bikemechanicapp.ListAdapterAdminRequest;
import uk.co.bikemandan.bikemechanicapp.LoginActivity;
import uk.co.bikemandan.bikemechanicapp.Model.Appointment;
import uk.co.bikemandan.bikemechanicapp.Model.Data;
import uk.co.bikemandan.bikemechanicapp.Model.User;
import uk.co.bikemandan.bikemechanicapp.R;

/**
 * Created by Emmanuel on 26/04/2016.
 */
public class AdminAppointmentRequestsFragment extends Fragment  implements AdapterView.OnItemClickListener {

    public List<Appointment> appointmentList;
    private ListView list;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_appoint_requests, container, false);
        getActivity().setTitle("Repair Requests");
        list = (ListView)view.findViewById(R.id.list);
        String request = "http://159.203.93.1/v1/getAppointmentRequest";
        SendDataToServer process = new SendDataToServer();
        process.execute(new String[]{request});

        appointmentList = new ArrayList<>();
        return view;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        Data.resetToken();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }

    public void parseData(String objs) throws JSONException {
        JSONObject mJsonObject = new JSONObject(objs);
        /*Gson gson = new Gson();
        Appointment results = gson.fromJson(objs, Appointment.class);*/
        for (int i = 0; i < mJsonObject.length(); i++) {
           // JsonParser parser = new JsonParser();
            JSONObject a = mJsonObject.getJSONObject(String.valueOf(i));
            Log.i("JSON", "Results" + a.toString());
            Appointment newAppoint = new Appointment();
            newAppoint.setProblem_title(a.getString("problem_title"));
            Log.i("JSON", "Results" + newAppoint.getProblem_title());
            newAppoint.setBike_part_fault(a.getString("bike_part_fault"));
            newAppoint.setService_type(a.getString("service_type"));
            newAppoint.setFirstname(a.getString("firstname"));
            newAppoint.setSurname(a.getString("surname"));
            newAppoint.setRequest_id(a.getString("request_id"));
            newAppoint.setAppointment_id(a.getString("appointment_id"));

            appointmentList.add(newAppoint);
                Log.i("JSON", "List  " + appointmentList.get(0));


        }


    }

    protected void reportBack(String data) {
         Gson gson = new Gson();
        Data results = gson.fromJson(data, Data.class);

        if(results.getResponse().equals("success")){


        try {
            parseData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }


         ListAdapterAdminRequest adapter = new ListAdapterAdminRequest(getContext(),appointmentList);
            list.setAdapter(adapter);
            list.setOnItemClickListener(this);

    }else{
            Toast.makeText(getActivity(), "Error: Something went wrong, please try again",
                    Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Click", "item has been clicked");
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
                .appendQueryParameter("id", User.getId());
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

        }

    }
}
