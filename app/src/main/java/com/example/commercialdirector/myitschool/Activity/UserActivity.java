package com.example.commercialdirector.myitschool.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commercialdirector.myitschool.Adapters.RecyclerMusiceAdapter;
import com.example.commercialdirector.myitschool.Adapters.RecyclerUsersAdapter;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.connection.APIService;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.models.Musics;
import com.example.commercialdirector.myitschool.models.Subscription;
import com.example.commercialdirector.myitschool.models.Subscriptions;
import com.example.commercialdirector.myitschool.models.ResultQuerry;
import com.example.commercialdirector.myitschool.models.User;
import com.example.commercialdirector.myitschool.models.Users;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserActivity extends AppCompatActivity {

    private TextView userLoginId;
    private RecyclerView mRecyclerView;
    private RecyclerMusiceAdapter mAdapter;
    private Button btnSubscribe;
    private Button btnUnsubscribe;

    public UserActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String name = intent.getStringExtra(RecyclerUsersAdapter.USER_NAME);
        final int id = intent.getIntExtra(RecyclerUsersAdapter.USER_ID, 0);
        setContentView(R.layout.activity_user);
        SessionManager sessionManager = new SessionManager(this);
        final int idme = sessionManager.getUser().getId();

        ImageView userAnotherPhotoImageView = (ImageView) findViewById(R.id.userAnotherPhotoImageView);
        userLoginId = (TextView) findViewById(R.id.userLoginId);
        btnSubscribe = (Button) findViewById(R.id.btnPodpiska);
        Retrofit isPodpiskaAdded = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();
        APIService service = isPodpiskaAdded.create(APIService.class);
        Subscription podpiska = new Subscription(idme, id);

        Call<Subscriptions> call = service.isPodpiskaAdded(podpiska.getId_user1(), podpiska.getId_user2());

        call.enqueue(new Callback<Subscriptions>() {
            @Override
            public void onResponse(Call<Subscriptions> call, Response<Subscriptions> response) {
                //Toast.makeText(getApplicationContext(), Integer.toString(response.body().getPodpiskas().size()), Toast.LENGTH_SHORT).show();
                if (response.body().getPodpiskas().size() != 0) {
                    btnSubscribe.setVisibility(View.GONE);
                    btnUnsubscribe.setVisibility(View.VISIBLE);
                } else {
                    btnSubscribe.setVisibility(View.VISIBLE);
                    btnUnsubscribe.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Subscriptions> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btnSubscribe.setOnClickListener(v -> subscribe(idme, id));
        btnUnsubscribe = (Button) findViewById(R.id.btnOtpiska);
        btnUnsubscribe.setOnClickListener(v -> unsubscribe(idme, id));
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_user);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        getUserByName(name);
        update(id);
    }

    private void subscribe(int id_user1, int id_user2) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Выполняется подписка...");
        pDialog.show();
        Retrofit subscribe = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();
        APIService service = subscribe.create(APIService.class);
        Subscription subscribeItem = new Subscription(id_user1, id_user2);

        Call<ResultQuerry> call = service.podpiska(subscribeItem.getId_user1(), subscribeItem.getId_user2());

        call.enqueue(new Callback<ResultQuerry>() {
            @Override
            public void onResponse(Call<ResultQuerry> call, Response<ResultQuerry> response) {
                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                btnSubscribe.setVisibility(View.GONE);
                btnUnsubscribe.setVisibility(View.VISIBLE);
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultQuerry> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void unsubscribe(int id_user1, int id_user2) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Выполняется отписка...");
        pDialog.show();
        Retrofit unsubscribe = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();
        APIService service = unsubscribe.create(APIService.class);
        Subscription subscribeItem = new Subscription(id_user1, id_user2);
        Call<ResultQuerry> call = service.otpiska(subscribeItem.getId_user1(), subscribeItem.getId_user2());

        call.enqueue(new Callback<ResultQuerry>() {
            @Override
            public void onResponse(Call<ResultQuerry> call, Response<ResultQuerry> response) {
                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                btnSubscribe.setVisibility(View.VISIBLE);
                btnUnsubscribe.setVisibility(View.GONE);
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultQuerry> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getUserByName(String name) {
        Retrofit getuserbyname = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();
        APIService service = getuserbyname.create(APIService.class);
        Call<Users> call = service.getUserByName(name);
        call.enqueue(new Callback<Users>() {

            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                ArrayList<User> users = response.body().getUsers();
                User user = users.get(0);
                userLoginId.setText(user.getName());
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void update(int id) {
        Retrofit allMusic = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();

        APIService service = allMusic.create(APIService.class);
        Call<Musics> call = service.getMusics(id);
        call.enqueue(new Callback<Musics>() {
            @Override
            public void onResponse(Call<Musics> call, Response<Musics> response) {
                mAdapter = new RecyclerMusiceAdapter(response.body().getMusics(), getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<Musics> call, Throwable t) {

            }
        });
    }

}
