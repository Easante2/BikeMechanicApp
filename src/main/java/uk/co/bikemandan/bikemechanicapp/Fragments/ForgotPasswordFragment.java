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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.bikemandan.bikemechanicapp.LoginActivity;
import uk.co.bikemandan.bikemechanicapp.Model.User;
import uk.co.bikemandan.bikemechanicapp.R;

/**
 * Created by Emmanuel on 20/04/2016.
 */
public class ForgotPasswordFragment extends Fragment {
    private EditText editTextUserName, editTextPWord
            ,editTextConfirmPWord;
    private Button btnConfirm;
    private String userName, pWord, confirmPWord;
    private List<User> user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        getActivity().setTitle("Reset Password");
        setHasOptionsMenu(true);
        editTextUserName = (EditText) view.findViewById(R.id.fp_username);
        editTextPWord = (EditText) view.findViewById(R.id.fp_password);
        editTextConfirmPWord = (EditText) view.findViewById(R.id.fp_confirm_pword);

        btnConfirm = (Button) view.findViewById(R.id.fp_confirm_btn);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDetails();
            }
        });
        return view;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void validateDetails(){
        userName = editTextUserName.getText().toString();
        pWord = editTextPWord.getText().toString();
        confirmPWord = editTextConfirmPWord.getText().toString();
       /* if( userName.equals("") || pWord.equals("") || confirmPWord.equals("")){
            Toast.makeText(getActivity(), "Please fill in all fields",
                    Toast.LENGTH_LONG).show();

        }else if(!isValidPassword(pWord)){
            Toast.makeText(getActivity(), "Incorrect password format",
                    Toast.LENGTH_LONG).show();
        }else if(!pWord.equals(confirmPWord)){
            Toast.makeText(getActivity(), "Passwords do not match",
                    Toast.LENGTH_LONG).show();
        }else{
         String request = "http://159.203.93.1/forgotPassword";
                SendDataToServer process = new SendDataToServer();
                process.execute(new String[]{request});

        }*/
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
        User results = gson.fromJson(data, User.class);
        Log.i("Laravel", "Results" + results);

        //user = results.getResults();

        if(results.equals("success")){
            Toast.makeText(getActivity(), "Password successfully changed",
                    Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);

        }else{
            Toast.makeText(getActivity(), "Error: Something went wrong, please try again",
                    Toast.LENGTH_LONG).show();
            Log.i("Error", user.get(0).getError());
        }

    }

    private class SendDataToServer extends AsyncTask<String,Void,String> {
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
                        .appendQueryParameter("username", userName)
                        .appendQueryParameter("pword", pWord);
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
