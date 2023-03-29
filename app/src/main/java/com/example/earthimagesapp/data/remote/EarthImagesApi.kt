package com.example.earthimagesapp.data.remote

import com.example.earthimagesapp.data.remote.dto.DayDto
import retrofit2.http.GET

interface EarthImagesApi {

    @GET("all")
    suspend fun getDays(): List<DayDto>

    companion object {
        const val BASE_URL = "https://epic.gsfc.nasa.gov/api/enhanced/"
    }

}