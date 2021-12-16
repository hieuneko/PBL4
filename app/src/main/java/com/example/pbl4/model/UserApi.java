package com.example.pbl4.model;

import com.example.pbl4.model.login.LoginResponse;
import com.example.pbl4.model.register.RegisterRequest;
import com.example.pbl4.model.register.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {
    @GET("users")
    Call<List<LoginResponse>> loginUser();

    @POST("users")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @PUT("users/{id}")
    Call<LoginResponse> putAccount(@Path("id") int id,@Body LoginResponse loginResponse);

    @PATCH("users/{id}")
    Call<LoginResponse> patchAccount(@Path("id") int id,@Body LoginResponse loginResponse);
}
