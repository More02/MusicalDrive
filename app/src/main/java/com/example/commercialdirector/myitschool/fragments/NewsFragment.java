package com.example.commercialdirector.myitschool.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.commercialdirector.myitschool.Adapters.RecyclerMusicesAdapter;
import com.example.commercialdirector.myitschool.Adapters.RecyclerNewsAdapter;
import com.example.commercialdirector.myitschool.Adapters.RecyclerUsersAdapter;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.connection.APIService;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.models.Music;
import com.example.commercialdirector.myitschool.models.Musics;
import com.example.commercialdirector.myitschool.models.New;
import com.example.commercialdirector.myitschool.models.News;
import com.example.commercialdirector.myitschool.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewsFragment extends Fragment {

    private RecyclerView nRecyclerView;
    private RecyclerNewsAdapter newsAdapter;
    private RecyclerView.LayoutManager nLayoutManager;
    private SessionManager sessionmanager;
    private TextView person_namenews;




    public NewsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);

        person_namenews = (TextView) v.findViewById(R.id.person_namenews);

        nRecyclerView=(RecyclerView)v.findViewById(R.id.rvnews);
        nRecyclerView.setHasFixedSize(true);

        nLayoutManager = new LinearLayoutManager(getActivity());
        nRecyclerView.setLayoutManager(nLayoutManager);
        sessionmanager = new SessionManager(getActivity());
        String d =  sessionmanager.getUser().getName();
       // person_namenews.setText(d);

        Retrofit news = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = news.create(APIService.class);

        User user = sessionmanager.getUser();
        int id = user.getId();
        Call<News> call = service.news(id);
        call.enqueue(new Callback<News>() {
                         @Override
                         public void onResponse(Call<News> call, Response<News> response) {
                             ArrayList<New> newi = new ArrayList<New>();
                             newi = response.body().getNews();
                             newsAdapter = new RecyclerNewsAdapter(response.body().getNews(), getActivity());
                             nRecyclerView.setAdapter(newsAdapter);

                         }

                         @Override
                         public void onFailure(Call<News> call, Throwable t) {

                         }
                     }
        );
        return v;
    }

}
