package com.muralex.shared.data.database

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.muralex.shared.data.mappers.json_db.ArticleJsonToDataDBMapper
import com.muralex.shared.data.mappers.json_db.DetailJsonToDataDBMapper
import com.muralex.shared.data.mappers.json_db.SectionsArticlesJsonToDBMapper
import com.muralex.shared.data.model.json.ArticleJsonData
import com.muralex.shared.data.model.json.DetailJsonData
import com.muralex.shared.data.model.json.SectionArticlesJsonData
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltWorker
class PrepopulateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dao: ArticlesDao,
    private val articlesMapper: ArticleJsonToDataDBMapper,
    private val sectionArticlesMapper: SectionsArticlesJsonToDBMapper,
    private val detailMapper: DetailJsonToDataDBMapper,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        Timber.d("DB prepopulate")
        try {

            insertArticlesFromJson()
            insertSectionArticlesFromJson()
            insertDetailsFromJson()

            Result.success()

        } catch (ex: Exception) {
            Result.failure()
        }
    }

    private suspend fun insertDetailsFromJson() {
        applicationContext.assets.open(DETAILS_FILE).use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                val dataType = object : TypeToken<List<DetailJsonData>>() {}.type
                val detailsList: List<DetailJsonData> =
                    Gson().fromJson(jsonReader, dataType)

                dao.deleteAllDetails()
                dao.insertDetails(
                    detailMapper.mapFromEntityList(detailsList)
                )
            }
        }
    }

    private suspend fun insertSectionArticlesFromJson() {
        applicationContext.assets.open(COLLECTIONS_FILE).use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                val dataType = object : TypeToken<List<SectionArticlesJsonData>>() {}.type
                val sectionArticles: List<SectionArticlesJsonData> =
                    Gson().fromJson(jsonReader, dataType)

                dao.deleteAllArticlesSections()
                dao.insertSectionArticles(
                    sectionArticlesMapper.mapFromEntityList(sectionArticles)
                )
            }
        }
    }

    private suspend fun insertArticlesFromJson() {
        applicationContext.assets.open(ARTICLES_FILE).use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                val dataType = object : TypeToken<List<ArticleJsonData>>() {}.type
                val articles: List<ArticleJsonData> = Gson().fromJson(jsonReader, dataType)
                dao.deleteAllArticles()
                dao.insertArticlesList(articlesMapper.mapFromEntityList(articles))
            }
        }
    }

    companion object {
        const val ARTICLES_FILE = "json/articles.json"
        const val COLLECTIONS_FILE = "json/collections.json"
        const val DETAILS_FILE = "json/details.json"
    }

}
