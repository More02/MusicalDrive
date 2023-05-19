package com.example.commercialdirector.myitschool.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

import com.example.commercialdirector.myitschool.Adapters.RecyclerUsersAdapter;
import com.example.commercialdirector.myitschool.Helper.SQLiteHandler;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.connection.APIService;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.models.User;
import com.example.commercialdirector.myitschool.models.Users;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubscriptionsActivity extends AppCompatActivity {
    private RecyclerView nRecyclerView;
    private RecyclerUsersAdapter uAdapter;
    private SessionManager sessionmanager;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_begin);
        nRecyclerView = (RecyclerView) findViewById(R.id.rvpodpiski);
        nRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(getApplicationContext());
        nRecyclerView.setLayoutManager(nLayoutManager);
        subscriptions();
    }

    private void subscriptions() {
        Retrofit subscriptions = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = subscriptions.create(APIService.class);
        sessionmanager = new SessionManager(getApplicationContext());
        User user = sessionmanager.getUser();
        int id = user.getId();
        Call<Users> call = service.podpisci(id);
        call.enqueue(new Callback<Users>() {
                         @Override
                         public void onResponse(Call<Users> call, Response<Users> response) {
                             uAdapter = new RecyclerUsersAdapter(response.body().getUsers(), getApplicationContext());
                             nRecyclerView.setAdapter(uAdapter);
                         }

                         @Override
                         public void onFailure(Call<Users> call, Throwable t) {

                         }
                     }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.exitbutton) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        sessionmanager.setLogin(false);

        db.deleteUsers();
        Intent intent = new Intent(SubscriptionsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
