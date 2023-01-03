package com.femi.e_class.data.repository.base

import com.femi.e_class.data.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.first
import javax.inject.Inject

open class BaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val collectionReference: CollectionReference,
    private val dataStore: UserPreferences
): BaseRepository {

    override suspend fun userEmail(email: String) = dataStore.userEmail(email)

    override suspend fun userEmail(): String = dataStore.userEmail.first()

    override suspend fun userMatric(matric: Long) = dataStore.userMatric(matric)

    override suspend fun userMatric(): Long = dataStore.userMatric.first()

    override suspend fun userFName(fName: String) = dataStore.userFName(fName)

    override suspend fun userFName(): String = dataStore.userFName.first()

    override suspend fun userLName(lName: String) = dataStore.userLName(lName)

    override suspend fun userLName(): String = dataStore.userLName.first()

    override suspend fun videoResolution(resolution: Int) = dataStore.videoResolution(resolution)

    override suspend fun videoResolution(): Int = dataStore.videoResolution.first()

    override fun getAuthReference() = firebaseAuth

    override fun getCollectionReference() = collectionReference
}