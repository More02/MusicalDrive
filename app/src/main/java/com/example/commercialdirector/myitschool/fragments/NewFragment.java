package com.example.commercialdirector.myitschool.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class NewFragment extends Fragment {
    private RecyclerView nRecyclerView;
    private RecyclerView.LayoutManager nLayoutManager;
    private RecyclerUsersAdapter uAdapter;
    private SessionManager sessionmanager;
//    private ImageButton btnpodpichiki;
    private Button btnpodpiski;


    public NewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_new, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rvnew);
        nRecyclerView=(RecyclerView)v.findViewById(R.id.rvnew);
        nRecyclerView.setHasFixedSize(true);

        nLayoutManager = new LinearLayoutManager(getActivity());
        nRecyclerView.setLayoutManager(nLayoutManager);

//        btnpodpichiki = (ImageButton) v.findViewById(R.id.btnpodpichiki);
//        btnpodpichiki.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                podpischiki();
//            }
//        });

//        btnpodpiski = (Button) v.findViewById(R.id.btnpodpiski);
//        btnpodpiski.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getContext(),
//                        NewBeginActivity.class);
//                startActivity(i);
//
//
//             //   podpisci();
//            }
//        });

        podpischiki();


        return v;
    }

    public void podpischiki(){
        Retrofit podpischiki = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = podpischiki.create(APIService.class);
        sessionmanager = new SessionManager(getActivity());
        User user = sessionmanager.getUser();
        int id = user.getId();
        Call<Users> call = service.podpischiki(id);
        call.enqueue(new Callback<Users>() {
                         @Override
                         public void onResponse(Call<Users> call, Response<Users> response) {
                             ArrayList<User> user =new ArrayList<User>();
                             user = response.body().getUsers();
                             uAdapter = new RecyclerUsersAdapter(response.body().getUsers(), getActivity());
                             nRecyclerView.setAdapter(uAdapter);
                         }

                         @Override
                         public void onFailure(Call<Users> call, Throwable t) {

                         }
                     }
        );

    }



}
