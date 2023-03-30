package com.example.earthimagesapp.data.remote

import com.example.earthimagesapp.data.remote.dto.DayDto
import com.example.earthimagesapp.data.remote.dto.ImageDataDto
import retrofit2.http.GET
import retrofit2.http.Url

interface EarthImagesApi {

    @GET("all")
    suspend fun getDays(): List<DayDto>

    @GET
    suspend fun getImagesData(@Url url: String): List<ImageDataDto>

    companion object {
        const val BASE_URL = "https://epic.gsfc.nasa.gov/api/enhanced/"
    }

}