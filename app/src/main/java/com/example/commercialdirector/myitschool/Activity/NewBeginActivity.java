package com.example.commercialdirector.myitschool.Activity;

import android.content.Intent;
import androidx.annotation.NonNull;
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

public class NewBeginActivity extends AppCompatActivity {
    private RecyclerView nRecyclerView;
    private RecyclerView.LayoutManager nLayoutManager;
    private RecyclerUsersAdapter uAdapter;
    private SessionManager sessionmanager;
//    FragmentManager fragmentManager;
//    FragmentTransaction fragmentTransaction;
    Fragment fragment;
//    private ImageButton btnpodpichikin;
//    private ImageButton btnpodpiskin;
     private SQLiteHandler db;




//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home_newbeginactivity:
//                    fragment=new ProfileFragment();
//                    break;
//                case R.id.navigation_new_newbeginactivity:
//                    fragment=new NewFragment();
//                    break;
//                case R.id.navigation_found_newbeginactivity:
//                    fragment=new FoundFragment();
//                    break;
//                case R.id.navigation_news_newbeginactivity:
//                    fragment=new NewsFragment();
//                    break;
//            }
//
//            fragmentManager=getSupportFragmentManager();
//            fragmentTransaction=fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.content_newbeginactivity,fragment).commit();
//            return true;
//        }
//    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_begin);


        RecyclerView rv = (RecyclerView) findViewById(R.id.rvpodpiski);
        nRecyclerView=(RecyclerView)findViewById(R.id.rvpodpiski);
        nRecyclerView.setHasFixedSize(true);

        nLayoutManager = new LinearLayoutManager(getApplicationContext());
        nRecyclerView.setLayoutManager(nLayoutManager);

       // db = new SQLiteHandler(getApplicationContext());


//        nRecyclerView.setHasFixedSize(true);

//        nLayoutManager = new LinearLayoutManager(getApplicationContext());
//        nRecyclerView.setLayoutManager(nLayoutManager);

//        btnpodpichikin = (ImageButton) findViewById(R.id.btnpodpichikin);
//        btnpodpichikin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //podpischiki();
//                   fragment = new NewFragment();
////                    fragmentManager=getSupportFragmentManager();
////                    fragmentTransaction=fragmentManager.beginTransaction();
////                    fragmentTransaction.replace(R.id.content_newbeginactivity,fragment).commit();
//            }
//        });

//        btnpodpiskin = (ImageButton) findViewById(R.id.btnpodpiskin);
//        btnpodpiskin.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                podpisci();
//            }
//        });

        podpisci();

//        db = new SQLiteHandler(getApplicationContext());
//        session = new SessionManager(getApplicationContext());
//        setContentView(R.layout.activity_new_begin);
//
//        //RecyclerView rv = (RecyclerView) findViewById(R.id.rvpodpiski);
//        nRecyclerView=(RecyclerView)findViewById(R.id.rvpodpiski);
//        nRecyclerView.setHasFixedSize(true);
//
//        nLayoutManager = new LinearLayoutManager(this);
//        nRecyclerView.setLayoutManager(nLayoutManager);
//



    }

    private void podpisci() {

        Retrofit podpisci = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = podpisci.create(APIService.class);
        sessionmanager = new SessionManager(getApplicationContext());
        User user = sessionmanager.getUser();
        int id = user.getId();
        Call<Users> call = service.podpisci(id);
        call.enqueue(new Callback<Users>() {
                         @Override
                         public void onResponse(Call<Users> call, Response<Users> response) {
                             ArrayList<User> user =new ArrayList<User>();
                             user = response.body().getUsers();
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
        switch (id) {
            case R.id.exitbutton:
                logoutUser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        sessionmanager.setLogin(false);

        db.deleteUsers();
        Intent intent = new Intent(NewBeginActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
