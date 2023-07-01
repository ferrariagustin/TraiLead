package com.aferrari.trailead.data.di

import android.content.Context
import com.aferrari.trailead.data.LocalDataSourceImpl
import com.aferrari.trailead.data.RemoteDataSourceImpl
import com.aferrari.trailead.data.db.AppDataBase
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object DataModule {

    @Provides
    fun provideRemoteDataSource(): RemoteDataSource = RemoteDataSourceImpl()

    @Provides
    fun provideLocalDataSource(@ApplicationContext context: Context): LocalDataSource {
        return LocalDataSourceImpl(AppDataBase.getInstance(context))
    }
}