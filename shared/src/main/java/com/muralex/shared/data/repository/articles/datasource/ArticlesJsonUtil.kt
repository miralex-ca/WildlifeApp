package com.muralex.shared.data.repository.articles.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.muralex.shared.data.model.json.ArticleJsonData
import com.muralex.shared.data.model.json.DetailJsonData
import com.muralex.shared.data.model.json.SectionArticlesJsonData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticlesJsonUtil(private val context: Context) {

    suspend fun getArticles(): List<ArticleJsonData> {
        return readSectionJson()
    }

    suspend fun getDetails(): List<DetailJsonData> {
        return readDetailJson()
    }

    suspend fun getCollections(): List<SectionArticlesJsonData> {
        return readCollectionsJson()
    }

    private suspend fun readSectionJson(): List<ArticleJsonData> = withContext(Dispatchers.IO) {
        try {
            context.assets.open(ARTICLES_FILE).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val dataType = object : TypeToken<List<ArticleJsonData>>() {}.type
                    return@withContext Gson().fromJson(jsonReader, dataType)
                }
            }
        } catch (ex: Exception) {
            return@withContext emptyList()
        }
    }

    private suspend fun readCollectionsJson(): List<SectionArticlesJsonData> =
        withContext(Dispatchers.IO) {
            try {
                context.assets.open(COLLECTIONS_FILE).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val dataType = object : TypeToken<List<SectionArticlesJsonData>>() {}.type
                        return@withContext Gson().fromJson(jsonReader, dataType)
                    }
                }
            } catch (ex: Exception) {
                return@withContext emptyList()
            }
        }

    private suspend fun readDetailJson(): List<DetailJsonData> = withContext(Dispatchers.IO) {
        try {
            context.assets.open(DETAILS_FILE).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val dataType = object : TypeToken<List<DetailJsonData>>() {}.type
                    return@withContext Gson().fromJson(jsonReader, dataType)
                }
            }
        } catch (ex: Exception) {
            return@withContext emptyList()
        }
    }

    companion object {
        const val ARTICLES_FILE = "json/articles.json"
        const val COLLECTIONS_FILE = "json/collections.json"
        const val DETAILS_FILE = "json/details.json"
    }
}