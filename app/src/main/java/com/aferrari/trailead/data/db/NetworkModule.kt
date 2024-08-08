package com.aferrari.trailead.data.db

import com.aferrari.trailead.notification.api.FcmAPI
import com.google.android.datatransport.runtime.dagger.Provides
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Singleton
object NetworkModule {

    private const val SERVER_KEY = "AIzaSyBgUqLaPqAWigZZC5A51aiH4ndVhNKsFc4"

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        // TODO: FIX issue from 401 Unauthorized
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor { chain ->
                val request = chain.request()
                val newRequest = request.newBuilder()
                    .addHeader("Authorization", "key=$SERVER_KEY")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): FcmAPI {
        return retrofit.create(FcmAPI::class.java)

    }
}