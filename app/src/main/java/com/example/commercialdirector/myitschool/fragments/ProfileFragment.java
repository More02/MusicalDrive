package com.example.commercialdirector.myitschool.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commercialdirector.myitschool.Activity.CustomsActivity;
import com.example.commercialdirector.myitschool.Adapters.RecyclerMusicesAdapter;
import com.example.commercialdirector.myitschool.Adapters.RecyclerUsersAdapter;
import com.example.commercialdirector.myitschool.FileManager;
import com.example.commercialdirector.myitschool.Helper.SessionManager;
import com.example.commercialdirector.myitschool.models.Music;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.models.Musics;
import com.example.commercialdirector.myitschool.models.Result;
import com.example.commercialdirector.myitschool.connection.APIService;
import com.example.commercialdirector.myitschool.connection.AppConfig;
import com.example.commercialdirector.myitschool.models.Result_Music;
import com.example.commercialdirector.myitschool.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

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
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerMusicesAdapter mAdapter;
    private FloatingActionButton AddMusic;
    private static final String FILENAME = "File";
    private static final int REQUEST = 0;
    private String filename;
    private SessionManager sessionmanager;
    private ImageView userPhotoimageView;
    private Button btnUserPhoto;
    private Button makephoto;
    private final int Pick_image = 1;
    private static final int CAMERA_PIC_REQUEST = 5;
    private static int TAKE_PICTURE = 1;
    private Uri mOutputFileUri;
    private TextView userMainId;
    private User user;
    private Button btnCustoms;



    int i = 1;


    
    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_profile, container, false);


          userMainId = (TextView)v.findViewById(R.id.userMainId);

        SessionManager sessionManager = new SessionManager(getActivity());
        String d =  sessionManager.getUser().getName();
        userMainId.setText(d);





        userPhotoimageView = (ImageView)v.findViewById(R.id.userPhotoimageView);
        btnUserPhoto = (Button)v.findViewById(R.id.btnUserPhoto);
        btnUserPhoto.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
               photoPickerIntent.setType("image/*");
               startActivityForResult(photoPickerIntent, Pick_image);
               i = 0;

           }
       });

        btnCustoms = (Button) v.findViewById(R.id.btnCustoms);
        btnCustoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),
                        CustomsActivity.class);
                startActivity(i);

            }
        });



        makephoto = (Button)v.findViewById(R.id.makephoto);
        makephoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                i = 2;
                try {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                    File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
                }
                catch (ActivityNotFoundException cant) {
                    String errorMessage = "Ваше устройство не поддерживает работу с камерой!";
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                }

            }
        });

        mRecyclerView=(RecyclerView)v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Retrofit allMusic = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = allMusic.create(APIService.class);
        sessionmanager = new SessionManager(getActivity());
        User user = sessionmanager.getUser();
        int id = user.getId();
        Call<Musics> call = service.getMusics(user.getId());
        call.enqueue(new Callback<Musics>() {
                         @Override
                         public void onResponse(Call<Musics> call, Response<Musics> response) {
                             ArrayList<Music> music =new ArrayList<Music>();
                             music = response.body().getMusicses();
                             mAdapter = new RecyclerMusicesAdapter(response.body().getMusicses(), getActivity());
                             mRecyclerView.setAdapter(mAdapter);
                         }

                         @Override
                         public void onFailure(Call<Musics> call, Throwable t) {

                         }
                     }
        );

        AddMusic = (FloatingActionButton)v. findViewById(R.id.AddMusic);
        AddMusic.setOnClickListener(new View.OnClickListener() {


            public void onClick (View v) {
                Intent i = new Intent(getActivity(), FileManager.class);
                startActivityForResult(i,REQUEST);
            }
        });
        return v;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            if (i == 0 ) {
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

            if (requestCode == REQUEST)
            {
                filename = data.getStringExtra(FILENAME);
                Toast.makeText(getActivity(),filename, Toast.LENGTH_SHORT).show();
                uploadMusic(filename);

            }
            if(requestCode== CAMERA_PIC_REQUEST) {
                Bitmap thumbnail=(Bitmap) data.getExtras().get("data");
                userPhotoimageView.setImageBitmap(thumbnail);
            }


        }
    }



    private void uploadMusic(final String filename){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Загрузка файла");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final APIService service = retrofit.create(APIService.class);

        final File file = new File (filename);
        User user = sessionmanager.getUser();
        final String name = user.getName();
        final int id_user = user.getId();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);


        RequestBody user_name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        Call<Result> resultCall = service.uploadMusic(body,  user_name);
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success")) {
                        Toast.makeText(getActivity(), "Загрузка произошла успешно", Toast.LENGTH_LONG).show();
                        addMusic(file.getName(), id_user, AppConfig.BASE_URL + "include/music/folder" + name, 0);
                    }
                    else
                        Toast.makeText(getActivity(), "При загрузке произошла ошибка", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(),"При загрузке произошла ошибка", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addMusic (String name_music, int id_user, String path, int likei){

        Retrofit addmusic = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final APIService service = addmusic.create(APIService.class);
        Music music = new Music (name_music, id_user, path, likei);
        Log.d("tr", name_music);
        Log.d("tr", Integer.toString(id_user));
        Log.d("tr", path);
        Log.d("tr", Integer.toString(likei));
        Call<Result_Music> call = service.createMusic(
                music.getName_music(),
                music.getId_user(),
                music.getPath(),
                music.getLikei()

        );

        call.enqueue(new Callback<Result_Music>() {
            @Override
            public void onResponse(Call<Result_Music> call, Response<Result_Music> response) {
                Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                update();
            }

            @Override
            public void onFailure(Call<Result_Music> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }



    public void update(){
        Retrofit allMusic = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_PUBLIC)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = allMusic.create(APIService.class);
        sessionmanager = new SessionManager(getActivity());
        User user = sessionmanager.getUser();
        int id = user.getId();
        Call<Musics> call = service.getMusics(user.getId());
        call.enqueue(new Callback<Musics>() {
                         @Override
                         public void onResponse(Call<Musics> call, Response<Musics> response) {
                             ArrayList<Music> music =new ArrayList<Music>();
                             music = response.body().getMusicses();
                             mAdapter = new RecyclerMusicesAdapter(response.body().getMusicses(), getActivity());
                             mRecyclerView.setAdapter(mAdapter);
                         }

                         @Override
                         public void onFailure(Call<Musics> call, Throwable t) {

                         }
                     }
        );
    }


}
