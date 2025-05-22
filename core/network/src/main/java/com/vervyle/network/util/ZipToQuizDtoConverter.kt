package com.vervyle.network.util

import com.vervyle.network.exception.NotParsedException
import com.vervyle.network.model.AnnotatedImageDto
import com.vervyle.network.model.AnnotationDto
import com.vervyle.network.model.QuizDto
import com.vervyle.network.model.StructureDto
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Type
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

/**
 * Класс реализующий преобразование zip-архива в QuizDto.
 */
class ZipToQuizDtoConverter(
    private val outputDir: File
) : Converter<ResponseBody, QuizDto> {

    /**
     * @param responseBody zip-архив отправленный сервером.
     * @throws SecurityException
     * @throws NotParsedException
     */
    override fun convert(responseBody: ResponseBody): QuizDto {
        outputDir.deleteRecursively()
        outputDir.mkdirs()

        val zipFiles = responseBody.getAndSaveFiles()

        val quizDto = zipFiles.toQuizDto()
        return quizDto
    }

    private fun ResponseBody.getAndSaveFiles(): List<File> {
        val zipFiles = mutableListOf<File>()
        val buffer = ByteArray(1024)

        ZipInputStream(this.byteStream()).use { zipInputStream ->
            var zipEntry: ZipEntry? = zipInputStream.nextEntry

            while (zipEntry != null) {
                val file = File(outputDir, zipEntry.name)
                val fos = FileOutputStream(file)

                var len: Int
                while (zipInputStream.read(buffer).also { len = it } > 0) {
                    fos.write(buffer, 0, len)
                }
                fos.close()
                zipFiles.add(file)
                zipEntry = zipInputStream.nextEntry
            }
            zipInputStream.closeEntry()
        }

        return zipFiles
    }

    /**
     * @throws NotParsedException
     */
    private fun List<File>.toQuizDto(): QuizDto {
        try {
            val metadata = this.first { it.name.contains("metadata") }
            return metadata.parseAsJson()
        } catch (e: NoSuchElementException) {
            throw NotParsedException(e.message)
        }
    }

    /**
     * TODO: сделать обработку асинхронной
     * @throws IllegalArgumentException
     * @throws NullPointerException
     */
    private fun File.parseAsJson(): QuizDto {
        val text = readText()
        val quiz = Json.parseToJsonElement(text).jsonObject
        return QuizDto(
            id = quiz["id"]!!.jsonPrimitive.content,
            structures = quiz["structures"]!!.jsonArray.map { jsonElement ->
                val structureObject = jsonElement.jsonObject
                StructureDto(
                    id = structureObject["id"]!!.jsonPrimitive.content.toInt(),
                    name = structureObject["name"]!!.jsonPrimitive.content,
                    description = structureObject["description"]!!.jsonPrimitive.content
                )
            },
            axialAnnotatedImages = quiz["axial"]!!.jsonArray.map { jsonElement ->
                val imageObject = jsonElement.jsonObject
                AnnotatedImageDto(
                    index = imageObject["index"]!!.jsonPrimitive.content.toInt(),
                    pathToImageFile = imageObject["path_to_image_file"]!!.jsonPrimitive.content,
                    annotations = imageObject["annotations"]!!.jsonArray.map {
                        val annotationObject = it.jsonObject
                        AnnotationDto(
                            structureId = annotationObject["structure_id"]!!.jsonPrimitive.content.toInt(),
                            pathToImageFile = annotationObject["path_to_image_file"]!!.jsonPrimitive.content
                        )
                    }
                )
            },
            coronalAnnotatedImages = quiz["coronal"]!!.jsonArray.map { jsonElement ->
                val imageObject = jsonElement.jsonObject
                AnnotatedImageDto(
                    index = imageObject["index"]!!.jsonPrimitive.content.toInt(),
                    pathToImageFile = imageObject["path_to_image_file"]!!.jsonPrimitive.content,
                    annotations = imageObject["annotations"]!!.jsonArray.map {
                        val annotationObject = it.jsonObject
                        AnnotationDto(
                            structureId = annotationObject["structure_id"]!!.jsonPrimitive.content.toInt(),
                            pathToImageFile = annotationObject["path_to_image_file"]!!.jsonPrimitive.content
                        )
                    }
                )
            },
            sagittalAnnotatedImages = quiz["sagittal"]!!.jsonArray.map { jsonElement ->
                val imageObject = jsonElement.jsonObject
                AnnotatedImageDto(
                    index = imageObject["index"]!!.jsonPrimitive.content.toInt(),
                    pathToImageFile = imageObject["path_to_image_file"]!!.jsonPrimitive.content,
                    annotations = imageObject["annotations"]!!.jsonArray.map {
                        val annotationObject = it.jsonObject
                        AnnotationDto(
                            structureId = annotationObject["structure_id"]!!.jsonPrimitive.content.toInt(),
                            pathToImageFile = annotationObject["path_to_image_file"]!!.jsonPrimitive.content
                        )
                    }
                )
            },
            name = quiz["name"]!!.jsonPrimitive.content
        )
    }

    class Factory(private val outputDir: File) : Converter.Factory() {
        @OptIn(ExperimentalStdlibApi::class)
        /**
         * Используется для автоматического преобразования из responseBody в QuizDto.
         * Предоставляет данный класс конвертера, если желаемый тип совпадает.
         */
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit,
        ): Converter<ResponseBody, *>? {
            return if (type == typeOf<QuizDto>().javaType) {
                ZipToQuizDtoConverter(outputDir)
            } else {
                null
            }
        }
    }
}