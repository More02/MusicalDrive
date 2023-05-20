package com.example.commercialdirector.myitschool.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.models.News;

import java.util.ArrayList;

public class RecyclerNewsAdapter extends RecyclerView.Adapter<RecyclerNewsAdapter.ViewHolder> {
    private final ArrayList<News> news;
    public static final String USER_NAME = "id_name";
    public static final String USER_ID = "id_user";

    @NonNull
    @Override
    public RecyclerNewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_news, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerNewsAdapter.ViewHolder holder, int position) {
        holder.personNameNews.setText(news.get(position).getName());
        holder.tvRecyclerItemNews.setText(news.get(position).getName_music());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public RecyclerNewsAdapter(ArrayList<News> news, Context mCtx) {
        this.news = news;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView personNameNews;
        private final TextView tvRecyclerItemNews;

        public ViewHolder(View itemView) {
            super(itemView);
            CardView cvNews = (CardView) itemView.findViewById(R.id.cvnews);
            personNameNews = (TextView) itemView.findViewById(R.id.person_namenews);
            ImageButton playBtnNews = (ImageButton) itemView.findViewById(R.id.playbtnews);
            ImageButton pauseBtnNews = (ImageButton) itemView.findViewById(R.id.pausebtnews);
            ImageButton stopBtnNews = (ImageButton) itemView.findViewById(R.id.stopbtnews);
            tvRecyclerItemNews = (TextView) itemView.findViewById(R.id.tv_recycler_itemnews);
            CheckBox likeBtnNews = (CheckBox) itemView.findViewById(R.id.likebtnews);
            playBtnNews.setOnClickListener(this);
            pauseBtnNews.setOnClickListener(this);
            stopBtnNews.setOnClickListener(this);
            cvNews.setOnClickListener(this);
            likeBtnNews.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.playbtnews: {
                }
                ;
                break;
                case R.id.pausebtnews: {
                }
                ;
                break;
                case R.id.stopbtnews: {
                }
                ;
                break;
                case R.id.likebtnews: {
                }
                ;
                break;
                case R.id.cvnews: {
                }
                ;
                break;
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
