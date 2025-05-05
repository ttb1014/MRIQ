package com.vervyle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vervyle.database.dao.AggregatesDao
import com.vervyle.database.dao.AnnotationDao
import com.vervyle.database.dao.DatasetDao
import com.vervyle.database.dao.LinksDao
import com.vervyle.database.dao.MedicalImageDao
import com.vervyle.database.dao.StructureDao
import com.vervyle.database.dao.StructureGuessDao
import com.vervyle.database.model.AnnotationImageEntity
import com.vervyle.database.model.DatasetEntity
import com.vervyle.database.model.MedicalImageEntity
import com.vervyle.database.model.StructureEntity
import com.vervyle.database.model.StructureGuessEntity
import com.vervyle.database.model.links.AnnotationStructureLink
import com.vervyle.database.model.links.DatasetImageLink
import com.vervyle.database.model.links.ImageAnnotationLink
import com.vervyle.database.util.Converters

@Database(
    entities = [
        MedicalImageEntity::class,
        AnnotationImageEntity::class,
        StructureEntity::class,
        DatasetEntity::class,
        StructureGuessEntity::class,
        AnnotationStructureLink::class,
        DatasetImageLink::class,
        ImageAnnotationLink::class,
    ],
    version = 2,
)
@TypeConverters(Converters::class)
internal abstract class MriqDatabase : RoomDatabase() {

    abstract fun annotationDao(): AnnotationDao

    abstract fun datasetDao(): DatasetDao

    abstract fun medicalImageDao(): MedicalImageDao

    abstract fun structureDao(): StructureDao

    abstract fun structureGuessDao(): StructureGuessDao

    abstract fun linksDao(): LinksDao

    abstract fun aggregatesDao(): AggregatesDao
}