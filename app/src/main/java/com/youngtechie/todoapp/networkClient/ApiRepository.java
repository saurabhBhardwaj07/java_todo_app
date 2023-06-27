package com.youngtechie.todoapp.networkClient;

import com.google.gson.JsonObject;
import com.youngtechie.todoapp.modelResponse.ApiResponse;
import com.youngtechie.todoapp.modelResponse.BasicResponse;
import com.youngtechie.todoapp.modelResponse.TodoResponse;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiRepository {
    @POST("api/register")
    Call<BasicResponse> userRegister(
//            @Field("username") String username,
//            @Field("password") String password,
//            @Field("email") String email
            @Body HashMap<String, String> hashMap
    );

    @POST("api/login")
    Call<BasicResponse> userLogin(@Body HashMap<String, String> hashMap);


    @GET("api/user")
    Call<JsonObject> userProfile();

    @POST("todo/")
    Call<ApiResponse> addTodoTask(@Body HashMap<String, String> bodyData);

    @GET("todo")
    Call<JsonObject> fetchTodoTask();

    @PUT("todo/{id}")
    Call<JsonObject> updateTodoTask(@Path("id") String taskId, @Body HashMap<String, String> bodyData);

    @DELETE("todo/{id}")
    Call<JsonObject> deleteTodoTask(@Path("id") String taskId);

    @FormUrlEncoded
    @PUT("todo/{id}")
    Call<JsonObject> taskDone(@Path("id") String taskId, @Field("finished") boolean isFinished);

    @GET("todo")
    Call<JsonObject> fetchFinishedTodoTask(@Query("finished") boolean isFinished);

}
