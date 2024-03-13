package com.example.vrijeme;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Button buttonGetWeather;
    private TextView textViewWeatherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        buttonGetWeather = findViewById(R.id.buttonGetWeather);
        textViewWeatherInfo = findViewById(R.id.textViewWeatherInfo);

        buttonGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString();
                new GetWeatherTask().execute(city);
            }
        });
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            String apiKey = "abccd6308c0881d1de3b63c058cf3f0d";
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        //Huskvarha Buenos Aires Zagreb Ulm Rifle
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject json = new JSONObject(result);

                    // Extract weather information
                    String weatherDescription = json.getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("description");

                    // Extract temperature in Kelvin
                    double temperatureKelvin = json.getJSONObject("main").getDouble("temp");

                    // Convert Kelvin to Celsius
                    double temperatureCelsius = temperatureKelvin - 273.15;

                    // Display temperature and weather description
                    String temperature = String.format("%.2f", temperatureCelsius) + " °C";
                    String weatherInfo = "Weather: " + weatherDescription + "\nTemperature: " + temperature;
                    textViewWeatherInfo.setText(weatherInfo);

                    // Display corresponding weather icon


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}