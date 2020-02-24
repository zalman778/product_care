package com.hwx.productcare.di.modules


import com.hwx.productcare.BuildConfig
import com.hwx.productcare.api.FnsApi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory

@Module
class KktApiModule {

    private val timeOut = 20L //20Secs//

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            readTimeout(timeOut, TimeUnit.SECONDS)
            connectTimeout(timeOut, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(this)
                }
            }
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: Lazy<OkHttpClient>, gson: Gson): Retrofit {
        return Retrofit.Builder()
              .baseUrl("https://proverkacheka.nalog.ru:9999")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .callFactory(client.get())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): FnsApi {
        return retrofit.create(FnsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }
}