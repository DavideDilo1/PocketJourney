package com.example.pocketjourney

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ClientNetwork {

    val interceptor = HttpLoggingInterceptor();
    val client = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(interceptor).build()

    val retrofit: ServerAPI by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/webmobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ServerAPI::class.java)
    }

}