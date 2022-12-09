package com.muralex.shared.app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.muralex.shared.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingsManager @Inject constructor (
    @ApplicationContext val context: Context
    ) {

    private var settings: SharedPreferences? = null

    init {
        settings = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun isDetailNavigationEnabled() : Boolean {
        val default = true
        return settings?.getBoolean( context.getString(R.string.setting_detail_navigation), default) ?: default
    }

    fun setDetailNavigation(enabled: Boolean)  {
        val sharedPref = settings ?: return
        with (sharedPref.edit()) {
            putBoolean( context.getString(R.string.setting_detail_navigation), enabled)
            apply()
        }
    }

    fun isFirstLaunch() : Boolean {
        val default = true
        return settings?.getBoolean( context.getString(R.string.setting_first_launch), default) ?: default
    }

    fun setFirstLaunch(first: Boolean)  {
        val sharedPref = settings ?: return
        with (sharedPref.edit()) {
            putBoolean( context.getString(R.string.setting_first_launch), first)
            apply()
        }
    }






}
