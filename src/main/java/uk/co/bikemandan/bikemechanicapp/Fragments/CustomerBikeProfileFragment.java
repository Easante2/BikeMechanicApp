package uk.co.bikemandan.bikemechanicapp.Fragments;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.bikemandan.bikemechanicapp.LoginActivity;
import uk.co.bikemandan.bikemechanicapp.Model.Data;
import uk.co.bikemandan.bikemechanicapp.Model.User;
import uk.co.bikemandan.bikemechanicapp.R;

/**
 * Created by Emmanuel on 26/04/2016.
 */
public class CustomerBikeProfileFragment extends Fragment  implements AdapterView.OnItemSelectedListener, View.OnClickListener {



    public Spinner spinner2, spinner3, spinner5;
    private static final String[]chainRingsOptions = {"Select", "Don't know", "1", "2", "3", "4"};
    private static final String[]cogsOptions = {"Select", "Don't know", "1", "5","6","7", "8", "9", "10", "11"};
    private static final String[]brakeOptions = {"Select", "Don't know", "V-brake", "Caliper", "Cantilever", "Disc (Cable)","Disc (hydraulic)"
            ,"Hydraulic Rim" };

    public String sChanges, sChainRings, sCogs ,sBrakes,
            strYear;
    public EditText editTextMake, editTextModel, editTextYear, editTextChanges, editTextTyre, editTextGears;
    private ImageView tyre_tooltip;
    private ImageView chainRings_tooltip;
    private ImageView gears_tooltip;
    private String make, model, sYear, changes, tyreSize, gears;
    private Context context ;
    private Button btnUpdate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customer_bike_profile, container, false);
        getActivity().setTitle("Bike Registration");
        setHasOptionsMenu(true);

        editTextMake = (EditText) view.findViewById(R.id.bike_make);
        editTextModel = (EditText) view.findViewById(R.id.bike_model);
        editTextYear = (EditText) view.findViewById(R.id.bike_year);
        editTextChanges = (EditText) view.findViewById(R.id.bike_changes);
        editTextTyre = (EditText) view.findViewById(R.id.bike_tyre);
        editTextGears = (EditText) view.findViewById((R.id.no_gears));

        make = editTextMake.getText().toString();
        model = editTextModel.getText().toString();
        sYear = editTextYear.getText().toString();
        changes= editTextChanges.getText().toString();
        tyreSize= editTextTyre.getText().toString();
        gears= editTextGears.getText().toString();

        tyre_tooltip = (ImageView) view.findViewById(R.id.tooltip_tyre);
        chainRings_tooltip = (ImageView) view.findViewById(R.id.tooltip_chainrings);
        gears_tooltip = (ImageView) view.findViewById(R.id.tooltip_gears);
        tyre_tooltip.setOnClickListener(this);
        chainRings_tooltip.setOnClickListener(this);
        gears_tooltip.setOnClickListener(this);

        context = getActivity();
        btnUpdate = (Button) view.findViewById(R.id.register);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNumeric(sYear)) {
                   /* String request = "http://159.203.93.1/v1/resgister";
                    SendDataToServer process = new SendDataToServer();
                    process.execute(new String[]{request});*/
                } else {
                    Toast.makeText(getActivity(), "Year not valid",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        // ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        // Enable the Up button
        // ab.setDisplayHomeAsUpEnabled(true);
        setUpSpinners(view);
        return view;
    }


    public static boolean isNumeric(String str)
    {
        String PATTERN =("^[0-9]*$");

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
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
       // User.resetId();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // selection1= paths[0];
        switch(parent.getId()){
            case R.id.spinner_chainrings:
                sChainRings = chainRingsOptions[position];
                Log.i("Spinner Value:", sChainRings);
                break;
            case R.id.spinner_rear_cogs:
                sCogs = cogsOptions[position];
                break;
            case R.id.spinner_brakes:
                sBrakes = brakeOptions[position];
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //handles the onclick events of the checkboxs and tooltip image select
    @Override
    public void onClick(View v) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_tooltip);
        dialog.setTitle("Tip");
        TextView text = (TextView) dialog.findViewById(R.id.text);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        int id = v.getId();
        switch (id){
            case R.id.tooltip_tyre:

                text.setText("Found embossed onto one of the sides of each tyre in he following format or similar: 26” x 1.75 or 700 x 35c");

                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
            case R.id.tooltip_chainrings:

                text.setText("Located on the front set near where the pedals attach");
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
            case R.id.tooltip_gears:
                text.setText("Displayed at the gear changers on the handlebars");
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
        }
        // set the custom dialog components - text, image and button



    }


    //set up the drop down spinners
    public void setUpSpinners(View view){


        spinner2 = (Spinner)view.findViewById(R.id.spinner_chainrings);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, chainRingsOptions);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        spinner3 = (Spinner)view.findViewById(R.id.spinner_rear_cogs);
        ArrayAdapter<String>adapter3 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, cogsOptions);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);


        spinner5 = (Spinner)view.findViewById(R.id.spinner_brakes);
        ArrayAdapter<String>adapter5 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, brakeOptions);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter5);
        spinner5.setOnItemSelectedListener(this);

    }

    protected void reportBack(String data) {

        Gson gson = new Gson();
        User results = gson.fromJson(data, User.class);
        Log.i("Laravel", "Results" + results);



        if(results.equals("success")){
            Toast.makeText(getActivity(), "Successfully updated",
                    Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);

        }else{
            Toast.makeText(getActivity(), "Error: Something went wrong, please try again",
                    Toast.LENGTH_LONG).show();
            Log.i("Error", results.getError());
        }

    }

    private class SendDataToServer extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonResults = new StringBuilder();
            HttpURLConnection urlConnection = null;

            try {
                int[] i= new int[5];
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
                        .appendQueryParameter("make", make)
                        .appendQueryParameter("model", model)
                        .appendQueryParameter("year", sYear)
                        .appendQueryParameter("changes", changes)
                        .appendQueryParameter("tyre_size", tyreSize)
                        .appendQueryParameter("gears", gears)
                        .appendQueryParameter("front_cogs", sChainRings)
                        .appendQueryParameter("rear_cogs", sCogs)
                        .appendQueryParameter("brake_type", sBrakes);
                String query = builder.build().getEncodedQuery();
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                //= urlConnection.getInputStream();
                // int responseCode=urlConnection.getResponseCode();
                InputStream inStream;
                if (urlConnection.getResponseCode() >= 400) {
                    inStream = urlConnection.getErrorStream();
                } else {
                    inStream = urlConnection.getInputStream();

                }
                BufferedReader in = new BufferedReader(new InputStreamReader(inStream));

                //     InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
                Log.i("EFA", "RESULTS: " + jsonResults);
                String input = jsonResults.toString();
                Log.i("EFA", "Input: " + input);

              /*
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
                //input stream
                int read;
                char[] buff = new char[1024];
                while ((read = inputStream.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
                Log.i("EFA", "RESULTS: " + jsonResults);
                String input = jsonResults.toString();*/
              /*  Gson gson = new Gson();
                User data = gson.fromJson(input, User.class);*/
                return input;
            } catch (MalformedURLException e) {
                Log.i("Laravel", "Error processing API URL");
                return null;
            } catch (IOException e) {
                Log.i("Laravel", "Error connecting to API");
                return null;
            }catch (Exception e) {
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
