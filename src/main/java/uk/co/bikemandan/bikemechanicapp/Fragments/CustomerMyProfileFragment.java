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
import android.widget.Button;
import android.widget.EditText;
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

import uk.co.bikemandan.bikemechanicapp.AdminHomeActivity;
import uk.co.bikemandan.bikemechanicapp.CustomerHomeActivity;
import uk.co.bikemandan.bikemechanicapp.LoginActivity;
import uk.co.bikemandan.bikemechanicapp.Model.Data;
import uk.co.bikemandan.bikemechanicapp.Model.User;
import uk.co.bikemandan.bikemechanicapp.Model.ValidateForm;
import uk.co.bikemandan.bikemechanicapp.R;

/**
 * Created by Emmanuel on 26/04/2016.
 */
public class CustomerMyProfileFragment extends Fragment implements ValidateForm {

    private EditText editTextFName, editTextLName, editTextPWord, editTextAd1
            , editTextAd2, editTextAd3, editTextPostCode, editTextEmail, editTextContactNo
            ,editTextConfirmPWord;
    private User results;
    private Button bUpdate;
    private String sName, sSurname, sEmail, sAd1, sAd2, sAd3, sPostCode, sContactNo
            ,sPword, sConfirmPWord;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customer_my_profile, container, false);
        getActivity().setTitle("My Profile");
        setHasOptionsMenu(true);
        editTextFName = (EditText)view.findViewById(R.id.name_cust);
        editTextLName = (EditText)view.findViewById(R.id.surname_cust);
        editTextEmail = (EditText)view.findViewById(R.id.email_cust);
        editTextAd1 = (EditText)view.findViewById(R.id.ad1_cust);
        editTextAd2 = (EditText)view.findViewById(R.id.ad2_cust);
        editTextAd3 = (EditText)view.findViewById(R.id.ad3_cust);
        editTextPostCode = (EditText)view.findViewById(R.id.postCode_cust);
        editTextContactNo = (EditText)view.findViewById(R.id.contact_cust);
        editTextPWord = (EditText)view.findViewById(R.id.pword_cust);
        editTextConfirmPWord = (EditText)view.findViewById(R.id.pWord_confirm_cust);

        bUpdate = (Button) view.findViewById(R.id.update_admin);


        String request = "http://159.203.93.1/v1/getUserDetails";
        DataFromServer process = new DataFromServer();
        process.execute(new String[]{request});

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
      //  User.resetId();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        Log.i("token", Data.token);
    }


    public void populateLayout(){
        editTextFName.setText(results.getFirstname());
        editTextLName.setText(results.getSurname());
        editTextEmail.setText(results.getEmail());
        editTextAd1.setText(results.getAddress_line_1());
        editTextAd2.setText(results.getAddress_line_2());
        editTextAd3.setText(results.getAddress_line_3());
        editTextContactNo.setText(results.getContact_number());
        editTextPostCode.setText(results.getPost_code());

    }

    protected void reportBack(String data) {
        Gson gson = new Gson();
        results = gson.fromJson(data, User.class);
        Log.i("Laravel", "Results" + results);

        if (results.getResponse().equals("failed")) {
            Toast.makeText(getActivity(), "Error: Something went wrong, please try again",
                    Toast.LENGTH_LONG).show();
            // Log.i("Error", results.getError());
        } else {
            populateLayout();

        }

    }

    @Override
    public void validateDetails() {
        sName = editTextFName.getText().toString();
        sSurname = editTextLName.getText().toString();
        sEmail = editTextEmail.getText().toString();
        sAd1 = editTextAd1.getText().toString();
        sAd2 = editTextAd2.getText().toString();
        sAd3 = editTextAd3.getText().toString();
        sPostCode = editTextPostCode.getText().toString();
        sContactNo = editTextContactNo.getText().toString();
        sPword = editTextPWord.getText().toString();
        sConfirmPWord = editTextConfirmPWord.getText().toString();

        if (sName.equals("") || sSurname.equals("") || sEmail.equals("") || sAd1.equals("")
                || sAd2.equals("") || sAd3.equals("") || sPostCode.equals("")|| sContactNo.equals("")
                || sPword.equals("")|| sConfirmPWord.equals("")){
            Toast.makeText(getActivity(), "Please fill in all fields",
                    Toast.LENGTH_LONG).show();
        }else if(!isValidNumber(sContactNo)) {
            Toast.makeText(getActivity(), "Incorrect contact number format",
                    Toast.LENGTH_LONG).show();
        }else if(!isValidPassword(sPword)){
            Toast.makeText(getActivity(), "Incorrect password format",
                    Toast.LENGTH_LONG).show();
        }else if(!sPword.equals(sConfirmPWord)) {
            Toast.makeText(getActivity(), "Passwords do not match",
                    Toast.LENGTH_LONG).show();
        }else{

            String request = "http://159.203.93.1/v1/updateUserDetails";
            SendDataToServer process = new SendDataToServer();
            process.execute(new String[]{request});

        }
    }

    private boolean isValidNumber(String number) {
        String PATTERN = "^[0-9]{11,12}$";

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
    private boolean isValidPassword(String password) {
        String PATTERN = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,}$";

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    private class DataFromServer extends AsyncTask<String, Void, String> {
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

    protected void reportBack2(String data) {
        Gson gson = new Gson();
        results = gson.fromJson(data, User.class);
        Log.i("Laravel", "Results" + results);

        if (results.getResponse().equals("failed")) {
            Toast.makeText(getActivity(), "Error: Something went wrong, please try again",
                    Toast.LENGTH_LONG).show();
            // Log.i("Error", results.getError());
        } else {
            Toast.makeText(getActivity(), "Update Successful",
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
                        .appendQueryParameter("firstname", sName)
                        .appendQueryParameter("lastname", sSurname)
                        .appendQueryParameter("pword", CustomerRegistrationFragment.getPWord())
                        .appendQueryParameter("adL1", CustomerRegistrationFragment.getAd1())
                        .appendQueryParameter("adL2", CustomerRegistrationFragment.getAd2())
                        .appendQueryParameter("adL3", CustomerRegistrationFragment.getAd3())
                        .appendQueryParameter("post_code", CustomerRegistrationFragment.getPostCode())
                        .appendQueryParameter("email", CustomerRegistrationFragment.getEmail())
                        .appendQueryParameter("contact_num", CustomerRegistrationFragment.getContactNo());
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

            reportBack2(s);

        }

    }
}
