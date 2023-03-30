package com.example.earthimagesapp.data.repository

import com.example.earthimagesapp.data.local.EarthImagesDatabase
import com.example.earthimagesapp.data.mapper.toDay
import com.example.earthimagesapp.data.mapper.toDayEntity
import com.example.earthimagesapp.data.mapper.toImageData
import com.example.earthimagesapp.data.remote.EarthImagesApi
import com.example.earthimagesapp.domain.EarthImagesRepository
import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.domain.model.ImageData
import com.example.earthimagesapp.util.DateUtils
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
    db: EarthImagesDatabase
) : EarthImagesRepository {

    private val dayDao = db.dayDao
    private val imageDataDao = db.imageDataDao

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

    override suspend fun getImageDataByDayFromRemote(): Flow<Resource<Int>> {

        return flow {
            val localDayListing = dayDao.getDayListing()
            var index = 0;
            while (index < localDayListing.size) {
                val remoteImagesByDayListings = try {
                    api.getImagesData("date/${localDayListing[index++].date}")
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

                emit(
                    Resource.Success(
                        data = index
                    )
                )
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
                    "https://epic.gsfc.nasa.gov/archive/enhanced/${
                        DateUtils.formatDateToGetImage(
                            it.date
                        )
                    }/png/epic_RGB_${it.identifier}.png"
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
        dayDao.insertDayListing(
            days.map { it.toDayEntity() }
        )
    }
}