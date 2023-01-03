package com.femi.e_class.data.repository.user_activity

import com.femi.e_class.data.UserPreferences
import com.femi.e_class.data.repository.base.BaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class UserActivityRepositoryImpl @Inject constructor(
    firebaseAuth: FirebaseAuth,
    collectionReference: CollectionReference,
    dataStore: UserPreferences
): UserActivityRepository, BaseRepositoryImpl(firebaseAuth, collectionReference, dataStore)