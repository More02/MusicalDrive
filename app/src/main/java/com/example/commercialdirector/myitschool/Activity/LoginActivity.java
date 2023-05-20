package com.example.commercialdirector.myitschool.Activity;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.commercialdirector.myitschool.models.User;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEmail = (TextInputLayout) findViewById(R.id.email);
        inputPassword = (TextInputLayout) findViewById(R.id.password);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(view -> {
            String email = Objects.requireNonNull(inputEmail.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(inputPassword.getEditText()).getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {

                checkLogin(email, password);
            } else {

                Toast.makeText(getApplicationContext(),
                                "Необходимые данные не введены", Toast.LENGTH_LONG)
                        .show();
            }
        });

        btnLinkToRegister.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),
                    RegisterActivity.class);
            startActivity(i);
            finish();
        });

    }

    private void checkLogin(final String email, final String password) {

        String tag_string_req = "req_login";

        pDialog.setMessage("Выполняется вход...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, response -> {
            Log.d(TAG, "Login Response: " + response.toString());
            hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    session.setLogin(true);
                    String uid = jObj.getString("uid");
                    JSONObject user = jObj.getJSONObject("user");
                    String name = user.getString("name");
                    String email1 = user.getString("email");
                    String created_at = user
                            .getString("created_at");
                    int id = user.getInt("id");
                    db.addUser(name, email1, uid, created_at);
                    User us = new User(id, name, email1);
                    session.userLogin(us);
                    Intent intent = new Intent(LoginActivity.this,
                            HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }, error -> {
            Log.e(TAG, "Login Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
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
