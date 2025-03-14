package com.vervyle.database.util

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
}