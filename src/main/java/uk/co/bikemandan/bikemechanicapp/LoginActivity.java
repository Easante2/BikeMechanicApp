package uk.co.bikemandan.bikemechanicapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;

import uk.co.bikemandan.bikemechanicapp.Model.Bike;
import uk.co.bikemandan.bikemechanicapp.Model.Data;
import uk.co.bikemandan.bikemechanicapp.Model.Encryption;
import uk.co.bikemandan.bikemechanicapp.Model.User;

public class LoginActivity extends AppCompatActivity {
    private Button register, login;
    private TextView tvForgetPass;
    private EditText editTextEmail, editTextPword;
    private String email, pWord;

    private List<Bike> bike;
    public  User user1;
    public Data mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.login_username);
        editTextPword = (EditText) findViewById(R.id.login_password);
        tvForgetPass = (TextView)findViewById(R.id.forgetPass);
        login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDetails();

            }
        });

        register = (Button)findViewById(R.id.login_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent= new Intent(getApplicationContext(), CustomerRegistrationActivity.class);
                startActivity(intent);
            }
        });


        String str="Forgot Password";
        SpannableString content = new SpannableString(str);
        content.setSpan(new UnderlineSpan(), 0, str.length(), 0);
        tvForgetPass.setText(content);
        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(i);
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void validateDetails(){
        email = editTextEmail.getText().toString();
        pWord = editTextPword.getText().toString();

        if( email.equals("") || pWord.equals("")){
            Toast.makeText(getApplicationContext(), "Please fill in login fields",
                    Toast.LENGTH_LONG).show();
        }else{

            Encryption encryption = Encryption.getDefault("YEk", "NaD16", new byte[16]);
            String encrypted = encryption.encryptOrNull(pWord);
            pWord = encrypted;
         String request = "http://159.203.93.1/v1/userLogin";
                SendDataToServer process = new SendDataToServer();
                process.execute(new String[]{request});

        }
    }

    protected void reportBack(String data) {

        Gson gsonToken = new Gson();
        Data tokenData = gsonToken.fromJson(data, Data.class);
        Data.setToken(tokenData.getTok());
        Log.i("Laravel", "DataToken" + Data.getToken());
        Gson gson = new Gson();
        user1 = gson.fromJson(data, User.class);
        User.setId(user1.getID_());

         Log.i("Laravel", "Id" + User.getId());


/*
        User.setsId(user.getId());
        Log.i("Laravel", "Id" + user.getId());
        Log.i("Laravel", "SID" + User.getsId());*/


        if (user1.getResponse().equals("success")) {
            Toast.makeText(this, "Login successful!",
                    Toast.LENGTH_LONG).show();
            if (user1.getIsAdmin() == 0) {
                Intent a = new Intent(LoginActivity.this, CustomerHomeActivity.class);
                startActivity(a);
                Log.i("token", Data.token);

            } else if (user1.getIsAdmin() == 1) {
                Intent i = new Intent(getApplicationContext(), AdminHomeActivity.class);
                startActivity(i);
            }
        } else {
            Toast.makeText(this, "Wrong login details, please try again",
                    Toast.LENGTH_LONG).show();
           // Log.i("Error", user.getError());
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
                        .appendQueryParameter("id", String.valueOf(66))
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("password", pWord);
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
