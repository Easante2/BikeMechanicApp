package uk.co.bikemandan.bikemechanicapp.Model;

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Emmanuel on 18/03/2016.
 */
public class Connection {

    public static String readURLRequest(String uri) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            URL url = new URL(uri);
            Log.i("EFA", url.toString());
            conn = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            Log.i("EFA", "RESULTS: " + jsonResults);
           // Gson gson = new Gson();
            //GooglePlaceList places = gson.fromJson(input, GooglePlaceList.class);
           // Log.i("PLACES_EXAMPLE", "Number of places found is " + places.getResults().size());
            //  return places;
        } catch (MalformedURLException e) {
            Log.i("Laravel Connect", "Error processing API URL");
            return null;
        } catch (IOException e) {
            Log.i("Laravel Connect", "Error connecting to API");
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
//  return places;
        return jsonResults.toString();
    }
}
