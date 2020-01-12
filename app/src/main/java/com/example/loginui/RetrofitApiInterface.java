package com.example.loginui;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Bijay on 11/20/2019.
 */

public interface RetrofitApiInterface {

    @Headers("x-api-key: T0uRT!cket")
    @POST("userlogin")
    Call<ResponseBody> login(@Body RequestBody body);

}
