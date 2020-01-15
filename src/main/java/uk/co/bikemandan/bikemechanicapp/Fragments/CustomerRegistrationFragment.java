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
import uk.co.bikemandan.bikemechanicapp.BikeRegistrationActivity;
import uk.co.bikemandan.bikemechanicapp.CustomerHomeActivity;
import uk.co.bikemandan.bikemechanicapp.LoginActivity;
import uk.co.bikemandan.bikemechanicapp.Model.Data;
import uk.co.bikemandan.bikemechanicapp.Model.Encryption;
import uk.co.bikemandan.bikemechanicapp.Model.ValidateForm;
import uk.co.bikemandan.bikemechanicapp.Model.User;
import uk.co.bikemandan.bikemechanicapp.R;

/**
 * Created by Emmanuel on 18/03/2016.
 */

public class CustomerRegistrationFragment extends Fragment implements ValidateForm {
    private EditText editTextFName, editTextLName, editTextUserName, editTextPWord, editTextAd1
            , editTextAd2, editTextAd3, editTextPostCode, editTextEmail, editTextContactNo
            ,editTextConfirmPWord;



    public static String fName, lName, userName, pWord, ad1
            , ad2, ad3, postCode, email, contactNo, confirmPWord;

    private Button next;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_registration, container, false);
        getActivity().setTitle("Register");
        setHasOptionsMenu(true);
        editTextFName = (EditText) view.findViewById(R.id.first_name);
        editTextLName = (EditText) view.findViewById(R.id.last_name);

        editTextPWord = (EditText) view.findViewById(R.id.cust_passWord);
        editTextAd1 = (EditText) view.findViewById(R.id.address1);
        editTextAd2 = (EditText) view.findViewById(R.id.address2);
        editTextAd3 = (EditText) view.findViewById(R.id.address3);
        editTextPostCode = (EditText) view.findViewById(R.id.post_code);
        editTextEmail = (EditText) view.findViewById(R.id.eMail);
        editTextContactNo = (EditText) view.findViewById(R.id.contact_number);
        editTextConfirmPWord = (EditText) view.findViewById(R.id.cust_confrimPassWord);


        next = (Button) view.findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDetails();
            }
        });


        //sendDataToServer();

        return view;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }



    @Override
    public void validateDetails(){
        fName = editTextFName.getText().toString();
        lName = editTextLName.getText().toString();

        pWord = editTextPWord.getText().toString();
        ad1 = editTextAd1.getText().toString();
        ad2 = editTextAd2.getText().toString();
        ad3 = editTextAd3.getText().toString();
        postCode = editTextPostCode.getText().toString();
        email = editTextEmail.getText().toString();
        contactNo = editTextContactNo.getText().toString();
        confirmPWord = editTextConfirmPWord.getText().toString();

        if(fName.equals("") || lName.equals("") || pWord.equals("")
                || ad1.equals("") || ad2.equals("") || ad3.equals("") || postCode.equals("")
                || email.equals("") || contactNo.equals("") || confirmPWord.equals("")){
            Toast.makeText(getActivity(), "Please fill in all fields",
                    Toast.LENGTH_LONG).show();

        }else if(!isValidPassword(pWord)){
        Toast.makeText(getActivity(), "Incorrect password format",
                Toast.LENGTH_LONG).show();
        }else if(!pWord.equals(confirmPWord)){
            Toast.makeText(getActivity(), "Passwords do not match",
                    Toast.LENGTH_LONG).show();
        }else if(!isValidNumber(contactNo)) {
            Toast.makeText(getActivity(), "Incorrect contact number format",
                    Toast.LENGTH_LONG).show();
        }
        else{
            Encryption encryption = Encryption.getDefault("YEk", "NaD16", new byte[16]);
            String encrypted = encryption.encryptOrNull(pWord);
            pWord = encrypted;

            Intent i = new Intent(getActivity(), BikeRegistrationActivity.class);
            startActivity(i);
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
    public static String getFName() {
        return fName;
    }

    public static String getLName() {
        return lName;
    }

    public static String getPWord() {
        return pWord;
    }
    public static String getAd1() {
        return ad1;
    }
    public static String getAd2() {
        return ad2;
    }
    public static String getAd3() {
        return ad3;
    }
    public static String getPostCode() {
        return postCode;
    }
    public static String getEmail() {
        return email;
    }
    public static String getContactNo() {
        return contactNo;
    }

    protected void reportBack(String data) {

        Gson gson = new Gson();
        User results = gson.fromJson(data, User.class);
        Log.i("Laravel", "Results" + results);

    }



   private class SendDataToServer extends AsyncTask <String,Void,String>{
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
                        .appendQueryParameter("name", "John");
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









/*

    private class PlacesReadFeed extends AsyncTask<String, Void, User> {
         private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected User doInBackground(String... urls) {
            try {
                //InstanceID instanceID = InstanceID.getInstance(this);
                //_token = Constant.AUTH_TOKEN;
                String input = Connection.readURLRequest(urls[0]);
                  Gson gson = new Gson();
                 User data = gson.fromJson(input, User.class);
                Log.i("PLACES_EXAMPLE", "Number of places found is " + data.getResults().size());
                // Log.i();
                return data;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("PLACES_EXAMPLE", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
          this.dialog.setMessage("Getting nearby places...");
          this.dialog.show();
        }

        @Override
        protected void onPostExecute(User places) {
            this.dialog.dismiss();
            reportBack(places);

        }



    }
*/


}
