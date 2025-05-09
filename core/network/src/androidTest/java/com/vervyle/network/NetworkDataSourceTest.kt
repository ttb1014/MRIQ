package com.vervyle.network

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
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

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun testParseMetadata() = runBlocking {
        val id = "brain_anatomy"
        val result = networkDataSource.getQuizById(id)
        println(result)
        assert(result.name == id)
        assert(result.id == id)
        assert(result.structures.isNotEmpty())
        assert(result.axialAnnotatedImages.isNotEmpty())
        assert(result.coronalAnnotatedImages.isNotEmpty())
        assert(result.sagittalAnnotatedImages.isNotEmpty())
    }

    @Test
    fun testFilesExtractedToCacheDir() = runBlocking {
        val id = "brain_anatomy"
        val result = networkDataSource.getQuizById(id)

        val cacheDir = context.externalCacheDir ?: context.cacheDir
        val extractedFiles =
            cacheDir?.listFiles()?.filter { it.name.endsWith(".png") || it.name.endsWith(".jpg") }!!
        extractedFiles.forEach {
            Log.d("ZipTest", "Найден файл: ${it.absolutePath}, size=${it.length()}")
            assertTrue("Файл ${it.name} пустой", it.length() > 0)
        }
    }
}