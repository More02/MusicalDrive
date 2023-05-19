package com.example.commercialdirector.myitschool.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commercialdirector.myitschool.Adapters.RecyclerNewsAdapter;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.connection.APIService;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.models.NewsAll;
import com.example.commercialdirector.myitschool.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewsFragment extends Fragment {

    private RecyclerView nRecyclerView;
    private RecyclerNewsAdapter newsAdapter;

    public NewsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);

        nRecyclerView = (RecyclerView) v.findViewById(R.id.rvnews);
        nRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(getActivity());
        nRecyclerView.setLayoutManager(nLayoutManager);
        SessionManager sessionmanager = new SessionManager(getActivity());
        String d = sessionmanager.getUser().getName();

        Retrofit news = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();

        APIService service = news.create(APIService.class);

        User user = sessionmanager.getUser();
        int id = user.getId();
        Call<NewsAll> call = service.news(id);
        call.enqueue(new Callback<NewsAll>() {
            @Override
            public void onResponse(Call<NewsAll> call, Response<NewsAll> response) {
                newsAdapter = new RecyclerNewsAdapter(response.body().getNews(), getActivity());
                nRecyclerView.setAdapter(newsAdapter);

            }

            @Override
            public void onFailure(Call<NewsAll> call, Throwable t) {

            }
        });
        return v;
    }

}
