package com.example.test;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.MailTo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    EditText user_contact, user_password;
    Button btnLogin;
    String contact, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_contact = findViewById(R.id.user_contact);
        user_password = findViewById(R.id.editTextTextPassword);
        btnLogin = findViewById(R.id.login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

    }

    private void checkLogin() {

        contact= user_contact.getText().toString();
        Password = user_password.getText().toString();
        if (contact.isEmpty() || Password.isEmpty()){
            alertFail("User Contact and Password Required");
        }else {
            sendLogin();
        }
    }

    private void sendLogin() {

        StringRequest request = new StringRequest(Request.Method.POST, "https://api.mypharmacy4u.com/api/user/signin", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String jsonObject1 = jsonObject.getString("data");
                    JSONObject jsonObject2 = new JSONObject(jsonObject1);

                    Log.d(TAG, "onResponse: "+jsonObject.getString("message"));
                    Log.d(TAG, "onResponse: "+jsonObject2.getString("user_contact"));
                    Log.d(TAG, "onResponse: "+jsonObject2.getString("user_password"));

                    if (jsonObject.getString("message").equals("Data Found")){
                        Toast.makeText(MainActivity.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Token.class);
                        intent.putExtra("username",jsonObject2.getString("user_contact"));
                        intent.putExtra("password",jsonObject2.getString("user_password"));
                        startActivity(intent);
                    }
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
                params.put("user_contact", user_contact.getText().toString());
                params.put("user_password", user_password.getText().toString());
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void alertFail(String s) {
        new AlertDialog.Builder(this)
                .setTitle("Failed")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setMessage(s)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}

