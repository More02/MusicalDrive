package com.example.commercialdirector.myitschool.Adapters;


import android.content.Context;
import android.os.AsyncTask;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.connection.APIService;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.models.Likei;
import com.example.commercialdirector.myitschool.models.Likes;
import com.example.commercialdirector.myitschool.models.Media;
import com.example.commercialdirector.myitschool.models.Music;
import com.example.commercialdirector.myitschool.models.Musics;
import com.example.commercialdirector.myitschool.models.Result_Music;
import com.example.commercialdirector.myitschool.models.Result_Querry;
import com.example.commercialdirector.myitschool.models.User;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RecyclerMusicesAdapter extends RecyclerView.Adapter<RecyclerMusicesAdapter.ViewHolder> {

    private ArrayList<Music> mDataset;
    private Context mCtx;
    private Media media;
    private SessionManager sessionmanager;
    private RecyclerView mRecyclerView;
    private RecyclerMusicesAdapter mAdapter;
    private ArrayList<Likei> liles = null;
    private Likes likes;
    private boolean yes;


    //    private MediaPlayer mediaPlayer;
    public RecyclerMusicesAdapter(ArrayList<Music> dataset, Context mCtx) {
        mDataset = dataset;
        this.mCtx = mCtx;
    }

    @Override
    public RecyclerMusicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).getName_music());
        int id_user = mDataset.get(position).getId_user();
        int id_music = mDataset.get(position).getId_music();
        yes=false;
        likes=new Likes();

        Likei likei = new Likei(id_music, id_user);
        likes = new Likes();
        new MTask(holder.likebt).execute(likei);
        holder.numberlikes.setText(Integer.toString(mDataset.get(position).getLikei()));


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView cardView;

        private TextView mTextView;
        private ImageButton playbt;
        private ImageButton pausebt;
        private ImageButton stopbt;
        private CheckBox likebt;
        private TextView numberlikes;


        public ViewHolder(View v) {
            super(v);

            cardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.tv_recycler_item);
            playbt = (ImageButton) v.findViewById(R.id.playbt);
            pausebt = (ImageButton) v.findViewById(R.id.pausebt);
            stopbt = (ImageButton) v.findViewById(R.id.stopbt);
            likebt = (CheckBox) v.findViewById(R.id.likebt);
            numberlikes = (TextView) v.findViewById(R.id.numberlikes);




//            pausebt.setEnabled(false);
//            stopbt.setEnabled(false);
            playbt.setOnClickListener(this);
            pausebt.setOnClickListener(this);
            stopbt.setOnClickListener(this);
            cardView.setOnClickListener(this);
            likebt.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.playbt:
                {
                    releseMP();
                    String path=mDataset.get(getPosition()).getPath();
                    String music=mDataset.get(getPosition()).getName_music();
                    media=new Media(path+"/"+music,mCtx);
                    media.play(v);
//                        pausebt.setEnabled(true);
//                        stopbt.setEnabled(true);
//                        playbt.setEnabled(false);
                };break;
                case R.id.pausebt:
                {
                    media.pause(v);
                    //   pausebt.setEnabled(false);
//                            stopbt.setEnabled(false);
//                            playbt.setEnabled(true);

                };break;
                case R.id.stopbt:
                {
                    media.stop();
                    //pausebt.setEnabled(false);
//                        stopbt.setEnabled(false);
//                        playbt.setEnabled(true);
                };break;
                case R.id.likebt:
                {

                    int likei = mDataset.get(getPosition()).getLikei();

                    //numberlikes.setText(likei);
                    String name_music = mDataset.get(getPosition()).getName_music();
                    int id_user = mDataset.get(getPosition()).getId_user();
                    String path = mDataset.get(getPosition()).getPath();
                    int id_music = mDataset.get(getPosition()).getId_music();
                    if(likebt.isChecked()) {
                        likei++;
                        updateMusic(name_music, id_user, path, likei, id_music);
                        addLike(id_music, id_user);
                    }
                    else
                    {
                        likei--;
                        updateMusic(name_music, id_user, path, likei, id_music);
                        deleteLike(id_music, id_user);
                    }

                };break;
            }

        }

        private void releseMP(){
            if (media !=null) {
                try {
                    media.releaseMedia();
                    media = null;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    public void updateMusic (String name_music, int id_user, String path, int likei, int id_music){

        Retrofit updateMusic = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final APIService service = updateMusic.create(APIService.class);
        Music music = new Music (id_music, name_music, id_user, path, likei);
        Log.d("tr", name_music);
        Log.d("tr", Integer.toString(id_user));
        Log.d("tr", path);
        Log.d("tr", Integer.toString(likei));
        Call<Result_Music> call = service.updateMusic(
                music.getId_music(),
                music.getName_music(),
                music.getId_user(),
                music.getPath(),
                music.getLikei()

        );

        call.enqueue(new Callback<Result_Music>() {
            @Override
            public void onResponse(Call<Result_Music> call, Response<Result_Music> response) {
             //   Toast.makeText(mCtx,response.body().getMessage(),Toast.LENGTH_LONG).show();
             //   update();
            }

            @Override
            public void onFailure(Call<Result_Music> call, Throwable t) {
             // Toast.makeText(mCtx,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }

    public void addLike (int id_music, int id_user){

        Retrofit addLike = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final APIService service = addLike.create(APIService.class);
        Likei likei = new Likei(id_music, id_user);

        Log.d("tr", Integer.toString(id_music));
        Log.d("tr", Integer.toString(id_user));
        Call<Result_Querry> call = service.addLike(
                likei.getId_music(),
                likei.getId_user()
        );

        call.enqueue(new Callback<Result_Querry>() {
            @Override
            public void onResponse(Call<Result_Querry> call, Response<Result_Querry> response) {
              //  Toast.makeText(mCtx,response.body().getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Result_Querry> call, Throwable t) {
             //  Toast.makeText(mCtx,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }
    public void deleteLike (int id_music, int id_user){

        Retrofit deleteLike = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final APIService service = deleteLike.create(APIService.class);
        Likei likei = new Likei(id_music, id_user);

        Log.d("tr", Integer.toString(id_music));
        Log.d("tr", Integer.toString(id_user));
        Call<Result_Querry> call = service.deleteLike(
                likei.getId_music(),
                likei.getId_user()
        );

        call.enqueue(new Callback<Result_Querry>() {
            @Override
            public void onResponse(Call<Result_Querry> call, Response<Result_Querry> response) {
               // Toast.makeText(mCtx,response.body().getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Result_Querry> call, Throwable t) {
               // Toast.makeText(mCtx,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }


    class MTask extends AsyncTask<Likei,Void,Likes>
    {

        private CheckBox checkBox;

        public MTask(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        @Override
        protected Likes doInBackground(Likei... params) {
            Likes like=new Likes();
            try
            {
                Retrofit isLikeAdded = new Retrofit.Builder()
                        .baseUrl(AppConfig.BASE_PUBLIC)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                APIService service = isLikeAdded.create(APIService.class);
                Call<Likes> call = service.isLikeAdded(params[0].getId_music(),
                        params[0].getId_user()
                );

                like= service.isLikeAdded(params[0].getId_music(),params[0].getId_user()).execute().body();
                if(like.getLikes().size()==0) isNotLike();
                else isLike();
            } catch (IOException e)
            {
                Log.d("but",e.getMessage());
            }
            return like;
        }

        @Override
        protected void onPostExecute(Likes like) {

            if(like.getLikes().size()!=0)
            {
                checkBox.setChecked(true);
            }
        }
    }

    private void isLike()
    {
        yes=true;
    }

    private void isNotLike()
    {
        yes=true;
    }
    public void update(){
        Retrofit allMusic = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = allMusic.create(APIService.class);
        sessionmanager = new SessionManager(mCtx);
        User user = sessionmanager.getUser();
        int id = user.getId();
        Call<Musics> call = service.getMusics(user.getId());
        call.enqueue(new Callback<Musics>() {
                         @Override
                         public void onResponse(Call<Musics> call, Response<Musics> response) {
                             ArrayList<Music> music =new ArrayList<Music>();
                             music = response.body().getMusicses();
                             mAdapter = new RecyclerMusicesAdapter(response.body().getMusicses(), mCtx);
                             mRecyclerView.setAdapter(mAdapter);
                         }

                         @Override
                         public void onFailure(Call<Musics> call, Throwable t) {

                         }
                     }
        );
    }


}