package com.femi.e_class.data.repository.base

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference

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

    fun getAuthReference(): FirebaseAuth

    fun getCollectionReference(): CollectionReference
}