package com.muralex.shared.data.mappers.json_db

import com.muralex.shared.app.data.EntityMapper
import com.muralex.shared.data.model.db.ArticleSectionDataDB
import com.muralex.shared.data.model.json.SectionArticlesJsonData
import javax.inject.Inject

class SectionsArticlesJsonToDBMapper @Inject constructor() :
    EntityMapper<SectionArticlesJsonData, List<ArticleSectionDataDB>> {

    override fun mapFromEntity(data: SectionArticlesJsonData): List<ArticleSectionDataDB> {
        return data.articles?.map {
            ArticleSectionDataDB(
                sectionId = data.sectionId ?: "",
                articleId = it[0]
            )
        } ?: emptyList()
    }

    fun mapFromEntityList(entitiesList: List<SectionArticlesJsonData>): List<ArticleSectionDataDB> {
        return entitiesList.flatMap { mapFromEntity(it) }
    }

}
