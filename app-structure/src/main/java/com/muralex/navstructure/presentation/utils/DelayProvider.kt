package com.muralex.navstructure.presentation.utils

import kotlinx.coroutines.delay

interface DelayProvider {
    suspend fun delayTransition()
    suspend fun delayLoading()

    class Base : DelayProvider {
        override suspend fun delayTransition() {
            delay(DETAIL_TRANSITION_TIME)
        }

        override suspend fun delayLoading() {
            delay(CATEGORY_LOADING_TIME)
        }
    }

    companion object {
        const val DETAIL_TRANSITION_TIME = 120L
        const val CATEGORY_LOADING_TIME = 160L
    }

}