package com.vervyle.local

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vervyle.network.MriqNetworkDataSource
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
class TestSyncWithRemote {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var networkDataSource: MriqNetworkDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    @Deprecated("do not use this module")
    fun testSync() = runBlocking {
        val id = "brain_anatomy"
        val result = networkDataSource.getQuizById(id)
    }
}