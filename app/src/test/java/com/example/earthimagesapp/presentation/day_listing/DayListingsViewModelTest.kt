package com.example.earthimagesapp.presentation.day_listing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.earthimagesapp.domain.model.Day
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DayListingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    lateinit var dayListingsViewModel: DayListingsViewModel
    lateinit var fakeRepository: FakeEarthImagesRepository

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeEarthImagesRepository()


        val daysToInsert = mutableListOf<Day>()
        ('a'..'z').forEachIndexed { index, c ->
            daysToInsert.add(
                Day(
                    date = "date $c"
                )
            )
        }
        runBlocking {
            fakeRepository.insertDays(daysToInsert)
        }

        //dayListingsViewModel = DayListingsViewModel(fakeRepository)
    }

    @Test
    fun `Starts with refresh set to false`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            TestCase.assertEquals(false, dayListingsViewModel.state.isRefreshing)
        } finally {
            Dispatchers.resetMain()
        }
    }


}