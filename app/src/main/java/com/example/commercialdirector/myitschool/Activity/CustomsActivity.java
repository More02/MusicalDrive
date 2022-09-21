package com.example.commercialdirector.myitschool.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.commercialdirector.myitschool.Adapters.RecyclerUsersAdapter;
import com.example.commercialdirector.myitschool.Helper.SQLiteHandler;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.HomeActivity;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.connection.APIService;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.connection.AppController;
import com.example.commercialdirector.myitschool.models.Result_Querry;
import com.example.commercialdirector.myitschool.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomsActivity extends AppCompatActivity {
    private EditText userlogin;
    private SessionManager sessionManager;
    private Button savelogin;
    private Button savepassword;
    private EditText oldpassword;
    private EditText newpassword;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customs);


        userlogin = (EditText) findViewById(R.id.userlogin);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String name =  sessionManager.getUser().getName();
        userlogin.setText(name);
        final int id = sessionManager.getUser().getId();

        savelogin = (Button) findViewById(R.id.savelogin);
        savelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userlogin.getText().toString().trim();
                updateLogin(id, name);
                userlogin.setText(name);
            }
        });

        savepassword = (Button) findViewById(R.id.savepassword);
        savepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
        });



    }

    public void updateLogin (int id, String name){

        Retrofit updateLogin = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final APIService service = updateLogin.create(APIService.class);
        User user = new User (id, name);
        Call<Result_Querry> call = service.updateLogin(
                user.getId(),
                user.getName()
        );

        call.enqueue(new Callback<Result_Querry>() {
            @Override
            public void onResponse(Call<Result_Querry> call, Response<Result_Querry> response) {
                Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<Result_Querry> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
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
//
//
//                    if (!error) {
//
//
//
//
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
