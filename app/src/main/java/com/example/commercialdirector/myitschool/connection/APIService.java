package com.example.commercialdirector.myitschool.connection;



import com.example.commercialdirector.myitschool.models.Likes;
import com.example.commercialdirector.myitschool.models.Musics;
import com.example.commercialdirector.myitschool.models.News;
import com.example.commercialdirector.myitschool.models.Podpiskas;
import com.example.commercialdirector.myitschool.models.Result;
import com.example.commercialdirector.myitschool.models.Result_Music;
import com.example.commercialdirector.myitschool.models.Result_Querry;
import com.example.commercialdirector.myitschool.models.Users;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface APIService {

    @Multipart
    @POST("upload.php/{user_name}")
    Call<Result> uploadMusic(@Part MultipartBody.Part file, @Part("user_name") RequestBody user_name);

    @FormUrlEncoded
    @POST("addMusic")
    Call<Result_Music> createMusic (
            @Field("name_music") String name_music,
            @Field("id_user") int id_user,
            @Field("path") String path,
            @Field("likei") int likei
    );

    @GET("musics/{id}")
    Call<Musics>getMusics(@Path("id") int id);

    @GET("user/{name}")
    Call<Users> getUserByName(@Path("name") String name);

    @GET("likes/{id_music}/{id_user}")
    Call<Likes> isLikeAdded (
            @Path("id_music") int id_music,
            @Path("id_user") int id_user

    );

    @GET("podpiskas/{id_user1}/{id_user2}")
    Call<Podpiskas> isPodpiskaAdded (
            @Path("id_user1") int id_user1,
            @Path("id_user2") int id_user2

    );

    @GET("podpischiki/{id_user2}")
    Call<Users>  podpischiki(
            @Path("id_user2") int id_user2
    );

    @GET("news/{id_user1}")
    Call<News> news (
            @Path("id_user1") int id_user1
    );



    @GET("podpisci/{id_user1}")
    Call<Users>  podpisci(
            @Path("id_user1") int id_user1
    );

    @FormUrlEncoded
    @POST("updateMusic/{id_music}")
    Call<Result_Music> updateMusic (
            @Path("id_music") int id_music,
            @Field("name_music") String name_music,
            @Field("id_user") int id_user,
            @Field("path") String path,
            @Field("likei") int likei
    );

    @FormUrlEncoded
    @POST("updateLogin/{id}")
    Call<Result_Querry> updateLogin (
            @Path("id") int id,
            @Field("name") String name
    );

    @FormUrlEncoded
    @POST("addLike")
    Call<Result_Querry> addLike (
            @Field("id_music") int id_music,
            @Field("id_user") int id_user
    );

    @FormUrlEncoded
    @POST("deleteLike")
    Call<Result_Querry> deleteLike (
            @Field("id_music") int id_music,
            @Field("id_user") int id_user
    );

    @FormUrlEncoded
    @POST("podpiska")
    Call<Result_Querry> podpiska (
            @Field("id_user1") int id_user1,
            @Field("id_user2") int id_user2
    );

    @FormUrlEncoded
    @POST("otpiska")
    Call<Result_Querry> otpiska (
            @Field("id_user1") int id_user1,
            @Field("id_user2") int id_user2
    );


}




