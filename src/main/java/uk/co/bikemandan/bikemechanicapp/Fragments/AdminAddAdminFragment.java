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
import uk.co.bikemandan.bikemechanicapp.CustomerRegistrationActivity;
import uk.co.bikemandan.bikemechanicapp.LoginActivity;
import uk.co.bikemandan.bikemechanicapp.Model.Data;
import uk.co.bikemandan.bikemechanicapp.Model.Encryption;
import uk.co.bikemandan.bikemechanicapp.Model.User;
import uk.co.bikemandan.bikemechanicapp.Model.ValidateForm;
import uk.co.bikemandan.bikemechanicapp.R;

/**
 * Created by Emmanuel on 26/04/2016.
 */
public class AdminAddAdminFragment extends Fragment implements ValidateForm {
    private EditText editTextFName, editTextLName, editTextPWord, editTextAd1, editTextAd2, editTextAd3, editTextPostCode, editTextEmail, editTextContactNo, editTextConfirmPWord;

    public String admin_fName, admin_lName, admin_pWord, admin_ad1, admin_ad2, admin_ad3, admin_postCode, admin_email, admin_contactNo, admin_confirmPWord;
    public Button btnConfirm;
    private User results;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_admin, container, false);
        getActivity().setTitle("My Profile");
        setHasOptionsMenu(true);

        editTextFName = (EditText) view.findViewById(R.id.name_add_admin);
        editTextLName = (EditText) view.findViewById(R.id.surname_add_admin);
        editTextPWord = (EditText) view.findViewById(R.id.pword_add_admin);
        editTextAd1 = (EditText) view.findViewById(R.id.ad1_add_admin);
        editTextAd2 = (EditText) view.findViewById(R.id.ad2_add_admin);
        editTextAd3 = (EditText) view.findViewById(R.id.ad3_add_admin);
        editTextPostCode = (EditText) view.findViewById(R.id.postCode_add_admin);
        editTextEmail = (EditText) view.findViewById(R.id.email_add_admin);
        editTextContactNo = (EditText) view.findViewById(R.id.contact_add_admin);
        editTextConfirmPWord = (EditText) view.findViewById(R.id.pWord_confirm_add_admin);

        btnConfirm = (Button) view.findViewById(R.id.update_add_admin);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        User.setId("");
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }


    @Override
    public void validateDetails() {
        admin_fName = editTextFName.getText().toString();
        admin_lName = editTextLName.getText().toString();
        admin_pWord = editTextPWord.getText().toString();
        admin_ad1 = editTextAd1.getText().toString();
        admin_ad2 = editTextAd2.getText().toString();
        admin_ad3 = editTextAd3.getText().toString();
        admin_postCode = editTextPostCode.getText().toString();
        admin_email = editTextEmail.getText().toString();
        admin_contactNo = editTextContactNo.getText().toString();
        admin_confirmPWord = editTextConfirmPWord.getText().toString();

        if (admin_fName.equals("") || admin_lName.equals("") || admin_pWord.equals("")
                || admin_ad1.equals("") || admin_ad2.equals("") || admin_ad3.equals("") || admin_postCode.equals("")
                || admin_email.equals("") || admin_contactNo.equals("") || admin_confirmPWord.equals("")) {
            Toast.makeText(getActivity(), "Please fill in all fields",
                    Toast.LENGTH_LONG).show();
        } else if (!isValidPassword(admin_pWord)) {
            Toast.makeText(getActivity(), "Incorrect password format",
                    Toast.LENGTH_LONG).show();
        } else if (!admin_pWord.equals(admin_confirmPWord)) {
            Toast.makeText(getActivity(), "Passwords do not match",
                    Toast.LENGTH_LONG).show();
        } else if (!isValidNumber(admin_contactNo)) {
            Toast.makeText(getActivity(), "Incorrect contact number format",
                    Toast.LENGTH_LONG).show();
        } else {

            Encryption encryption = Encryption.getDefault("YEk", "NaD16", new byte[16]);
            String encrypted = encryption.encryptOrNull(admin_pWord);
            admin_pWord = encrypted;
            String request = "http://159.203.93.1/v1/addAdmin";
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


    protected void reportBack(String data) {
        Gson gson = new Gson();
        results = gson.fromJson(data, User.class);
        Log.i("Laravel", "Results" + results);

        if (results.getResponse().equals("failed")) {
            Toast.makeText(getActivity(), "Error: Something went wrong, please try again",
                    Toast.LENGTH_LONG).show();
            // Log.i("Error", results.getError());
        } else {
            Toast.makeText(getActivity(), "Admin has been successfully added",
                    Toast.LENGTH_LONG).show();

        }

    }
    private class SendDataToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonResults = new StringBuilder();
            HttpURLConnection urlConnection = null;

            try {
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
                        .appendQueryParameter("firstname", admin_fName)
                        .appendQueryParameter("id", User.getId())
                        .appendQueryParameter("lastname", admin_lName)
                        .appendQueryParameter("email", admin_email)
                        .appendQueryParameter("pword", admin_pWord)
                        .appendQueryParameter("adL1", admin_ad1)
                        .appendQueryParameter("adL2", admin_ad2)
                        .appendQueryParameter("adL3", admin_ad3)
                        .appendQueryParameter("post_code", admin_postCode)
                        .appendQueryParameter("contact_num", admin_contactNo);
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
