package com.muralex.shared.data.database

import androidx.room.*
import com.muralex.shared.data.model.db.ArticleDataDB
import com.muralex.shared.data.model.db.ArticleSectionDataDB
import com.muralex.shared.data.model.db.DetailDataDB
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticlesList(list: List<ArticleDataDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSectionArticles(data: List<ArticleSectionDataDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(data: List<DetailDataDB>)

    @Transaction
    @Query ("DELETE FROM article_table")
    suspend fun deleteAllArticles()

    @Transaction
    @Query ("DELETE FROM article_section_table")
    suspend fun deleteAllArticlesSections()

    @Transaction
    @Query ("DELETE FROM detail_table")
    suspend fun deleteAllDetails()

    @Query("SELECT * from article_table")
    suspend fun getAllArticles(): List<ArticleDataDB>

    @Query("SELECT * from article_table WHERE article_id = :articleId")
    suspend fun getArticleById(articleId: String): ArticleDataDB

    @Query("SELECT * from detail_table WHERE detail_id = :articleId")
    suspend fun getDetailById(articleId: String): DetailDataDB?

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * from article_table a left join article_section_table b ON a.article_id = b.article_id " +
            "WHERE b.section_id IS NOT NULL AND b.section_id = :sectionId")
     fun getArticlesBySectionId(sectionId: String): Flow<List<ArticleDataDB>>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * from article_table a left join article_section_table b ON a.article_id = b.article_id " +
            "WHERE b.section_id IS NOT NULL AND b.section_id = :sectionId")
    fun getArticlesListBySectionId(sectionId: String): List<ArticleDataDB>



}
