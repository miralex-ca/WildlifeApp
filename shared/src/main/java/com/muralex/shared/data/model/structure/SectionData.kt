package com.muralex.shared.data.model.structure

data class SectionData(
    val id: String,
    val title: String,
    val desc: String,
    val image: String,
    val parent: String,
    val type: String,
)

data class SectionJsonData(
    val id: String?,
    val title: String?,
    val desc: String?,
    val image: String?,
    val parent: String?,
    val type: String?,
)