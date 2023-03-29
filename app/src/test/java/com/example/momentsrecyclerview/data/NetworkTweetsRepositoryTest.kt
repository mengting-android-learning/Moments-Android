package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.fake.FakeData
import com.example.momentsrecyclerview.fake.FakeTweetsService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkTweetsRepositoryTest {
    @Test
    fun networkTweetsRepository_getTweetsList_verifySuccess() {
        runTest {
            val tweetsRepository = NetworkTweetsRepository(FakeTweetsService())
            val tweets = tweetsRepository.getTweetsList()
            assertEquals(listOf(FakeData.networkTweet), tweets)
        }
    }
}
