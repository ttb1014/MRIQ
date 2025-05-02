package com.vervyle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "structure_guesses",
    indices = [
        Index("structure_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = StructureEntity::class,
            parentColumns = ["id"],
            childColumns = ["structure_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)
data class StructureGuessEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "structure_id")
    val structureId: Int,

    @ColumnInfo(name = "is_right")
    val isRight: Boolean,

    @ColumnInfo(name = "time_stamp")
    val timeStamp: Instant,
)
