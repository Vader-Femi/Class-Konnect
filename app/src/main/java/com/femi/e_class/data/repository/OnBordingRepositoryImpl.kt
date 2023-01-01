package com.femi.e_class.data.repository

import com.femi.e_class.data.UserPreferences
import javax.inject.Inject

class OnBordingRepositoryImpl @Inject constructor(
    dataStore: UserPreferences,
): OnBordingRepository, BaseRepositoryImpl(dataStore) {

}