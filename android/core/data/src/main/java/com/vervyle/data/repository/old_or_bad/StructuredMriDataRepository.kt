package com.vervyle.data.repository.old_or_bad

import com.vervyle.model.old.StructuredMriData
import kotlinx.coroutines.flow.Flow

@Deprecated("ну че за хуетень блять")
interface StructuredMriDataRepository {

    fun getStructuredMriData(id: String): Flow<StructuredMriData>
}