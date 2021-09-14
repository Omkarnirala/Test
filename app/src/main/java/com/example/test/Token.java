package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Token extends AppCompatActivity {
    private static final String TAG = "Token Activity";
    String userName, password;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        Log.d(TAG, "onCreate: "+userName);
        Log.d(TAG, "onCreate: "+password);
        getToken();
    }

    private void getToken() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.mypharmacy4u.com/token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(TAG, "onResponse: "+jsonObject.getString("access_token"));
                    // Log.d(TAG, "onResponse: " + jsonObject.getString("access_token"));

                    Intent intent = new Intent(Token.this, User.class);
                    intent.putExtra("Token",jsonObject.getString("access_token"));
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Accept","*/*");
                params.put("Accept-Encoding","gzip, deflate, br");
                params.put("Connection","keep-alive");
                params.put("Authorization", "BASIC YXBwY2xpZW50c2VjcmV0OjdGRTU1NzYxLTFFNDMtNEJBOS05QjcxLUVDOUNGNDg4MDhCNg==");
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", userName);
                params.put("password", password);
                params.put("grant_type", "password");
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

}
