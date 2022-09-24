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

import com.example.commercialdirector.myitschool.Adapters.RecyclerMusicesAdapter;
import com.example.commercialdirector.myitschool.Adapters.RecyclerUsersAdapter;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.connection.APIService;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.models.Music;
import com.example.commercialdirector.myitschool.models.Musics;
import com.example.commercialdirector.myitschool.models.Podpiska;
import com.example.commercialdirector.myitschool.models.Podpiskas;
import com.example.commercialdirector.myitschool.models.Result_Querry;
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
    private RecyclerMusicesAdapter mAdapter;
    private ImageView userAnotherPhotoImageView;
    private Button btnPodpiska;
    private Button btnOtpiska;
    private ArrayList<Podpiska> pDataset;
    private SessionManager sessionManager;


    private RecyclerView.LayoutManager mLayoutManager;
    private User user;
    private User userme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        String name=intent.getStringExtra(RecyclerUsersAdapter.USER_NAME);
        final int id=intent.getIntExtra(RecyclerUsersAdapter.USER_ID,0);
        setContentView(R.layout.activity_user);
        sessionManager=new SessionManager(this);
        final int idme = sessionManager.getUser().getId();

        userAnotherPhotoImageView = (ImageView) findViewById(R.id.userAnotherPhotoImageView);
        userLoginId = (TextView)findViewById(R.id.userLoginId);
        btnPodpiska = (Button) findViewById(R.id.btnPodpiska);
        Retrofit isPodpiskaAdded = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = isPodpiskaAdded.create(APIService.class);
        Podpiska podpiska = new Podpiska(idme, id);

        Call<Podpiskas> call = service.isPodpiskaAdded(
                podpiska.getId_user1(),
                podpiska.getId_user2()
        );

        call.enqueue(new Callback<Podpiskas>() {
            @Override
            public void onResponse(Call<Podpiskas> call, Response<Podpiskas> response) {
                Toast.makeText(getApplicationContext(), Integer.toString(response.body().getPodpiskas().size()), Toast.LENGTH_SHORT).show();
                if (response.body().getPodpiskas().size() != 0) {
                    btnPodpiska.setVisibility(View.GONE);
                    btnOtpiska.setVisibility(View.VISIBLE);
                }

                else {
                    btnPodpiska.setVisibility(View.VISIBLE);
                    btnOtpiska.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Podpiskas> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        btnPodpiska.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                podpiska(idme,id);
            }
        });

        btnOtpiska = (Button)findViewById(R.id.btnOtpiska);
        btnOtpiska.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpiska(idme,id);
            }
        });

        mRecyclerView=(RecyclerView)findViewById(R.id.recycler_view_user);
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        getUserByName(name);
        update(id);
    }

    private void podpiska(int id_user1, int id_user2) {
        final ProgressDialog pDialog=new ProgressDialog(this);
        pDialog.setMessage("Выполняется подписка...");
        pDialog.show();
        Retrofit podpiska = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = podpiska.create(APIService.class);
        Podpiska podpiskai = new Podpiska(id_user1, id_user2);

        Call<Result_Querry> call = service.podpiska(
                podpiskai.getId_user1(),
                podpiskai.getId_user2()
        );

        call.enqueue(new Callback<Result_Querry>() {
            @Override
            public void onResponse(Call<Result_Querry> call, Response<Result_Querry> response) {
                Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                btnPodpiska.setVisibility(View.GONE);
                btnOtpiska.setVisibility(View.VISIBLE);
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Result_Querry> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void otpiska(int id_user1, int id_user2) {
        final ProgressDialog pDialog=new ProgressDialog(this);
        pDialog.setMessage("Выполняется отписка...");
        pDialog.show();
        Retrofit otpiska = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = otpiska.create(APIService.class);
        Podpiska podpiskai = new Podpiska(id_user1, id_user2);

        Call<Result_Querry> call = service.otpiska(
                podpiskai.getId_user1(),
                podpiskai.getId_user2()
        );

        call.enqueue(new Callback<Result_Querry>() {
            @Override
            public void onResponse(Call<Result_Querry> call, Response<Result_Querry> response) {
                Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                btnPodpiska.setVisibility(View.VISIBLE);
                btnOtpiska.setVisibility(View.GONE);
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Result_Querry> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void getUserByName(String name) {
        Retrofit getuserbyname = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

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
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void update(int id){
        Retrofit allMusic = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = allMusic.create(APIService.class);
        Call<Musics> call = service.getMusics(id);
        call.enqueue(new Callback<Musics>() {
                         @Override
                         public void onResponse(Call<Musics> call, Response<Musics> response) {
                             ArrayList<Music> music =new ArrayList<Music>();
                             music = response.body().getMusicses();
                             mAdapter = new RecyclerMusicesAdapter(response.body().getMusicses(), getApplicationContext());
                             mRecyclerView.setAdapter(mAdapter);
                         }

                         @Override
                         public void onFailure(Call<Musics> call, Throwable t) {

                         }
                     }
        );
    }



}
