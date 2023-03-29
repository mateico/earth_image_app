package com.example.earthimagesapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.earthimagesapp.data.mapper.toProductListingEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ProductDaoTest {

    private lateinit var database: ProductDatabase
    private lateinit var productDao: ProductDao
    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ProductDatabase::class.java
        ).allowMainThreadQueries().build()

        productDao = database.dao
    }

    @Test
    fun insertWord_returnsTrue() = runBlocking {
        val product = ProductListing("asdfasf", "asdfadsf", 1, "asdfasdf", 3.3, "fasdfaf")
        productDao.insertProductListing(listOf(product.toProductListingEntity()))


            val result = productDao.getProductListing()

        assertEquals(result.size, 1)


    }

    @After
    fun closeDatabase() {
        database.close()
    }

}