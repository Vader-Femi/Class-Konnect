package com.femi.e_class.data.repository

import com.femi.e_class.data.UserPreferences
import javax.inject.Inject

class MainActivityRepositoryImpl @Inject constructor(
    dataStore: UserPreferences,
): MainActivityRepository, BaseRepositoryImpl(dataStore) {

}