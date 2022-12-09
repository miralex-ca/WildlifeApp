package com.muralex.shared.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_table")
data class ArticleDataDB(
    @PrimaryKey
    @ColumnInfo(name = "article_id")
    val id: String,
    @ColumnInfo(name = "article_title")
    val title: String,
    @ColumnInfo(name = "article_desc")
    val desc: String,
    @ColumnInfo(name = "article_text")
    val text: String,
    @ColumnInfo(name = "article_image")
    val image: String,
)
