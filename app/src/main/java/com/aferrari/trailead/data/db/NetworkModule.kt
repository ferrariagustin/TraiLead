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

    internal const val SERVER_KEY: String = "e91f6024fe71eebe5eb3155a6000ef1123c6c1c6"
//    internal const val SERVER_KEY: String = "BJVXDXNdJWU5ssf36B9HpcXde-AcjCGVsfTc-I1UBv1lK5uqlhFkvYsjatr4pnNXd1wqSSdy60JVpSVvD-D7bDs "

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        // TODO: FIX issue from 401 Unauthorized
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build()
                it.proceed(request)
            }
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://0.0.0.0:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): FcmAPI {
        return retrofit.create(FcmAPI::class.java)

    }
}