package com.integrationlabs.results;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class details extends AppCompatActivity{

    TextView result,dateOfBirth;
    ImageView captchaView;
    Button login;
    EditText captcha,registrationNumber;
    private int mYear, mMonth, mDay;
    ProgressBar progressBar;
    LinearLayout linearLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        result = findViewById(R.id.result);
        captchaView = findViewById(R.id.captchaView);
        login = findViewById(R.id.login);
        captcha = findViewById(R.id.verificationCode);
        progressBar = findViewById(R.id.progressBar);
        linearLayout2 = findViewById(R.id.linearLayout2);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        registrationNumber = findViewById(R.id.registrationNumber);

        //<!--hiding navigation bar->>
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //code ends for hide_navigation

        SharedPreferences sharedPreferences = getSharedPreferences("Details", MODE_PRIVATE);

        String reg = sharedPreferences.getString("reg","");
        String dob = sharedPreferences.getString("dob","Select Date Of Birth");

        registrationNumber.setText(reg);
        dateOfBirth.setText(dob);


        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(details.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String temp = "";
                                String temp_1 = "";

                                if(String.valueOf(dayOfMonth).length() ==  1){
                                    temp = "0"+String.valueOf(dayOfMonth);
                                }
                                else{
                                    temp = String.valueOf(dayOfMonth);
                                }

                                if(String.valueOf(monthOfYear+1).length() ==  1){
                                    temp_1 = "0"+String.valueOf(monthOfYear+1);
                                }
                                else{
                                    temp_1 = String.valueOf(monthOfYear+1);
                                }


                                dateOfBirth.setText(year + "-" + temp_1 + "-" + temp);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        getWebsite();
    }
    private void getWebsite() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    final String strActionURL = "https://evarsity.srmist.edu.in/srmwebonline/exam/onlineResult.jsp";

                    final Connection.Response loginPageResponse = Jsoup.connect(strActionURL)
                            .userAgent("Mozilla/5.0")
                            .timeout(10 * 1000)
                            .followRedirects(true)
                            .execute();

                    final Map<String, String> mapLoginPageCookies = loginPageResponse.cookies();

                    final Document d = Jsoup.connect(strActionURL)
                            .referrer(strActionURL)
                            .userAgent("Mozilla/5.0")
                            .followRedirects(true)
                            .timeout(10 * 1000)
                            .cookies(mapLoginPageCookies)
                            .get();

                    Element img = d.select("#searchfilter > table > tbody > tr > td > table > tbody > tr:nth-child(4) > td:nth-child(4) > img").first();
                    final Connection.Response response = Jsoup.connect(img.absUrl("src")) // Extract image absolute URL
                            .cookies(mapLoginPageCookies) // Grab cookies
                            .ignoreContentType(true) // Needed for fetching image
                            .execute();
                    final Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(response.bodyAsBytes()));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            captchaView.setVisibility(View.VISIBLE);
                            captchaView.setImageBitmap(bitmap);
                        }
                    });


                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {


                                    try {
                                        final String cap = cal();
                                        final String reg = regisCall();
                                        final String fullDob = full();


                                        if(!cap.equals("") && !reg.equals("") && !fullDob.equals(""))
                                        {


                                                    final Dialog dialog =  new Dialog(details.this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                    dialog.setContentView(R.layout.loading);
                                                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                                                    dialog.show();

                                                    String[] temp = fullDob.split("-");
                                                    System.out.println(Arrays.toString(temp));

                                                    final Map<String, String> mapParams = new HashMap<String, String>();
                                                    mapParams.put("frmdate", fullDob);
                                                    mapParams.put("iden", "1");
                                                    mapParams.put("txtRegisterno", reg);
                                                    mapParams.put("txtFromDate", temp[2]);
                                                    mapParams.put("selMonth", temp[1]);
                                                    mapParams.put("txtYear", temp[0]);
                                                    mapParams.put("txtvericode", cap);
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Document responsePostLogin = null;
                                                            try {
                                                                responsePostLogin = Jsoup.connect(strActionURL)
                                                                        .referrer(strActionURL)
                                                                        .userAgent("Mozilla/5.0")
                                                                        .timeout(10 * 1000)
                                                                        .data(mapParams)
                                                                        .cookies(mapLoginPageCookies)
                                                                        .followRedirects(true)
                                                                        .post();
                                                                dialog.dismiss();
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }



                                                            SharedPreferences sharedPreferences = getSharedPreferences("Details", MODE_PRIVATE);
                                                            SharedPreferences.Editor editor =  sharedPreferences.edit();
                                                            editor.putString("reg",reg);
                                                            editor.putString("dob", fullDob);
                                                            editor.apply();

                                                            String registration = responsePostLogin.select("#divResult > table:nth-child(2) > tbody > tr:nth-child(2) > td:nth-child(2)").text();
                                                            if(!registration.equals("")){
                                                                Intent i = new Intent(details.this, display_result.class);
                                                                String response = String.valueOf(responsePostLogin);
                                                                i.putExtra("responsePostLogin", response);
                                                                startActivity(i);

                                                            }
                                                            else{
                                                                Intent i = new Intent(details.this, ErrorPage.class);
                                                                startActivity(i);

                                                            }



                                                        }
                                                    }).start();


                                        }
                                        else{

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast toast = Toast.makeText(details.this,
                                                            "Enter The Full Details",
                                                            Toast.LENGTH_SHORT);

                                                    toast.show();
                                                }
                                            });
                                        }
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }

                                        }
                                    });
                                }

                            }).start();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private String cal() {
        String cap = captcha.getText().toString();
        return cap;
    }
    private String regisCall() {
        String reg = registrationNumber.getText().toString();
        return reg;
    }
    private  String full(){
        String date = dateOfBirth.getText().toString();
        return date;
    }


    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
    }
}
