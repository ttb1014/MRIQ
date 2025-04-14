package com.vervyle.database.util

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vervyle.database.model.MedicalImageType
import com.vervyle.model.Plane
import kotlinx.datetime.Instant
import java.lang.reflect.Type


internal class Converters {

    @TypeConverter
    fun fromListToString(list: List<Int>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToList(value: String): List<Int> {
        val listType: Type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromUriToString(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun fromStringToUri(value: String): Uri {
        return Uri.parse(value)
    }

    @TypeConverter
    fun fromStringToEnum(value: String): Plane {
        return Plane.valueOf(value)
    }

    @TypeConverter
    fun fromEnumToString(plane: Plane): String = plane.toString()

    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? =
        instant?.toEpochMilliseconds()

    @TypeConverter
    fun fromMedicalImageType(medicalImageType: MedicalImageType): String {
        return medicalImageType.name
    }

    @TypeConverter
    fun toMedicalImageType(medicalImageType: String): MedicalImageType {
        return MedicalImageType.valueOf(medicalImageType)
    }

    @TypeConverter
    fun fromPlane(plane: com.vervyle.database.model.Plane): String {
        return plane.name
    }

    @TypeConverter
    fun toPlane(plane: String): com.vervyle.database.model.Plane {
        return com.vervyle.database.model.Plane.valueOf(plane)
    }
}