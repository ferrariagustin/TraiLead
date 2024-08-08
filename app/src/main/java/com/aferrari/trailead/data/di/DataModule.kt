package com.aferrari.trailead.data.di

import com.aferrari.trailead.data.RemoteDataSourceImpl
import com.aferrari.trailead.data.db.NetworkModule
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object DataModule {

    @Provides
    fun provideRemoteDataSource(): RemoteDataSource = RemoteDataSourceImpl(
        NetworkModule.provideApiService(
            NetworkModule.provideRetrofit(NetworkModule.provideOkHttpClient())
        )
    )

//    @Provides
//    fun provideLocalDataSource(@ApplicationContext context: Context): LocalDataSource {
//        return LocalDataSourceImpl(AppDataBase.getInstance(context))
//    }
}