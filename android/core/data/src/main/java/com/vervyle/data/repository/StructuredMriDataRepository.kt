package com.vervyle.data.repository

import com.vervyle.model.StructuredMriData
import kotlinx.coroutines.flow.Flow

interface StructuredMriDataRepository {

    fun getStructuredMriData(id: String): Flow<StructuredMriData>
}