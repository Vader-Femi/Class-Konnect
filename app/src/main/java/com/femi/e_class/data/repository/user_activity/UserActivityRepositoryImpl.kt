package com.femi.e_class.data.repository.user_activity

import com.femi.e_class.data.UserPreferences
import com.femi.e_class.data.repository.base.BaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class UserActivityRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val collectionReference: CollectionReference,
    dataStore: UserPreferences
): UserActivityRepository, BaseRepositoryImpl(dataStore) {

    override fun getAuthReference() = firebaseAuth
    override fun getCollectionReference() = collectionReference
}