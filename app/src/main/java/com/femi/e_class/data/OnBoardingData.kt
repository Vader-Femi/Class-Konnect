package com.femi.e_class.data

import com.femi.e_class.R

object OnBoardingData {
    fun getItems(): List<OnBoardingItem> {
        return listOf(
            OnBoardingItem(R.drawable.telecommuting_bro,
                "Join a Class",
                "Join a class from the comfort of your home"),
            OnBoardingItem(R.drawable.telecommuting_rafiki,
                "HD Audio-Video",
                "Enjoy high quality audio paired with high definition video"),
            OnBoardingItem(R.drawable.typing_rafiki,
                "Share Chat and screen",
                "Share your screen and messages while in a class")
        )
    }

    data class OnBoardingItem(
        val image: Int,
        val title: String,
        val description: String
    )
}