package com.example.earthimagesapp.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.earthimagesapp.domain.model.CentroIdCoordinates

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {

    @TypeConverter
    fun fromCoordinatesJson(json: String): CentroIdCoordinates? {
        return jsonParser.fromJson(json, CentroIdCoordinates::class.java)
    }

    @TypeConverter
    fun toCoordinatesJson(coord: CentroIdCoordinates?): String? {
        return jsonParser.toJson(coord, CentroIdCoordinates::class.java)
    }


}