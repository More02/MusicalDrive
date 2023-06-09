package com.example.commercialdirector.myitschool.Activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.commercialdirector.myitschool.Helper.SQLiteHandler;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.HomeActivity;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.connection.AppController;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private TextInputLayout inputFullName;
    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (TextInputLayout) findViewById(R.id.name);
        inputEmail = (TextInputLayout) findViewById(R.id.email);
        inputPassword = (TextInputLayout) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        Log.d("error",TAG);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        session = new SessionManager(getApplicationContext());


        db = new SQLiteHandler(getApplicationContext());


        if (session.isLoggedIn()) {

            Intent intent = new Intent(RegisterActivity.this,
                    HomeActivity.class);
            startActivity(intent);
            finish();
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getEditText().getText().toString().trim();
                String email = inputEmail.getEditText().getText().toString().trim();
                String password = inputPassword.getEditText().getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Необходимые данные не введены", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void registerUser(final String name, final String email,
                              final String password) {

        String tag_string_req = "req_register";

        pDialog.setMessage("Выполняется регистрация...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");


                        db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "Регистрация прошла успешно. Теперь войдите в свой аккаунт", Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {


                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
