package com.vervyle.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SettingsActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var networkDataSource: MriqNetworkDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testParseMetadata() = runBlocking {
        val result = networkDataSource.getQuizByName("brain_anatomy")
    }
}