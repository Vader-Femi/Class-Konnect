package com.femi.e_class.repositories

import com.femi.e_class.data.UserPreferences
import kotlinx.coroutines.flow.first

open class BaseRepository(
    private val dataStore: UserPreferences,
) {

    suspend fun userEmail(email: String) = dataStore.userEmail(email)

    suspend fun userEmail(): String = dataStore.userEmail.first()

    suspend fun userMatric(matric: Long) = dataStore.userMatric(matric)

    suspend fun userMatric(): Long = dataStore.userMatric.first()

    suspend fun userFName(fName: String) = dataStore.userFName(fName)

    suspend fun userFName(): String = dataStore.userFName.first()

    suspend fun userLName(lName: String) = dataStore.userLName(lName)

    suspend fun userLName(): String = dataStore.userLName.first()

    suspend fun videoResolution(resolution: Int) = dataStore.videoResolution(resolution)

    suspend fun videoResolution(): Int = dataStore.videoResolution.first()
}