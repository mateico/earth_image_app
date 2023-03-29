package com.example.earthimagesapp.data.repository

import com.example.earthimagesapp.data.local.EarthImagesDatabase
import com.example.earthimagesapp.data.mapper.toDay
import com.example.earthimagesapp.data.mapper.toDayEntity
import com.example.earthimagesapp.data.remote.EarthImagesApi
import com.example.earthimagesapp.domain.EarthImagesRepository
import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EarthImagesRepositoryImpl @Inject constructor(
    private val api: EarthImagesApi,
    private val db: EarthImagesDatabase
): EarthImagesRepository {

    private val dayDao = db.dayDao

    override suspend fun getDays(): Flow<Resource<List<Day>>> {
        return flow {

            emit(Resource.Loading(true))

            val localDayListing = dayDao.getDayListing()
            emit(Resource.Success(
                data = localDayListing.map { it.toDay() }
            ))

            val remoteDayListings = try {
                api.getDays()
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Please check your network connection"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Something went wrong"))
                null
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error("Something went wrong"))
                null
            }

            remoteDayListings?.let { dayListings ->

                dayDao.insertDayListing(

                    dayListings.map { it.toDayEntity() }

                )

                emit(Resource.Success(
                    data = dayDao.getDayListing().map { it.toDay() }
                ))
            }

        }
    }

    override suspend fun insertDays(days: List<Day>) {
        dayDao.insertDayListing(
            days.map { it.toDayEntity() }
        )
    }
}