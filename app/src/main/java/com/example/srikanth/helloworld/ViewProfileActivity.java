package com.example.srikanth.helloworld;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

public class ViewProfileActivity extends AppCompatActivity {

    String[] names = {}, descriptions = {};
    List<String> namesList = new ArrayList<>(),
            descList = new ArrayList<>();

    String jsonURL = "https://msitis-iiith.appspot.com/api/profile/ag5ifm1zaXRpcy1paWl0aHIUCxIHU3R1ZGVudBiAgICAgICACgw";
    JsonObjectRequest jsonObjectRequest;
    private static JSONObject data = null;

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

            listView = findViewById(R.id.myList);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Toolbar actionBar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentActivity = this;

        if(data == null) {
            Toast.makeText(this, "Data is null!", Toast.LENGTH_LONG).show();
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

            queue.add(jsonObjectRequest);
        } else {
            parseJSON();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.profile_action_bar, menu);
//        return true;
//    }
}
