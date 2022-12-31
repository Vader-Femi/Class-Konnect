package com.femi.e_class.data.repository

import com.femi.e_class.data.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class ResetPasswordRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    dataStore: UserPreferences,
) : ResetPasswordRepository, BaseRepositoryImpl(dataStore) {

    override fun getAuthReference() = firebaseAuth
}