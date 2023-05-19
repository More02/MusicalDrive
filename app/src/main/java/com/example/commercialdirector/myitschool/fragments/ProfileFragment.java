package com.example.commercialdirector.myitschool.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commercialdirector.myitschool.Activity.CustomActivity;
import com.example.commercialdirector.myitschool.Adapters.RecyclerMusiceAdapter;
import com.example.commercialdirector.myitschool.FileManager;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.models.Music;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.models.Musics;
import com.example.commercialdirector.myitschool.models.Result;
import com.example.commercialdirector.myitschool.connection.APIService;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.models.ResultMusic;
import com.example.commercialdirector.myitschool.models.User;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerMusiceAdapter mAdapter;
    private static final String FILENAME = "File";
    private static final int REQUEST = 0;
    private SessionManager sessionmanager;
    private ImageView userPhotoimageView;
    private final int Pick_image = 1;
    private static final int CAMERA_PIC_REQUEST = 5;
    private static final int TAKE_PICTURE = 1;
    private Uri mOutputFileUri;
    private User user;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    int i = 1;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView userMainId = (TextView) v.findViewById(R.id.userMainId);
        SessionManager sessionManager = new SessionManager(getActivity());
        String d = sessionManager.getUser().getName();
        userMainId.setText(d);

        userPhotoimageView = (ShapeableImageView) v.findViewById(R.id.userPhotoimageView);
        Button btnUserPhoto = (Button) v.findViewById(R.id.btnUserPhoto);
        btnUserPhoto.setOnClickListener(v1 -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Pick_image);
            i = 0;

        });

        Button btnCustoms = (Button) v.findViewById(R.id.btnCustoms);
        btnCustoms.setOnClickListener(v12 -> {
            Intent i = new Intent(getContext(), CustomActivity.class);
            startActivity(i);

        });


        Button makePhoto = (Button) v.findViewById(R.id.makephoto);
        makePhoto.setOnClickListener(v13 -> {
            i = 2;
            try {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
            } catch (ActivityNotFoundException cant) {
                String errorMessage = "Ваше устройство не поддерживает работу с камерой!";
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }

        });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Retrofit allMusic = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();

        APIService service = allMusic.create(APIService.class);
        sessionmanager = new SessionManager(getActivity());
        User user = sessionmanager.getUser();
        int id = user.getId();
        Call<Musics> call = service.getMusics(user.getId());
        call.enqueue(new Callback<Musics>() {
            @Override
            public void onResponse(Call<Musics> call, Response<Musics> response) {
                mAdapter = new RecyclerMusiceAdapter(response.body().getMusics(), getActivity());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<Musics> call, Throwable t) {

            }
        });

        FloatingActionButton addMusic = (FloatingActionButton) v.findViewById(R.id.AddMusic);
        addMusic.setOnClickListener(v14 -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                Intent i = new Intent(getActivity(), FileManager.class);
                startActivityForResult(i, REQUEST);
            }
        });
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {// если пользователь закрыл запрос на разрешение, не дав ответа, массив grantResults будет пустым
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(getActivity(), FileManager.class);
                startActivityForResult(i, REQUEST);
            } else {
                String errorMessage = "Для добавления нового файла необходимо предоставить разрешение на доступ к файловой системе";
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (i == 0) {
                try {

                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    userPhotoimageView.setImageBitmap(selectedImage);
                    //дописать
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == REQUEST) {
                String filename = data.getStringExtra(FILENAME);
                Toast.makeText(getActivity(), filename, Toast.LENGTH_SHORT).show();
                uploadMusic(filename);

            }
            if (requestCode == CAMERA_PIC_REQUEST) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                userPhotoimageView.setImageBitmap(thumbnail);
            }
        }
    }

    private void uploadMusic(final String filename) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Загрузка файла");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        final APIService service = retrofit.create(APIService.class);

        final File file = new File(filename);
        User user = sessionmanager.getUser();
        final String name = user.getName();
        final int id_user = user.getId();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);


        RequestBody user_name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        Call<Result> resultCall = service.uploadMusic(body, user_name);
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success")) {
                        Toast.makeText(getActivity(), "Загрузка произошла успешно", Toast.LENGTH_LONG).show();
                        addMusic(file.getName(), id_user, AppConfig.BASE_URL + "include/music/folder" + name, 0);
                    } else
                        Toast.makeText(getActivity(), "При загрузке произошла ошибка", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "При загрузке произошла ошибка", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addMusic(String name_music, int id_user, String path, int likei) {

        Retrofit addMusic = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();
        final APIService service = addMusic.create(APIService.class);
        Music music = new Music(name_music, id_user, path, likei);
        Log.d("tr", name_music);
        Log.d("tr", Integer.toString(id_user));
        Log.d("tr", path);
        Log.d("tr", Integer.toString(likei));
        Call<ResultMusic> call = service.createMusic(music.getNameMusic(), music.getIdUser(), music.getPath(), music.getIdLike()

        );

        call.enqueue(new Callback<ResultMusic>() {
            @Override
            public void onResponse(Call<ResultMusic> call, Response<ResultMusic> response) {
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                update();
            }

            @Override
            public void onFailure(Call<ResultMusic> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void update() {
        Retrofit allMusic = new Retrofit.Builder().baseUrl(AppConfig.BASE_PUBLIC).addConverterFactory(GsonConverterFactory.create()).build();

        APIService service = allMusic.create(APIService.class);
        sessionmanager = new SessionManager(getActivity());
        User user = sessionmanager.getUser();
        int id = user.getId();
        Call<Musics> call = service.getMusics(user.getId());
        call.enqueue(new Callback<Musics>() {
            @Override
            public void onResponse(Call<Musics> call, Response<Musics> response) {
                mAdapter = new RecyclerMusiceAdapter(response.body().getMusics(), getActivity());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<Musics> call, Throwable t) {

            }
        });
    }

}
