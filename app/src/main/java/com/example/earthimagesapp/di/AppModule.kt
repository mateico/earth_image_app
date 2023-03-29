package com.example.earthimagesapp.di

import android.app.Application
import androidx.room.Room
import com.example.earthimagesapp.data.local.EarthImagesDatabase
import com.example.earthimagesapp.data.remote.EarthImagesApi
import com.example.earthimagesapp.data.repository.EarthImagesRepositoryImpl
import com.example.earthimagesapp.domain.EarthImagesRepository
import com.example.earthimagesapplication.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideEarthImagesRepository(
        db: EarthImagesDatabase,
        api: EarthImagesApi
    ): EarthImagesRepository {
        return EarthImagesRepositoryImpl(api, db)
    }



    @Provides
    @Singleton
    fun provideEarthImagesApi(): EarthImagesApi {

        val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logging)}

        return Retrofit.Builder()
            .baseUrl(EarthImagesApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideEarthImagesDatabase(app: Application): EarthImagesDatabase {
        return Room.databaseBuilder(
            app,
            EarthImagesDatabase::class.java,
            "earthimagesdb.db"
        ).build()
    }

}