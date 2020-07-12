package com.integrationlabs.results;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class display_result extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<User> mUploads;
    private ResultAdapter mAdapter;
    TextView name,dob,reg,program,college;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        reg = findViewById(R.id.reg);
        program = findViewById(R.id.program);
        college = findViewById(R.id.college);
        mRecyclerView = findViewById(R.id.rec);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        String response = null;;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            response = bundle.getString("responsePostLogin");
        }

        final Document responsePostLogin = Jsoup.parse(response);
        final String temp_college = responsePostLogin.select("#divResult > table:nth-child(2) > tbody > tr:nth-child(4) > td:nth-child(2)").text();
        final String temp_prog = responsePostLogin.select("#divResult > table:nth-child(2) > tbody > tr:nth-child(3) > td:nth-child(2)").text();
        final String registration = responsePostLogin.select("#divResult > table:nth-child(2) > tbody > tr:nth-child(2) > td:nth-child(2)").text();
        final String[] nameDob = responsePostLogin.select("#divResult > table:nth-child(2) > tbody > tr:nth-child(1) > td:nth-child(2)").text().split(" ");
        String temp = "";
        for(int i = 0; i<nameDob.length;i++){
            if(!nameDob[i].equals("[")){
                temp += nameDob[i] + " ";
                nameDob[i] = "";
            }
            else{
                break;
            }
        }
        String temp_1 = "";
        for(int i = 0;i<nameDob.length;i++){
            if(!nameDob[i].equals("")) {

                temp_1 += nameDob[i];
            }

        }
        final String finalTemp_ = temp_1;
        System.out.println(Arrays.toString(nameDob));
        final String finalTemp = temp;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name.setText(finalTemp);
                dob.setText(finalTemp_);
                reg.setText(registration);
                program.setText(temp_prog);
                college.setText(temp_college);
            }
        });

        Elements table = responsePostLogin.select("#table1 > tbody"); //select the first table.
        Elements rows = table.select("tr");

        for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
            Element row = rows.get(i);
            Elements cols = row.select("td");
            ArrayList<String> temp_array = new ArrayList<>();

            for (int j=1; j < cols.size();j++) {
                if (!cols.get(j).text().equals("")) {
                    temp_array.add(cols.get(j).text());
                }
            }
            //[7, 15CS401, ARTIFICIAL INTELLIGENCE, 3, 87, 100, A, PASS]

            String courseName = temp_array.get(1) +"-"+ temp_array.get(2);
            String semester = temp_array.get(0);
            String credits = temp_array.get(3);
            String marks = temp_array.get(4)+"/"+temp_array.get(5);
            String grade = temp_array.get(6);
            String result = temp_array.get(7);
            User upload = new User(courseName,semester,credits,marks,grade,result);
            mUploads.add(upload);
            mAdapter = new ResultAdapter(display_result.this,mUploads);
            mRecyclerView.setAdapter(mAdapter);
        }

    }
}
