package com.muralex.shared.domain.data.article

import com.muralex.shared.domain.data.navstructure.Section

data class SectionWithArticles (
    val section: Section,
    val articles: List<Article>,
)

