package com.femi.e_class.repositories

import com.google.firebase.firestore.CollectionReference

class HomeActivityRepository(
    private val collectionReference: CollectionReference,
) : BaseRepository() {
    fun getCollectionReference() = collectionReference
}