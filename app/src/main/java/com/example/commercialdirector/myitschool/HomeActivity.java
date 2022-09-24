package com.example.commercialdirector.myitschool;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


import com.example.commercialdirector.myitschool.Activity.LoginActivity;
import com.example.commercialdirector.myitschool.Helper.SQLiteHandler;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.fragments.FoundFragment;
import com.example.commercialdirector.myitschool.fragments.NewFragment;
import com.example.commercialdirector.myitschool.fragments.NewsFragment;
import com.example.commercialdirector.myitschool.fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;
    AppCompatActivity activity;
    private SQLiteHandler db;
    private SessionManager session;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment=new ProfileFragment();
                    break;
                case R.id.navigation_new:
//                        Intent intent = new Intent(HomeActivity.this, NewBeginActivity.class);
//                        startActivity(intent);

                   fragment=new NewFragment();
                    break;
                case R.id.navigation_found:
                    fragment=new FoundFragment();
                    break;
                case R.id.navigation_news:
                    fragment=new NewsFragment();
                    break;
            }

            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content,fragment).commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        setContentView(R.layout.activity_home);
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content,new ProfileFragment()).commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
        session.setLogin(false);

        db.deleteUsers();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
