package com.femi.e_class.data.repository.base

interface BaseRepository{

    suspend fun userEmail(email: String)

    suspend fun userEmail(): String

    suspend fun userMatric(matric: Long)

    suspend fun userMatric(): Long

    suspend fun userFName(fName: String)

    suspend fun userFName(): String

    suspend fun userLName(lName: String)

    suspend fun userLName(): String

    suspend fun videoResolution(resolution: Int)

    suspend fun videoResolution(): Int
}