package com.muralex.shared.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_section_table")
data class ArticleSectionDataDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "article_id")
    val articleId: String,
    @ColumnInfo(name = "section_id")
    val sectionId: String,
)