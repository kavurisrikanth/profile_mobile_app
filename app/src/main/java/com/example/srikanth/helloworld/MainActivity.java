package com.example.srikanth.helloworld;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    String[] names = {"Name", "Field of study", "University"},
//             descriptions = {"Srikanth", "Computer Science", "IIIT-Hyderabad"};

    String[] names = {}, descriptions = {};
    List<String> namesList = new ArrayList<>(),
                      descList = new ArrayList<>();

    String jsonURL = "https://msitis-iiith.appspot.com/api/profile/ag5ifm1zaXRpcy1paWl0aHIUCxIHU3R1ZGVudBiAgICAgICACgw";
    JsonObjectRequest jsonObjectRequest;

    ListView listView;
    Activity currentActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentActivity = this;

        RequestQueue queue = Volley.newRequestQueue(this);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                jsonURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        try {
                            JSONObject data = response.getJSONArray("data").getJSONObject(0);
                            String fullName = data.getString("student_fullname");
                            System.out.println("Full name: " + fullName);

                            Iterator<String> iterator = data.keys();
                            while (iterator.hasNext()) {
                                String currentKey = iterator.next(),
                                        currentVal = data.getString(currentKey);
                                if(!currentVal.isEmpty()) {
                                    namesList.add(currentKey);
                                    descList.add(currentVal);
                                }
                            }

                            names = namesList.toArray(names);
                            descriptions = descList.toArray(descriptions);

                            CustomListAdapter adapter = new CustomListAdapter(currentActivity, names, descriptions);

                            listView = (ListView)findViewById(R.id.myList);
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println("Error fetching JSON: " + error.toString());
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }
}

