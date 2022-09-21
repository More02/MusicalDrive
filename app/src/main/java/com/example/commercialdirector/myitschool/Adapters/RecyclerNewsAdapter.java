package com.example.commercialdirector.myitschool.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commercialdirector.myitschool.Activity.UserActivity;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.connection.APIService;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.models.Likei;
import com.example.commercialdirector.myitschool.models.Music;
import com.example.commercialdirector.myitschool.models.New;
import com.example.commercialdirector.myitschool.models.News;
import com.example.commercialdirector.myitschool.models.Result_Querry;
import com.example.commercialdirector.myitschool.models.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerNewsAdapter extends RecyclerView.Adapter<RecyclerNewsAdapter.ViewHolder>{
    private Context mCtx;
    private SessionManager sessionmanager;
    private RecyclerView mRecyclerView;
    private RecyclerNewsAdapter rAdapter;
    private RecyclerMusicesAdapter mAdapter;
    private ArrayList<Likei> liles = null;
    private ArrayList<Music> mDataset;
    private ArrayList<User> users;
    private ArrayList<New> news;
    private int id;

    public static final String USER_NAME="id_name";
    public static final String USER_ID="id_user";

    @Override
    public RecyclerNewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_news, parent, false);

        RecyclerNewsAdapter.ViewHolder vh = new RecyclerNewsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerNewsAdapter.ViewHolder holder, int position) {
//        id = mDataset.get(position).getId_user();
        holder.person_namenews.setText(news.get(position).getName());
        //holder.musicviewnews.setText
        holder.tv_recycler_itemnews.setText(news.get(position).getName_music());

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public RecyclerNewsAdapter(ArrayList<New> news, Context mCtx) {
        this.news = news;
        this.mCtx = mCtx;
    }





    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CardView cvnews;
        private TextView person_namenews;
        private ImageButton playbtnews;
        private ImageButton pausebtnews;
        private ImageButton stopbtnews;
        private TextView tv_recycler_itemnews;
        private CheckBox likebtnews;
//        private TextView numberlikesnews;




        public ViewHolder(View itemView) {
            super(itemView);

            cvnews = (CardView)itemView.findViewById(R.id.cvnews);
            person_namenews = (TextView) itemView.findViewById(R.id.person_namenews);
            playbtnews = (ImageButton)itemView.findViewById(R.id.playbtnews);
            pausebtnews = (ImageButton)itemView.findViewById(R.id.pausebtnews);
            stopbtnews = (ImageButton)itemView.findViewById(R.id.stopbtnews);
            tv_recycler_itemnews = (TextView)itemView.findViewById(R.id.tv_recycler_itemnews);
            likebtnews = (CheckBox)itemView.findViewById(R.id.likebtnews);
//            numberlikesnews = (TextView)itemView.findViewById(R.id.numberlikesnews);



            playbtnews.setOnClickListener(this);
            pausebtnews.setOnClickListener(this);
            stopbtnews.setOnClickListener(this);
            cvnews.setOnClickListener(this);
            likebtnews.setOnClickListener(this);
//            person_namenews.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.playbtnews: {};break;
                case R.id.pausebtnews:{};break;
                case R.id.stopbtnews:{};break;
                case R.id.likebtnews:{};break;
                case R.id.cvnews:{};break;
//                case R.id.person_namenews:
//                {
//                    Intent intent=new Intent(mCtx, UserActivity.class);
//                    intent.putExtra(USER_ID,users.get(getPosition()).getId());
//                    intent.putExtra(USER_NAME,users.get(getPosition()).getName());
//                    mCtx.startActivity(intent);
//
//                };break;

            }


        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
