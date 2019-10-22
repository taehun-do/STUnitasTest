package com.example.stunitastest.network

import com.example.stunitastest.common.Define
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ApiClient {
    companion object {
        private val instance = ApiClient()

        @Synchronized
        private fun getInstance(): ApiClient {
            return instance
        }

        fun getKakaoApi(): KakaoApi {
            return getInstance().kaKaoApi
        }
    }

    private val kaKaoApi: KakaoApi by lazy {
        createRetrofitApi(Define.Api.API_BASE_URL, KakaoApi::class.java)
    }

    private fun <T> createRetrofitApi(baseUrl: String, apiInterface: Class<T>, async: Boolean = true) =
        Retrofit.Builder().apply {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            baseUrl(baseUrl)
            client(OkHttpClient.Builder().addInterceptor(interceptor).build())
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(takeIf { async }?.let { RxJava2CallAdapterFactory.createAsync() } ?: RxJava2CallAdapterFactory.create() )
        }.build().create(apiInterface)

}