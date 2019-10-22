package com.example.stunitastest.network

import com.example.stunitastest.common.Define
import com.example.stunitastest.data.ApiListData
import com.example.stunitastest.data.ImageItem
import io.reactivex.Single
import retrofit2.http.*

interface KakaoApi {

    @GET(Define.Api.API_PATH_SEARCH_IMAGE)
    fun getImages(@Header("Authorization") kakaoKey: String = "KakaoAK a5c3e9777fb58155e4a9f0589261fb75",
                  @Query(Define.Api.API_PARAM_NAME_QUERY) query: String,
                  @Query(Define.Api.API_PARAM_NAME_PAGE) page: Int,
                  @Query(Define.Api.API_PARAM_NAME_SIZE) size: Int = Define.Api.API_PARAM_VALUE_SIZE): Single<ApiListData<ImageItem>>
}