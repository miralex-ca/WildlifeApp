package com.muralex.shared.data.model.structure

import com.muralex.shared.data.model.articles.ArticleData
import com.muralex.shared.data.model.structure.SectionData

data class SectionDataWithArticles(
    val section: SectionData,
    val articles: List<ArticleData>,
)
