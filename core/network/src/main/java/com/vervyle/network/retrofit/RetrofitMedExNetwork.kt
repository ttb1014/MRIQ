package com.vervyle.network.retrofit

import android.content.Context
import com.vervyle.network.BuildConfig
import com.vervyle.network.MedExNetworkDataSource
import com.vervyle.network.utils.ZipConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private const val MED_EX_BASE_URl = BuildConfig.backend_url

// TODO: Add API calls
private interface RetrofitMedExNetworkApi {
    @GET("api")
    suspend fun getTopics(
        @Query("id") id: Int?,
    )

    @GET("api/frames/{id}")
    suspend fun getZipTest(
        @Path("id") id: String?,
    ): List<File>
}

@Singleton
internal class RetrofitMedExNetwork @Inject constructor(
    @ApplicationContext private val context: Context,
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : MedExNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(MED_EX_BASE_URl)
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addConverterFactory(
            ZipConverter.Factory(context.externalCacheDir ?: context.cacheDir)
        )
        .build()
        .create(RetrofitMedExNetworkApi::class.java)

    override suspend fun getMriData(id: Int?) {
        networkApi.getTopics(id)
    }

    override suspend fun getZipTest(id: String?): List<File> {
        return networkApi.getZipTest(id)
    }
}