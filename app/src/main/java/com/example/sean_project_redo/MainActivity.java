package com.example.sean_project_redo;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView tvCountry, tvCity, tvTemperature, tvDateTime;
    private TextView tvLatitude, tvLongitude, tvHumidity, tvSunrise, tvSunset, tvPressure, tvWindSpeed;
    private EditText editCityName;
    private ImageView ivWeatherIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCountry = findViewById(R.id.tvCountry);
        tvCity = findViewById(R.id.tvCity);
        tvTemperature = findViewById(R.id.tvTemperature);
        editCityName = findViewById(R.id.editCityName);
        Button btnEnter = findViewById(R.id.btnEnter);
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvPressure = findViewById(R.id.tvPressure);
        tvWindSpeed = findViewById(R.id.tvWindspeed);
        tvDateTime = findViewById(R.id.tvDateTime);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);

        // Gets weather information of specified city when user clicks enter button
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeatherInfo();
            }
        });
    }

    // Gets weather information from OpenWeatherMap API
    public void getWeatherInfo() {

        String city = editCityName.getText().toString();
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=93e998d9465496a05b8f9c2c0f1b44b3";

        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Sets required key from appropriate JSON object to correlating text view
                try {

                    JSONObject jsonObject  = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("sys");

                    // Gets country
                    String getCountry = jsonObject1.getString("country");
                    tvCountry.setText(getCountry);

                    // Gets city
                    String getCity = jsonObject.getString("name");
                    tvCity.setText(getCity);

                    // Gets temperature
                    JSONObject jsonObject2 = jsonObject.getJSONObject("main");
                    int getTemperature = jsonObject2.getInt("temp");
                    tvTemperature.setText(getTemperature - 273 + "°C"); // Converts kelvin to celsius

                    // Gets weather icon
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject jsonObject3 = jsonArray.getJSONObject(0);
                    String weatherIcon = jsonObject3.getString("icon");
                    Picasso.get().load("http://openweathermap.org/img/wn/"+weatherIcon+"@2x.png").into(ivWeatherIcon);

                    // Gets latitude
                    JSONObject jsonObject4 = jsonObject.getJSONObject("coord");
                    double getLatitude = jsonObject4.getDouble("lat");
                    tvLatitude.setText(getLatitude + "° N");

                    // Gets longitude
                    JSONObject jsonObject5 = jsonObject.getJSONObject("coord");
                    double getLongitude = jsonObject5.getDouble("lon");
                    tvLongitude.setText(getLongitude + "° E");

                    // Gets humidity
                    JSONObject jsonObject6 = jsonObject.getJSONObject("main");
                    int getHumidity = jsonObject6.getInt("humidity");
                    tvHumidity.setText(getHumidity + " %");

                    // Gets pressure
                    JSONObject jsonObject9 = jsonObject.getJSONObject("main");
                    String getPressure = jsonObject9.getString("pressure");
                    tvPressure.setText(getPressure + " hPa");

                    // Gets wind speed
                    JSONObject jsonObject10 = jsonObject.getJSONObject("wind");
                    String getWindSpeed = jsonObject10.getString("speed");
                    tvWindSpeed.setText(getWindSpeed + " km/h");

                    // Gets date and time
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy \nHH:mm:ss");
                    String formattedDateTime = simpleDateFormat.format(calendar.getTime());
                    tvDateTime.setText(formattedDateTime);

                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }
}
