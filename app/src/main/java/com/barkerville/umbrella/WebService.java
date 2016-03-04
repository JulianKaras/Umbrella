package com.barkerville.umbrella;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.barkerville.umbrella.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JulianK on 03/03/2016.
 */
public class WebService extends AsyncTask <String, String, String> {
    //HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    //InputStream in = urlConnection.getInputStream();
    HttpURLConnection urlConnection = null;

    public void getWeather(Context) {

        onPostExecute();
        doInBackground();
        useUmbrella();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //TextView textview = (TextView) findViewById(R.id.hello_world);
        TextView textview = (TextView)findViewById(R.id.use_umbrella);
        textview.setText("Should I take an umbrella today?" +s);
    }

    @Override
    protected String doInBackground(String... params) {
        String useUmbrellaStr = "Don't know, sorry about that.";
        //HttpURLConnection urlConnection = null;

        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/weather?lat="+params[0]+"&lon="
                    +params[1]+"&APPID=4a0c624b968df9180851beee9fe34be0");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            useUmbrellaStr = useUmbrella(urlConnection.getInputStream());
        } catch (IOException e) {
            Log.e("MainActivity", "Error", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return useUmbrellaStr;

        // return "Hello " + params[0];
    }
    protected String useUmbrella(InputStream in){
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        //read and parse InputStream
        //return "Yes"; //temporary
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = bufferedReader.readLine())!= null) {
                stringBuilder.append(line + "\n");
            }
            JSONObject forecastJson = new JSONObject(stringBuilder.toString());
            JSONArray weatherArray = forecastJson.getJSONArray("list");
            JSONObject todayForecast = weatherArray.getJSONObject(0);
            Log.i("Returned data",stringBuilder.toString());

            if (todayForecast.has("rain") || todayForecast.has("snow")) {
                return("Yes");
            } else {
                return("No");
            }

        }catch (Exception e) { Log.e("MainActivity", "Error", e);
        } finally {
            if(bufferedReader != null){
                try{
                    bufferedReader.close();
                } catch (final IOException e){
                    Log.e("PlaceholderFragment", "Error closing stream", e );
                }
            }
        }   return "Don't know, sorry about that.";
    }
}

}
