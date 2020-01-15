package uk.co.bikemandan.bikemechanicapp.Fragments;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.bikemandan.bikemechanicapp.AdminHomeActivity;
import uk.co.bikemandan.bikemechanicapp.CustomerAppointmentActivity;
import uk.co.bikemandan.bikemechanicapp.CustomerHomeActivity;
import uk.co.bikemandan.bikemechanicapp.LoginActivity;
import uk.co.bikemandan.bikemechanicapp.Model.Data;
import uk.co.bikemandan.bikemechanicapp.Model.User;
import uk.co.bikemandan.bikemechanicapp.Model.ValidateForm;
import uk.co.bikemandan.bikemechanicapp.R;


/**
 * Created by Emmanuel on 21/04/2016.
 */
public class RepairRequestFragment extends Fragment implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, ValidateForm {
    public Spinner serviceTypeSpinner, timeSpinner;
    private static final String[] serviceOptions = {"Select", "Basic service", "Full service", "Specific repair"};
    private static final String[] timeOptions = {"Select", "09:00", "13:00", "17:00"};
    public String sServiceType, sTime, sProblem_title, sBike_part, sDecription, sDate;
    private EditText editTextProblemTitle, editTextBikePartFault, editTextDescription;
    private TextView textViewDate, ad_date_set, ad_set_time;
    private Button btn;
    private List<String> appointDates;
    private List<String> appointTimes;
    private ImageView addIcon;
    public LinearLayout inner_container;
    private Button btnSubmit;
    private HashMap<String, String> dateTime;
    private Boolean validDates = true;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_repair_request, container, false);
        setHasOptionsMenu(true);
        dateTime = new HashMap<String, String>();
        editTextProblemTitle = (EditText) view.findViewById(R.id.problem_title);
        editTextBikePartFault = (EditText) view.findViewById(R.id.bike_part_fault);
        editTextDescription = (EditText) view.findViewById(R.id.description);
        textViewDate = (TextView) view.findViewById(R.id.date_select);
        setUpSpinner(view);
        showDialogOnEditText();
        inner_container = (LinearLayout) view.findViewById(R.id.inner_container);
        addIcon = (ImageView) view.findViewById(R.id._add);
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.set_appointment_row, null);
                ad_date_set = (TextView) addView.findViewById(R.id.additional_date_set);
                ad_date_set.setText(textViewDate.getText().toString());
                ad_set_time = (TextView) addView.findViewById(R.id.additional_time_set);
                ad_set_time.setText(sTime);


                ImageView removeIcon = (ImageView) addView.findViewById(R.id._remove);
                removeIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) addView.getParent()).removeView(addView);
                    }
                });
                inner_container.addView(addView);
            }
        });
        LayoutTransition transition = new LayoutTransition();
        inner_container.setLayoutTransition(transition);

        btnSubmit = (Button) view.findViewById(R.id.submit_appoint);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String showallPrompt = "";

                int childCount = inner_container.getChildCount();
                showallPrompt += "childCount: " + childCount + "\n\n";
                for (int c = 0; c < childCount; c++) {
                    View childView = inner_container.getChildAt(c);
                    TextView childDateTextView = (TextView) (childView.findViewById(R.id.additional_date_set));
                    String childDate = (String) (childDateTextView.getText());

                    TextView childTimeTextView = (TextView) (childView.findViewById(R.id.additional_time_set));
                    String childTime = (String) (childTimeTextView.getText());

                    dateTime.put(childDate, childTime);
                    showallPrompt += c + ": " + childDate + " :: " + childTime + "\n";
                }

                Log.i("Dates", showallPrompt);
                sDate = textViewDate.getText().toString();
                dateTime.put(sDate, sTime);

                int y = 0;
                mainLoop:
                for (Map.Entry<String, String> entry : dateTime.entrySet()) {
                    int x = 0;
                    String comparison_key = entry.getKey();
                    String comparison_value = entry.getValue();
                    for (Map.Entry<String, String> currentMap : dateTime.entrySet()) {
                        String mapKey = currentMap.getKey();
                        String mapValue = currentMap.getValue();
                        if(comparison_key.equals(mapKey)){
                            x++;
                        } if (comparison_value.equals(mapValue) ) {
                            x++;
                        } if(x == 4) {
                            validDates = false;
                            break mainLoop;

                        }
                        }
                    }

                validateDetails();

            }
        });

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
       // User.resetId();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }

    public void showDialogOnEditText() {
        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                //datePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        datePickerListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                // dpd.setSelectableDays(dates);
                //dpd.show(getFragmentManager(), "Datepickerdialog");

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
                datePickerDialog.show();
            }
        });
    }


    public void setUpSpinner(View view) {

        serviceTypeSpinner = (Spinner) view.findViewById(R.id.spinner_service_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, serviceOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceTypeSpinner.setAdapter(adapter);
        serviceTypeSpinner.setOnItemSelectedListener(this);

        timeSpinner = (Spinner) view.findViewById(R.id.spinner_time);
        ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, timeOptions);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapterTime);
        timeSpinner.setOnItemSelectedListener(this);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_service_types:
                if (serviceOptions[position].equals("Select")) {
                    sServiceType = "";
                    Log.i("Spinner Value:", sServiceType);
                } else {
                    sServiceType = serviceOptions[position];
                    Log.i("Spinner Value:", sServiceType);
                }
                break;
            case R.id.spinner_time:
                if (timeOptions[position].equals("Select")) {
                    sTime = "";
                } else {
                    sTime = timeOptions[position];
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            int month = selectedMonth + 1;
            String formattedMonth = "" + month;
            String formattedDay = "" + selectedDay;
            if (month < 10) {

                formattedMonth = "0" + month;
            }
            if (selectedDay < 10) {

                formattedDay = "0" + selectedDay;
            }

            Log.i( "new date :  ",  formattedDay + "-" + formattedMonth + "-" +
                    selectedYear);
            String date = formattedDay + "/" + formattedMonth + "/" + selectedYear;


            SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date newDate = curFormater.parse(date);
                Log.i("DATE ", " " + newDate);
                Log.i("Formated", " " + curFormater.format(newDate));
                String formattedDate = curFormater.format(newDate);
                Log.i("Formatted Date", " " + formattedDate);
                // appointDates.add(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Date dateObj = curFormater.parse(date);
            Log.i("Month Value:", formattedMonth);
            textViewDate.setText(date);
            //Do whatever you want
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener2
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            int month = selectedMonth + 1;
            String formattedMonth = "" + month;
            String formattedDay = "" + selectedDay;
            if (month < 10) {

                formattedMonth = "0" + month;
            }
            if (selectedDay < 10) {

                formattedDay = "0" + selectedDay;
            }

           Log.i("New Date   ", "dt: " + formattedDay + "-" + formattedMonth + "-" +
                    selectedYear);
            String date = formattedDay + "/" + formattedMonth + "/" + selectedYear;


            SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date newDate = curFormater.parse(date);
                Log.i("DATE ", " " + newDate);
                Log.i("Formated", " " + curFormater.format(newDate));
                String formattedDate = curFormater.format(newDate);
                Log.i("Formatted Date", " " + formattedDate);
                // appointDates.add(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Date dateObj = curFormater.parse(date);
            Log.i("Month Value:", formattedMonth);

            //Do whatever you want
        }
    };

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }


    @Override
    public void validateDetails() {
        sProblem_title = editTextProblemTitle.getText().toString();
        sBike_part = editTextBikePartFault.getText().toString();
        sDecription = editTextDescription.getText().toString();


        if (sProblem_title.equals("") || sBike_part.equals("") || sDecription.equals("")) {
            Toast.makeText(getActivity(), "Please fill in all fields",
                    Toast.LENGTH_LONG).show();
        } else if (dateTime.isEmpty()) {
            Toast.makeText(getActivity(), "Please set an appointment",
                    Toast.LENGTH_LONG).show();
        } else if (!validDates) {
            Toast.makeText(getActivity(), "Error: Duplicate dates chosen", Toast.LENGTH_LONG).show();
        }else{

            String request = "http://159.203.93.1/v1/makeAppointment";
            SendDataToServer process = new SendDataToServer();
            process.execute(new String[]{request});

    }


    }


    protected void reportBack(String data) {

        Gson gson = new Gson();
        Data results = gson.fromJson(data, Data.class);
        Log.i("Laravel", "Results" + results);

        if(results.getResponse().equals("success")){
            Toast.makeText(getActivity(), "Appointment request sent",
                    Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), CustomerAppointmentActivity.class);
            startActivity(i);

        }else{
            Toast.makeText(getActivity(), "Error: Something went wrong, please try again",
                    Toast.LENGTH_LONG).show();
            Log.i("Error", results.getError());
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
                Uri.Builder builder = new Uri.Builder();
                int x = 0;
                for (Map.Entry<String,String> entry : dateTime.entrySet()) {
                    String dateVal = entry.getKey();
                    String timeVal = entry.getValue();

                    builder.appendQueryParameter("date" + x, dateVal);
                    builder.appendQueryParameter("time" + x, timeVal);
                    x++;
                }
                    // do stuff
                builder.appendQueryParameter("problem_title", sProblem_title)
                        .appendQueryParameter("bike_part", sBike_part)
                        .appendQueryParameter("desc", sDecription)
                        .appendQueryParameter("service", sServiceType)
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
                return input;
            } catch (MalformedURLException e) {
                Log.i("Laravel", "Error processing Places API URL");
                return null;
            } catch (IOException e) {
                Log.i("Laravel", "Error connecting to Places API");
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
