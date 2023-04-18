package com.example.momentsrecyclerview.ui

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.momentsrecyclerview.fake.FakeData
import com.example.momentsrecyclerview.fake.FakeTweetsRepository
import com.example.momentsrecyclerview.fake.FakeUserInfoRepository
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

    @Before
    fun setup() {
        `when`(application.applicationContext).thenReturn(context)
    }

    @Test
    fun momentsViewModel_init_verifySuccess() {
        runTest {
            val viewModel = MomentsViewModel(
                application,
                userInfoRepository = FakeUserInfoRepository(),
                tweetsRepository = FakeTweetsRepository()
            )
            assertEquals(STATUS.DONE, viewModel.status.value)
            assertEquals(viewModel.userInfo.value, FakeData.userInfo)
            assertEquals(viewModel.tweetsList.value, listOf(FakeData.tweet))
        }
    }

    @Test
    fun momentsViewModel_refreshData_verifySuccess() {
        runTest {
            val viewModel = MomentsViewModel(
                application,
                userInfoRepository = FakeUserInfoRepository(),
                tweetsRepository = FakeTweetsRepository()
            )
            viewModel.refreshData()
            assertEquals(STATUS.DONE, viewModel.status.value)
            assertEquals(viewModel.userInfo.value, FakeData.userInfo)
            assertEquals(viewModel.tweetsList.value, listOf(FakeData.tweet))
        }
    }
}
