package com.femi.e_class.repositories

import com.femi.e_class.data.UserPreferences

class MainActivityRepository(
    dataStore: UserPreferences
) : BaseRepository(dataStore) {
}