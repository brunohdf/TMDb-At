package com.arctouch.codechallenge

import android.app.Application
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.detail.DetailViewModel
import com.arctouch.codechallenge.home.HomeViewModel
import com.arctouch.codechallenge.repository.TMDbRepository
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Suppress("unused")
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(modules)
        }
    }

    private val modules = module {

        single { OkHttpClient.Builder().build() as OkHttpClient }
        single { MoshiConverterFactory.create() as Converter.Factory }
        single { RxJava2CallAdapterFactory.create() as CallAdapter.Factory }

        single {
            Retrofit.Builder()
                    .baseUrl(TmdbApi.URL)
                    .client(get())
                    .addConverterFactory(get())
                    .addCallAdapterFactory(get())
                    .build()
                    .create(TmdbApi::class.java)
        }

        single { HomeViewModel(get()) }
        single { TMDbRepository(get(), get()) }
        single { DetailViewModel(get()) }

        single { Cache() }
    }

}