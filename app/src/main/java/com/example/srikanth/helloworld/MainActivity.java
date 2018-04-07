package com.example.srikanth.helloworld;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
    JSONObject data = null;

    ListView listView;
    Activity currentActivity;

    public JSONObject getData() {
        return data;
    }

    private void parseJSON() {
        try {
            Iterator<String> iterator = data.keys();
            while (iterator.hasNext()) {
                String currentKey = iterator.next(),
                        currentVal = data.getString(currentKey);

//                                System.out.println(currentKey + " -> " + currentVal);
//                                System.out.println(currentVal.equals("null") ? "Value is ACTUALLY \"null\"!\n" : "");

                if (!currentVal.isEmpty() && !currentVal.equals("null")) {
                    if (currentKey.equals("image")) {
                        ImageView myImage = findViewById(R.id.myPhotoID);

                        byte[] photoBytes = currentVal.getBytes("ISO-8859-1");

                        String b64 = Base64.encodeToString(photoBytes, Base64.DEFAULT);
                        byte[] workingImageBytes = Base64.decode(b64, Base64.DEFAULT);

                        Bitmap workingImage = BitmapFactory.decodeByteArray(workingImageBytes, 0, workingImageBytes.length);
                        myImage.setImageBitmap(workingImage);
                    } else {
                        String formattedKey = "";
                        String[] keyPieces = currentKey.split("_");
                        for (String piece : keyPieces) {
                            formattedKey += piece.substring(0, 1).toUpperCase() + piece.substring(1) + " ";
                        }
                        namesList.add(formattedKey.trim());
                        descList.add(currentVal);
                    }
                }
            }

            names = namesList.toArray(names);
            descriptions = descList.toArray(descriptions);

            CustomListAdapter adapter = new CustomListAdapter(currentActivity, names, descriptions);

            listView = (ListView) findViewById(R.id.myList);
            listView.setAdapter(adapter);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void handleJSONData(JSONObject response) {
        try {
            data = response.getJSONArray("data").getJSONObject(0);
            parseJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentActivity = this;

        if(data == null) {
            RequestQueue queue = Volley.newRequestQueue(this);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    jsonURL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                        System.out.println("Response: " + response.toString());
                            handleJSONData(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.err.println("Error fetching JSON: " + error.toString());
                        }
                    }
            );
//            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);
        } else {
            parseJSON();
        }
    }
}

