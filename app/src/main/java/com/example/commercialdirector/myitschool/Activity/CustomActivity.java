package com.example.commercialdirector.myitschool.Activity;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.commercialdirector.myitschool.Helper.SQLiteHandler;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.connection.APIService;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.models.ResultQuerry;
import com.example.commercialdirector.myitschool.models.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomActivity extends AppCompatActivity {
    private TextInputLayout userlogin;
    private SessionManager sessionManager;
    private TextInputLayout oldpassword;
    private TextInputLayout newpassword;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customs);
        userlogin = (TextInputLayout) findViewById(R.id.userlogin);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String name = sessionManager.getUser().getName();
        Objects.requireNonNull(userlogin.getEditText()).setText(name);
        final int id = sessionManager.getUser().getId();
        Button saveLogin = (Button) findViewById(R.id.savelogin);

        saveLogin.setOnClickListener(v -> {
            String name1 = userlogin.getEditText().getText().toString().trim();
            updateLogin(id, name1);
            userlogin.getEditText().setText(name1);
        });

        Button savepassword = (Button) findViewById(R.id.savepassword);
        savepassword.setOnClickListener(v -> {
//                String password = newpassword.getText().toString().trim();
//                if (!password.isEmpty()) {
//
////                    checkLogin(password);
//
//                } else {
//
//                    Toast.makeText(getApplicationContext(),
//                            "Необходимые данные не введены", Toast.LENGTH_LONG)
//                            .show();
//                }

        });
    }

    public void updateLogin(int id, String name) {

        Retrofit updateLogin = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final APIService service = updateLogin.create(APIService.class);
        User user = new User(id, name);
        Call<ResultQuerry> call = service.updateLogin(
                user.getId(),
                user.getName()
        );

        call.enqueue(new Callback<ResultQuerry>() {
            @Override
            public void onResponse(Call<ResultQuerry> call, Response<ResultQuerry> response) {
                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResultQuerry> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

//    private void checkLogin(final String password) {
//
//        String tag_string_req = "req_login";
//
//
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                AppConfig.URL_LOGIN, new com.android.volley.Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//                    if (!error) {
////                        sessionManager.setLogin(true);
////
////
////                        String uid = jObj.getString("uid");
////
////                        JSONObject user = jObj.getJSONObject("user");
////                        String name = user.getString("name");
////                        String email = user.getString("email");
////                        String created_at = user
////                                .getString("created_at");
////                        int id = user.getInt("id");
////
////
////                        db.addUser(name, email, uid, created_at);
////
////                        User us = new User (id, name, email);
////                        sessionManager.userLogin(us);
//
//                    } else {
//
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("password", password);
//
//                return params;
//            }
//
//        };
//
//
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }
}
