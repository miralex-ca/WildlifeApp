package com.muralex.shared.data.repository.navstructure.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.muralex.shared.data.model.structure.SectionJsonData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class SectionsJsonUtil(private val context: Context) {

    suspend fun getSections(): List<SectionJsonData> {
        return readSectionJson()
    }

    private suspend fun readSectionJson(): List<SectionJsonData> = withContext(Dispatchers.IO) {
        try {
            context.assets.open(SECTIONS_FILE).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val dataType = object : TypeToken<List<SectionJsonData>>() {}.type
                    return@withContext Gson().fromJson(jsonReader, dataType)
                }
            }
        } catch (ex: Exception) {
            Timber.e("Error reading structure json: ${ex.message}")
            return@withContext emptyList()
        }
    }

    companion object {
        const val SECTIONS_FILE = "json/sections.json"
    }
}