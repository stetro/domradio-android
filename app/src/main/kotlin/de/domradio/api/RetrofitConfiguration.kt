package de.domradio.api

import me.toptas.rssconverter.RssConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object RetrofitConfiguration {

    fun getHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }


    fun getRssRetrofit(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://www.domradio.de")
            .client(httpClient)
            .addConverterFactory(RssConverterFactory.create())
            .build()

    fun getRssService(retrofit: Retrofit): RssService =
        retrofit.create(RssService::class.java)


    fun getPlayerRetrofit(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://www.domradio.de")
            .client(httpClient)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()

    fun getStationService(retrofit: Retrofit): StationService =
        retrofit.create(StationService::class.java)

}