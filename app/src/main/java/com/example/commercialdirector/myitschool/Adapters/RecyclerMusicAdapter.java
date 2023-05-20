package com.example.commercialdirector.myitschool.Adapters;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
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
import com.example.commercialdirector.myitschool.models.Like;
import com.example.commercialdirector.myitschool.models.Likes;
import com.example.commercialdirector.myitschool.models.Media;
import com.example.commercialdirector.myitschool.models.Music;
import com.example.commercialdirector.myitschool.models.Musics;
import com.example.commercialdirector.myitschool.models.ResultMusic;
import com.example.commercialdirector.myitschool.models.ResultQuerry;
import com.example.commercialdirector.myitschool.models.User;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RecyclerMusicAdapter extends RecyclerView.Adapter<RecyclerMusicAdapter.ViewHolder> {

    private final ArrayList<Music> mDataset;
    private final Context mCtx;
    private Media media;
    private RecyclerView mRecyclerView;
    private RecyclerMusicAdapter mAdapter;
    private boolean isLikeSet;

    public RecyclerMusicAdapter(ArrayList<Music> dataset, Context mCtx) {
        mDataset = dataset;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public RecyclerMusicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).getNameMusic());
        int idUser = mDataset.get(position).getIdUser();
        int idMusic = mDataset.get(position).getIdMusic();
        isLikeSet = false;
        Like like = new Like(idMusic, idUser);
        new MTask(holder.likeBtn).execute(like);
        holder.numberLikes.setText(Integer.toString(mDataset.get(position).getIdLike()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTextView;
        private final CheckBox likeBtn;
        private final TextView numberLikes;

        public ViewHolder(View v) {
            super(v);

            CardView cardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.tv_recycler_item);
            ImageButton playBtn = (ImageButton) v.findViewById(R.id.playbt);
            ImageButton pauseBtn = (ImageButton) v.findViewById(R.id.pausebt);
            ImageButton stopBtn = (ImageButton) v.findViewById(R.id.stopbt);
            likeBtn = (CheckBox) v.findViewById(R.id.likebt);
            numberLikes = (TextView) v.findViewById(R.id.numberlikes);
            playBtn.setOnClickListener(this);
            pauseBtn.setOnClickListener(this);
            stopBtn.setOnClickListener(this);
            cardView.setOnClickListener(this);
            likeBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.playbt: {
                    releaseMP();
                    String path = mDataset.get(getPosition()).getPath();
                    String music = mDataset.get(getPosition()).getNameMusic();
                    media = new Media(path + "/" + music, mCtx);
                    media.play();
//                        pausebt.setEnabled(true);
//                        stopbt.setEnabled(true);
//                        playbt.setEnabled(false);
                }
                ;
                break;
                case R.id.pausebt: {
                    media.pause();
                    //   pausebt.setEnabled(false);
//                            stopbt.setEnabled(false);
//                            playbt.setEnabled(true);

                }
                ;
                break;
                case R.id.stopbt: {
                    media.stop();
                    //pausebt.setEnabled(false);
//                        stopbt.setEnabled(false);
//                        playbt.setEnabled(true);
                }
                ;
                break;
                case R.id.likebt: {
                    int like = mDataset.get(getPosition()).getIdLike();
                    //numberlikes.setText(likei);
                    String name_music = mDataset.get(getPosition()).getNameMusic();
                    int id_user = mDataset.get(getPosition()).getIdUser();
                    String path = mDataset.get(getPosition()).getPath();
                    int id_music = mDataset.get(getPosition()).getIdMusic();
                    if (likeBtn.isChecked()) {
                        like++;
                        updateMusic(name_music, id_user, path, like, id_music);
                        addLike(id_music, id_user);
                    } else {
                        like--;
                        updateMusic(name_music, id_user, path, like, id_music);
                        deleteLike(id_music, id_user);
                    }

                }
                ;
                break;
            }

        }

        private void releaseMP() {
            if (media != null) {
                try {
                    media.releaseMedia();
                    media = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateMusic(String nameMusic, int idUser, String path, int like, int idMusic) {

        Retrofit updateMusic = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();
        final APIService service = updateMusic.create(APIService.class);
        Music music = new Music(idMusic, nameMusic, idUser, path, like);
        Log.d("tr", nameMusic);
        Log.d("tr", Integer.toString(idUser));
        Log.d("tr", path);
        Log.d("tr", Integer.toString(like));
        Call<ResultMusic> call = service.updateMusic(music.getIdMusic(), music.getNameMusic(), music.getIdUser(), music.getPath(), music.getIdLike()

        );

        call.enqueue(new Callback<ResultMusic>() {
            @Override
            public void onResponse(Call<ResultMusic> call, Response<ResultMusic> response) {
                //   Toast.makeText(mCtx,response.body().getMessage(),Toast.LENGTH_LONG).show();
                //   update();
            }

            @Override
            public void onFailure(Call<ResultMusic> call, Throwable t) {
                // Toast.makeText(mCtx,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addLike(int idMusic, int idUser) {

        Retrofit addLike = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();
        final APIService service = addLike.create(APIService.class);
        Like like = new Like(idMusic, idUser);

        Log.d("tr", Integer.toString(idMusic));
        Log.d("tr", Integer.toString(idUser));
        Call<ResultQuerry> call = service.addLike(like.getIdMusic(), like.getIdUser());

        call.enqueue(new Callback<ResultQuerry>() {
            @Override
            public void onResponse(Call<ResultQuerry> call, Response<ResultQuerry> response) {
                //  Toast.makeText(mCtx,response.body().getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResultQuerry> call, Throwable t) {
                //  Toast.makeText(mCtx,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteLike(int idMusic, int idUser) {

        Retrofit deleteLike = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();
        final APIService service = deleteLike.create(APIService.class);
        Like like = new Like(idMusic, idUser);

        Log.d("tr", Integer.toString(idMusic));
        Log.d("tr", Integer.toString(idUser));
        Call<ResultQuerry> call = service.deleteLike(like.getIdMusic(), like.getIdUser());

        call.enqueue(new Callback<ResultQuerry>() {
            @Override
            public void onResponse(Call<ResultQuerry> call, Response<ResultQuerry> response) {
                // Toast.makeText(mCtx,response.body().getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResultQuerry> call, Throwable t) {
                // Toast.makeText(mCtx,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    class MTask extends AsyncTask<Like, Void, Likes> {

        private final CheckBox checkBox;

        public MTask(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        @Override
        protected Likes doInBackground(Like... params) {
            Likes like = new Likes();
            try {
                Retrofit isLikeAdded = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();
                APIService service = isLikeAdded.create(APIService.class);
                Call<Likes> call = service.isLikeAdded(params[0].getIdMusic(), params[0].getIdUser());

                like = service.isLikeAdded(params[0].getIdMusic(), params[0].getIdUser()).execute().body();
                if (like.getLikes().size() == 0) isNotLike();
                else isLike();
            } catch (IOException e) {
                Log.d("but", e.getMessage());
            }
            return like;
        }

        @Override
        protected void onPostExecute(Likes like) {

            if (like.getLikes().size() != 0) {
                checkBox.setChecked(true);
            }
        }
    }

    private void isLike() {
        isLikeSet = true;
    }

    private void isNotLike() {
        isLikeSet = true;
    }

    public void update() {
        Retrofit allMusic = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();

        APIService service = allMusic.create(APIService.class);
        SessionManager sessionmanager = new SessionManager(mCtx);
        User user = sessionmanager.getUser();
        int id = user.getId();
        Call<Musics> call = service.getMusics(user.getId());
        call.enqueue(new Callback<Musics>() {
            @Override
            public void onResponse(Call<Musics> call, Response<Musics> response) {
                mAdapter = new RecyclerMusicAdapter(response.body().getMusics(), mCtx);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<Musics> call, Throwable t) {

            }
        });
    }

}