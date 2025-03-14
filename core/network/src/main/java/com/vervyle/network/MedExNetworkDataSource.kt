package com.vervyle.network

import java.io.File

// TODO: Add required functionality
interface MedExNetworkDataSource {

    suspend fun getMriData(id: Int? = null)

    suspend fun getZipTest(id: String? = null) : List<File>
}