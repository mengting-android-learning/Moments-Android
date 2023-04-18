package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.fake.FakeData
import com.example.momentsrecyclerview.fake.FakeNetworkTweetsService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkTweetsRepositoryTest {
    @Test
    fun networkTweetsRepository_getTweetsList_verifySuccess() {
        runTest {
            val tweetsRepository = NetworkTweetsRepository(FakeNetworkTweetsService())
            val tweets = tweetsRepository.getTweetsList()
            assertEquals(listOf(FakeData.tweet), tweets)
        }
    }
}
