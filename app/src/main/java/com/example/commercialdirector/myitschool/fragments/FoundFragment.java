package com.example.commercialdirector.myitschool.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.commercialdirector.myitschool.Adapters.RecyclerUsersAdapter;
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


public class FoundFragment extends Fragment {
    private EditText foundinput;
    private Button foundbtn;
    private SessionManager sessionmanager;
    private RecyclerView uRecyclerView;
    private RecyclerView.LayoutManager uLayoutManager;
    private RecyclerUsersAdapter uAdapter;


    public FoundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_found, container, false);

        uRecyclerView = (RecyclerView) v.findViewById(R.id.rvfound);
        uRecyclerView.setHasFixedSize(true);

        uLayoutManager = new LinearLayoutManager(getActivity());
        uRecyclerView.setLayoutManager(uLayoutManager);


        foundinput = (EditText) v.findViewById(R.id.foundinput);
        foundbtn = (Button) v.findViewById(R.id.foundbtn);

        foundbtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String name = foundinput.getText().toString().trim();

                if (!name.isEmpty()) {

                    getUserByName(name);

                } else {
                    Toast.makeText(getContext(), "Введите логин пользователя, которого хотите найти", Toast.LENGTH_LONG).show();
                }
            }
        });


        return v;


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
                ArrayList<User> user = response.body().getUsers();
                if (user.size()==0) {
                    Toast.makeText(getActivity(), "Пользователь с таким логином не существует", Toast.LENGTH_LONG).show();
                }
                uAdapter = new RecyclerUsersAdapter(user, getActivity());
                uRecyclerView.setAdapter(uAdapter);
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
    }

}

