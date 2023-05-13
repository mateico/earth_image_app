package com.example.earthimagesapp.data.repository

import com.example.earthimagesapp.data.local.DayEntity
import com.example.earthimagesapp.data.local.DayStatus
import com.example.earthimagesapp.data.local.EarthImagesDatabase
import com.example.earthimagesapp.data.mapper.toImageData
import com.example.earthimagesapp.data.remote.EarthImagesApi
import com.example.earthimagesapp.domain.DayDataRepository
import com.example.earthimagesapp.domain.model.ImageData
import com.example.earthimagesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DayDataRepositoryImpl @Inject constructor(
    private val db: EarthImagesDatabase,
    private val api: EarthImagesApi
): DayDataRepository {

    private val dayDao = db.dayDao
    private val imageDataDao = db.imageDataDao

    override suspend fun getImageDataByDayFromRemote() {

        dayDao.getDaysStream().collect { result ->
            var index = 0;
            while (index < result.size) {
                val remoteImagesByDayListings = try {
                    db.dayDao.updateDay(DayEntity(result[index].date, DayStatus.LOADING))
                    api.getImagesData("date/${result[index].date}")
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
                    db.dayDao.updateDay(DayEntity(result[index].date, DayStatus.WITH_DATA))
                    index++
                }
            }
        }
    }

    override suspend fun getImageDataByDayFromLocal(day: String): Flow<Resource<List<ImageData>>> {

        return flow {
            val localImagesByDayListing = imageDataDao.getImagesByDayListing(day)

            emit(
                Resource.Success(
                data = localImagesByDayListing.map { it.toImageData() }
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

}