package com.vervyle.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vervyle.database.dao.DatasetDao
import com.vervyle.database.model.DatasetEntity
import com.vervyle.network.MriqNetworkDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DatabaseInstrumentedTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test")
    internal lateinit var database: MriqDatabase
    internal lateinit var dao: DatasetDao

    @Inject
    private lateinit var network: MriqNetworkDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.datasetDao()
    }

    @Test
    fun testDatasetDao() = runBlocking {
        val datasetName = "dataset_brain_new"
        val dataset = DatasetEntity(name = datasetName)
        dao.insertDataset(dataset)

        val result = dao.getDatasetByName(datasetName)
        assert(result != null)
        assert(result!!.name == datasetName)
    }

    // TODO: написать тест
    @Test
    fun testAggregatesDao() = runBlocking {
        
    }
}