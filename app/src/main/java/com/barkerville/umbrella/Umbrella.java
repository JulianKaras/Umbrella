package com.barkerville.umbrella;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Umbrella extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umbrella);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WebServiceTask webserviceTask = new WebServiceTask();
        webserviceTask.execute("Julian!!!");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_umbrella, menu);
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
    private class WebServiceTask extends AsyncTask<String, String, String> {

        //HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        //InputStream in = urlConnection.getInputStream();
        HttpURLConnection urlConnection = null;

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
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/city?id=6058560&APPID=4a0c624b968df9180851beee9fe34be0");
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


