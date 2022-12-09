package com.muralex.shared.app.utils

import kotlinx.coroutines.delay

interface DataDelayProvider {
    suspend fun delayLoading()

    class Base : DataDelayProvider {
        override suspend fun delayLoading() {
            delay(CATEGORY_LOADING_TIME)
        }
    }

    companion object {
        const val CATEGORY_LOADING_TIME = 160L
    }

}