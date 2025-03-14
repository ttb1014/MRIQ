package com.vervyle.data.model

import com.vervyle.model.Plane

data class FileMetadata(
    val plane: Plane,
    val base: String,
    val id: String
)