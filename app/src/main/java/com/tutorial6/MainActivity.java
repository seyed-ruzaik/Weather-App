package com.tutorial6;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    protected TextView myDate, myCity, myTemp, w_description,
            time_sunrise,time_sunset,feels_like_temp;
    protected ImageView weather_icon;
    protected ImageButton submit;
    protected EditText editText;
    protected Button displayData;
    protected  String date;
    protected String time;
    protected String weather;
    protected static String tem;
    protected DatabaseHelper myDb;
    protected String imageUrl;

    @SuppressLint("StaticFieldLeak")
    class getWeather extends AsyncTask<String , Void , String>{


        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result  = new StringBuilder();

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                BufferedReader reader  = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";

                while ((line = reader.readLine()) != null){
                    result.append(line).append("\n");

                }
                System.out.println("Back "+result.toString());
                return result.toString();


            }catch (Exception e){
                e.printStackTrace();
                return null;

            }

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {



                JSONObject jsonObject = new JSONObject(result);
                String info = jsonObject.getString("main");

                JSONObject jb = new JSONObject(info);
                String temperature = jb.getString("temp");
                JSONObject jb_2 = new JSONObject(info);
                double value4 = Double.parseDouble(temperature);
                int roundVal5= (int) Math.round(value4);
                String my_temp5 = String.valueOf(roundVal5);
                String feels2 = jb_2.getString("feels_like");
                double value3 = Double.parseDouble(feels2);
                int roundVal3= (int) Math.round(value3);
                String my_temp3 = String.valueOf(roundVal3);
                JSONArray array2 = jsonObject.getJSONArray("weather");
                JSONObject object2 = array2.getJSONObject(0);
                String descrip2 = object2.getString("description");
                System.out.println("MY COUNT "+count);
                if (count == 1){
                    editText.setText("London");
                }
                weather = "Weather Temperature : " + my_temp5+"℃"+"\n" +
                        "Feels Like : "+my_temp3+" ℃"+"\n" +
                        "Weather Description : "+descrip2.toUpperCase()+"\n" +
                        "City : "+editText.getText().toString();
                boolean isInserted = myDb.insertData(date, time,
                        weather);

                    editText.setText("");
                JSONObject jb2 = new JSONObject(info);
                String feels = jb2.getString("feels_like");

                double value2 = Double.parseDouble(feels);
                int roundVal2= (int) Math.round(value2);
                String my_temp2 = String.valueOf(roundVal2);

                String timeZ = jsonObject.getString("timezone");
                System.out.println("Time Zone is "+timeZ);


                JSONObject sys = jsonObject.getJSONObject("sys");
                String sunrise = sys.getString("sunrise");
                String sunset = sys.getString("sunset");


                long unixTimestamp = Long.parseLong(sunrise);
                long unixTimestamp2 = Long.parseLong(sunset);

                long javaTimestamp = unixTimestamp * 1000L;
                long javaTimestamp2 = unixTimestamp2 * 1000L;

                Date date = new Date(javaTimestamp);
                Date date2 = new Date(javaTimestamp2);


                String sunrise2 = new SimpleDateFormat("h:mm a", Locale.ENGLISH).format(date);
                String sunset3 = new SimpleDateFormat("h:mm a", Locale.ENGLISH).format(date2);


                time_sunrise.setText("Sun Rise : \n"+sunrise2);
                time_sunset.setText("Sun Set : \n"+sunset3);

                feels_like_temp.setText("Feels Like : "+my_temp2+" ℃");
                JSONArray array = jsonObject.getJSONArray("weather");
                JSONObject object = array.getJSONObject(0);
                String descrip = object.getString("description");
                String icon = object.getString("icon");

                String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
                ImageView image = findViewById(R.id.imageView);
                Picasso.with(MainActivity.this).load(iconUrl).into(image);
                w_description.setText(descrip);




                double value = Double.parseDouble(temperature);
                        int roundVal= (int) Math.round(value);
                        String my_temp = String.valueOf(roundVal);

                        myTemp.setText(my_temp+"℃");



            } catch (Exception e) {
                e.printStackTrace();
            }




        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    protected int count = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDate = findViewById(R.id.textView_date);
        myCity = findViewById(R.id.textView_city);
        myTemp = findViewById(R.id.textView_temp);
        w_description = findViewById(R.id.textView_description);
        weather_icon = findViewById(R.id.imageView);
        feels_like_temp = findViewById(R.id.textView_feelslike);
        submit = findViewById(R.id.btn_submit);
        editText = findViewById(R.id.editText_cityname);
        displayData = findViewById(R.id.btn_display);

        time_sunrise = findViewById(R.id.textView_sunrise);
        time_sunset = findViewById(R.id.textView_sunset);
        myDb = new DatabaseHelper(this);

        myTemp.setMovementMethod(new ScrollingMovementMethod());
        final String[] tempo = {""};



        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty()) {
                    String city = editText.getText().toString();
                    String urls = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=31abbf554f42f6c83db14e0a0e1fb54d&units=metric";
                    getWeather task = new getWeather();
                    try {
                        date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
                        time = new SimpleDateFormat("HH:mm:ss").format(new Date());

                        count++;
                        tempo[0] = task.execute(urls).get();
                        myCity.setText(editText.getText().toString().toUpperCase());
                        Calendar calendar = Calendar.getInstance();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =
                                new SimpleDateFormat("EEEE-MM-dd");
                        String formattedDate = sdf.format(calendar.getTime());
                        myDate.setText(formattedDate);

                        System.out.println("Temper is " + tem);

                        viewAll();

                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (tempo[0] == null) {
                        Toast.makeText(MainActivity.this, "Weather Information!\nNot Available!",
                                Toast.LENGTH_LONG).show();
                        count = 0;
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
            }
        });

      if (count == 0){
          editText.setText("London");
          submit.performClick();
          editText.setText("");


      }
    }


    public void viewAll() {
       displayData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Date :"+ res.getString(0)+"\n");
                            buffer.append("Time :"+ res.getString(1)+"\n");
                            buffer.append("Weather :"+ res.getString(2)+"\n\n");
                        }

                        // Show all data
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }



}