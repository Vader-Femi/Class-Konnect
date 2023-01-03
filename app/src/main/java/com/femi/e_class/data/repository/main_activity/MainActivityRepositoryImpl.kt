package com.femi.e_class.data.repository.main_activity

import com.femi.e_class.data.UserPreferences
import com.femi.e_class.data.repository.base.BaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class MainActivityRepositoryImpl @Inject constructor(
    firebaseAuth: FirebaseAuth,
    collectionReference: CollectionReference,
    dataStore: UserPreferences,
): MainActivityRepository, BaseRepositoryImpl(firebaseAuth, collectionReference, dataStore)