package com.example.earthimagesapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.earthimagesapp.data.local.DayDao
import com.example.earthimagesapp.data.local.EarthImagesDatabase
import com.example.earthimagesapp.data.mapper.toDayEntity
import com.example.earthimagesapp.domain.model.Day
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class DayDaoTest {

    private lateinit var database: EarthImagesDatabase
    private lateinit var dayDao: DayDao

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            EarthImagesDatabase::class.java
        ).allowMainThreadQueries().build()

        dayDao = database.dayDao
    }

    @Test
    fun insertDay_returnsTrue() = runBlocking {
        val day = Day("20220-03-22")
        dayDao.insertOrIgnoreDays(listOf(day.toDayEntity()))


        val result = dayDao.getDaysStream()

        assertEquals(result.size, 1)


    }

    @After
    fun closeDatabase() {
        database.close()
    }

}