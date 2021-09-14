package com.example.test;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User extends AppCompatActivity {

    private static final String TAG = "User Activity";
    TextView tvName, tvEmail, tvPhone, tvAge, tvGender, tvCity, tvAddress, tvNationality, tvPincode, tvState, tvDob;
    ImageView tvImage;
    String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        Token = intent.getStringExtra("Token");

        tvName = findViewById(R.id.usName);
        tvEmail = findViewById(R.id.usEmail);
        tvPhone = findViewById(R.id.usPhone);
        tvAge = findViewById(R.id.usAge);
        tvGender = findViewById(R.id.usGender);
        tvCity = findViewById(R.id.usCity);
        tvAddress = findViewById(R.id.usAddress);
        tvNationality = findViewById(R.id.usNationality);
        tvPincode = findViewById(R.id.usPincode);
        tvState = findViewById(R.id.usState);
        tvDob = findViewById(R.id.usDob);
        tvImage = findViewById(R.id.usImage);

        Log.d(TAG, "onCreate: "+Token);

        getUser();
    }

    private void getUser() {

        String url = "https://api.mypharmacy4u.com/api/user";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            JSONObject jsonObject = response.getJSONObject("data");

                            tvName.setText("Name:- "+jsonObject.getString("user_name"));
                            tvEmail.setText("Email:- "+jsonObject.getString("user_email"));
                            tvPhone.setText("Phone:- "+jsonObject.getString("user_contact"));
                            tvAge.setText("Age:- "+jsonObject.getString("user_age"));
                            tvGender.setText("Gender:- "+jsonObject.getString("user_gender"));
                            tvCity.setText("City:- "+jsonObject.getString("user_city"));
                            tvAddress.setText("Address:- "+jsonObject.getString("user_address"));
                            tvNationality.setText("Nationality:- "+jsonObject.getString("user_nationality"));
                            tvPincode.setText("Pincode:- "+jsonObject.getString("user_pincode"));
                            tvState.setText("State:- "+jsonObject.getString("user_state"));
                            tvDob.setText("DOB:- "+jsonObject.getString("user_dob"));

                            Glide
                                    .with(User.this)
                                    .load("https://check-time.in/image/noimage.jpg")
                                    .centerCrop()
                                    .into(tvImage);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: "+error);
                    }
                }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Accept", "*/*");
                params.put("Accept-Encoding", "gzip, deflate, br");
                params.put("Connection", "keep-alive");
                params.put("Authorization", "Bearer "+Token);
                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }
}