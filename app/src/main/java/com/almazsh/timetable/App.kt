package com.almazsh.timetable

import android.app.Application
import android.arch.persistence.room.Room
import android.support.v7.preference.PreferenceManager
import com.almazsh.timetable.db.MainDB
import com.almazsh.timetable.network.RetrofitService
import com.chibatching.kotpref.Kotpref
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this

        logAlm("App PreferenceManager.setDefaultValues")
        // TODO change prefs file name
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false)

        Kotpref.init(this)
//        Kotpref.gson = Gson()

        logAlm("Current group: ${AppPrefs.groupName}")

        initRetrofit()

        dataManager = DataManager()

        db = Room.databaseBuilder(this, MainDB::class.java, "main_db_1")
            .build()
    }

    private fun initRetrofit() {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
//                .connectTimeout(5, TimeUnit.SECONDS)
//                .writeTimeout(5, TimeUnit.SECONDS)
//                .readTimeout(5, TimeUnit.SECONDS)
            .build()

        retrofitService = Retrofit.Builder()
//            .baseUrl(API_ENDPOINT)
            .baseUrl(AppPrefs.serverBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RetrofitService::class.java)


    }


    companion object {

//        const val API_ENDPOINT = "http://10.0.2.2/api/"
//        const val API_ENDPOINT = "http://192.168.43.27/api/"
//        const val API_ENDPOINT = "http://192.168.0.101/api/"

        init {

        }

        lateinit var app: App

        lateinit var retrofitService: RetrofitService

        lateinit var dataManager: DataManager

        lateinit var db: MainDB
    }
}
