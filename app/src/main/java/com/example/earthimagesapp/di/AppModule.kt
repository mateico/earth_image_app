package com.example.earthimagesapp.di

import android.app.Application
import androidx.room.Room
import com.example.earthimagesapp.data.local.Converters
import com.example.earthimagesapp.data.local.EarthImagesDatabase
import com.example.earthimagesapp.data.local.GsonParser
import com.example.earthimagesapp.data.remote.EarthImagesApi
import com.example.earthimagesapp.data.repository.DayDataRepositoryImpl
import com.example.earthimagesapp.data.repository.DayRepositoryImpl
import com.example.earthimagesapp.domain.DayDataRepository
import com.example.earthimagesapp.domain.DayRepository
import com.example.earthimagesapplication.BuildConfig
import com.google.gson.Gson
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
    fun provideDayRepository(
        db: EarthImagesDatabase,
        api: EarthImagesApi
    ): DayRepository{
        return DayRepositoryImpl(db, api)
    }

    @Provides
    @Singleton
    fun provideDayDataRepository(
        db: EarthImagesDatabase,
        api: EarthImagesApi
    ): DayDataRepository {
        return DayDataRepositoryImpl(db, api)
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
            okHttpClientBuilder.addInterceptor(logging)
        }

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
        )
            .addTypeConverter(Converters(GsonParser(Gson())))
            .fallbackToDestructiveMigration()
            .build()
    }

}