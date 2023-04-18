package com.example.momentsrecyclerview.ui

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.momentsrecyclerview.fake.FakeData
import com.example.momentsrecyclerview.fake.FakeLocalTweetsRepo
import com.example.momentsrecyclerview.fake.FakeLocalUserInfoRepo
import com.example.momentsrecyclerview.fake.FakeRemoteTweetsRepo
import com.example.momentsrecyclerview.fake.FakeRemoteUserInfoRepo
import com.example.momentsrecyclerview.rules.TestDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class MomentsViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private val application = mock(Application::class.java)

    @Mock
    private val context = mock(Context::class.java)

    private lateinit var viewModel: MomentsViewModel

    @Before
    fun setup() {
        viewModel = MomentsViewModel(
            application,
            FakeRemoteUserInfoRepo(),
            FakeRemoteTweetsRepo(),
            FakeLocalUserInfoRepo(),
            FakeLocalTweetsRepo()
        )
        `when`(application.applicationContext).thenReturn(context)
    }

    @Test
    fun momentsViewModel_init_verifySuccess() {
        runTest {
            assertEquals(STATUS.DONE, viewModel.status.value)
            assertEquals(viewModel.userInfo.value, FakeData.userInfo)
            assertEquals(viewModel.tweetsList.value, listOf(FakeData.tweet))
        }
    }

    @Test
    fun momentsViewModel_refreshData_verifySuccess() {
        runTest {
            viewModel.refreshData()
            assertEquals(STATUS.DONE, viewModel.status.value)
            assertEquals(viewModel.userInfo.value, FakeData.userInfo)
            assertEquals(viewModel.tweetsList.value, listOf(FakeData.tweet))
        }
    }
}
