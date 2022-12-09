package com.muralex.shared.domain.data.navstructure

data class Section(
    val id: String,
    val title: String,
    val desc: String,
    val image: String,
    val parent: String,
    val type: String,
)
