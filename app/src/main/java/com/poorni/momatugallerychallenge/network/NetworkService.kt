package com.poorni.momatugallerychallenge.network

import com.poorni.momatugallerychallenge.model.Photo
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface NetworkService {

    @GET("list")
    fun getNews(@Query("page") page: Int, @Query("limit") limit: Int): Single<List<Photo>>


    companion object {
        fun getService(): NetworkService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
            val client =
                OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://picsum.photos/v2/")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(NetworkService::class.java)
        }
    }
}