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
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private const val MED_EX_BASE_URl = BuildConfig.backend_url

/**
 * Внутренний интерфейс, описывающий доступные запросы к серверу.
 */
private interface MriqNetworkApi {

    @GET("api/frames/{id}")
    suspend fun getDatasetArchiveById(
        @Path("id") id: String?,
    ): List<File>
}

/**
 * Класс для работы с API. На данный момент поддерживается получения набора данных zip-архивом.
 */
@Singleton
internal class RetrofitMriqNetwork @Inject constructor(
    @ApplicationContext private val context: Context,
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : MedExNetworkDataSource {

    // Инкапсулированный объект, совершающий запросы к серверу. Конвертирует zip-архив в список файлов автоматически.
    private val networkApi = Retrofit.Builder()
        .baseUrl(MED_EX_BASE_URl)
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addConverterFactory(
            // Добавляем конвертер для zip-архивов
            ZipConverter.Factory(context.externalCacheDir ?: context.cacheDir)
        )
        .build()
        .create(MriqNetworkApi::class.java)

    /**
     * Получение списка изображений для датасета по ID с сервера
     */
    override suspend fun getDatasetImagesById(id: String?): List<File> {
        return networkApi.getDatasetArchiveById(id)
    }
}