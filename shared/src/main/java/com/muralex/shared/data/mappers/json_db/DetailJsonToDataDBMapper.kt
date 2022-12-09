package com.muralex.shared.data.mappers.json_db

import com.muralex.shared.app.data.EntityMapper
import com.muralex.shared.data.model.db.DetailDataDB
import com.muralex.shared.data.model.json.DetailJsonData
import javax.inject.Inject

class DetailJsonToDataDBMapper @Inject constructor() : EntityMapper<DetailJsonData, DetailDataDB> {

    override fun mapFromEntity(data: DetailJsonData): DetailDataDB {
        return DetailDataDB(
            id = data.id ?: "",
            title = data.title ?: "",
            desc = data.desc ?: "",
            text = data.text ?: "",
            image = data.image ?: ""
        )
    }

    fun mapFromEntityList(entitiesList: List<DetailJsonData>): List<DetailDataDB> {
        return entitiesList.map { mapFromEntity(it) }
    }

}
