package com.femi.e_class.repositories

import com.femi.e_class.data.UserPreferences
import com.google.firebase.firestore.CollectionReference

class VideoActivityRepository(
    private val collectionReference: CollectionReference,
    dataStore: UserPreferences
) : BaseRepository(dataStore) {
    fun getCollectionReference() = collectionReference
}