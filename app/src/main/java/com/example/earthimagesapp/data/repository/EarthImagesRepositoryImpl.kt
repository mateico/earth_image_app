package com.example.earthimagesapp.data.repository

import com.example.earthimagesapp.data.local.EarthImagesDatabase
import com.example.earthimagesapp.data.mapper.toDay
import com.example.earthimagesapp.data.mapper.toDayEntity
import com.example.earthimagesapp.data.mapper.toImageData
import com.example.earthimagesapp.data.remote.EarthImagesApi
import com.example.earthimagesapp.data.remote.dto.DayDto
import com.example.earthimagesapp.domain.EarthImagesRepository
import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.domain.model.ImageData
import com.example.earthimagesapp.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EarthImagesRepositoryImpl @Inject constructor(
    private val api: EarthImagesApi,
    db: EarthImagesDatabase
) : EarthImagesRepository {

    private val dayDao = db.dayDao
    private val imageDataDao = db.imageDataDao

    override fun getDaysFromLocal(): Flow<List<Day>> =
        dayDao.getDaysStream().map { entityDays ->
            entityDays.map { it.toDay() }
        }.onEach {
            if (it.isEmpty()) getDaysFromRemote()
        }

    override suspend fun getDaysFromRemote() {
        api.getDays()
            .shuffled()
            .also { dayDtos ->
                dayDao.insertOrIgnoreDays(days = dayDtos.map(DayDto::toDayEntity))
            }
    }

    override suspend fun getImageDataByDayFromRemote() {


            dayDao.getDaysStream().collect { result ->
                var index = 0;
                while (index < result.size) {
                    val remoteImagesByDayListings = try {
                        api.getImagesData("date/${result[index++].date}")
                    } catch (e: IOException) {
                        e.printStackTrace()
                        null
                    } catch (e: HttpException) {
                        e.printStackTrace()
                        null
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }

                    remoteImagesByDayListings?.let { imageByDayListings ->
                        imageDataDao.insertImagesByDayListing(
                            imageByDayListings.map { it.toImageDataEntity() }
                        )
                    }


                }
            }

    }

    override suspend fun getImageDataByDayFromLocal(day: String): Flow<Resource<List<ImageData>>> {

        return flow {
            val localImagesByDayListing = imageDataDao.getImagesByDayListing(day)

            emit(Resource.Success(
                data = localImagesByDayListing.map { it.toImageData() }
            ))

            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun getListImagesToDownload(): Flow<Resource<List<String>>> {
        return flow {
            val imageEntities = imageDataDao.getImagesByDayListing()

            emit(Resource.Success(
                data = imageEntities.map {
                    "$IMAGE_URI_START${
                        DateUtils.formatDateToGetImage(
                            it.date
                        )
                    }$IMAGE_URI_MIDDLE${it.identifier}$IMAGE_TYPE"
                }

            ))

            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun getPhotoDataById(id: String): Flow<Resource<ImageData>> {
        return flow {
            val localImage = imageDataDao.getImageById(id)

            emit(
                Resource.Success(
                    data = localImage.toImageData()
                )
            )

            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun insertDays(days: List<Day>) {
        dayDao.insertOrIgnoreDays(
            days.map { it.toDayEntity() }
        )
    }
}