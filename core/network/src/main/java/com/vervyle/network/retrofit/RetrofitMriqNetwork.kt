package com.vervyle.network.retrofit

import android.content.Context
import com.vervyle.network.BuildConfig
import com.vervyle.network.MriqNetworkDataSource
import com.vervyle.network.model.QuizDto
import com.vervyle.network.util.ZipToQuizDtoConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

private const val MED_EX_BASE_URl = BuildConfig.backend_url

/**
 * Внутренний интерфейс, описывающий доступные запросы к серверу.
 */
private interface MriqNetworkApi {

    @GET("api/frames/{id}")
    suspend fun getQuizArchiveById(
        @Path("id") id: String?,
    ): QuizDto
}

/**
 * Класс для работы с API. На данный момент поддерживается получения набора данных zip-архивом.
 */
@Singleton
internal class RetrofitMriqNetwork @Inject constructor(
    @ApplicationContext private val context: Context,
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : MriqNetworkDataSource {

    // Инкапсулированный объект, совершающий запросы к серверу. Конвертирует zip-архив в список файлов автоматически.
    private val networkApi = Retrofit.Builder()
        .baseUrl(MED_EX_BASE_URl)
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addConverterFactory(
            // Добавляем конвертер для zip-архивов
            ZipToQuizDtoConverter.Factory(context.externalCacheDir ?: context.cacheDir)
        )
        .build()
        .create(MriqNetworkApi::class.java)

    /**
     * Получение викторины по ID с сервера
     */
    override suspend fun getQuizByName(id: String): QuizDto {
        return networkApi.getQuizArchiveById(id)
    }
}