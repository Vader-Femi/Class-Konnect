package com.femi.e_class.data.repository.main_activity

import com.femi.e_class.data.UserPreferences
import com.femi.e_class.data.repository.base.BaseRepositoryImpl
import javax.inject.Inject

class MainActivityRepositoryImpl @Inject constructor(
    dataStore: UserPreferences,
): MainActivityRepository, BaseRepositoryImpl(dataStore) {

}