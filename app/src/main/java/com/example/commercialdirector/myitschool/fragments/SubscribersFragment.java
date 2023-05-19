package com.example.commercialdirector.myitschool.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commercialdirector.myitschool.Adapters.RecyclerUsersAdapter;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.connection.APIService;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.models.User;
import com.example.commercialdirector.myitschool.models.Users;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubscribersFragment extends Fragment {
    private RecyclerView nRecyclerView;
    private RecyclerUsersAdapter uAdapter;

    public SubscribersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_new, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rvnew);
        nRecyclerView = (RecyclerView) v.findViewById(R.id.rvnew);
        nRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(getActivity());
        nRecyclerView.setLayoutManager(nLayoutManager);
        subscribers();
        return v;
    }

    public void subscribers() {
        Retrofit podpischiki = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();

        APIService service = podpischiki.create(APIService.class);
        SessionManager sessionmanager = new SessionManager(getActivity());
        User user = sessionmanager.getUser();
        int id = user.getId();
        Call<Users> call = service.podpischiki(id);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                uAdapter = new RecyclerUsersAdapter(response.body().getUsers(), getActivity());
                nRecyclerView.setAdapter(uAdapter);
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

            }
        });

    }

}
