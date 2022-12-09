package com.muralex.shared.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detail_table")
data class DetailDataDB(
    @PrimaryKey
    @ColumnInfo(name = "detail_id")
    val id: String,
    @ColumnInfo(name = "detail_title")
    val title: String,
    @ColumnInfo(name = "detail_desc")
    val desc: String,
    @ColumnInfo(name = "detail_text")
    val text: String,
    @ColumnInfo(name = "detail_image")
    val image: String,
)
