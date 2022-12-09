package com.muralex.shared.data.mappers.json_data

import android.webkit.URLUtil
import com.muralex.shared.app.data.EntityMapper
import com.muralex.shared.app.utils.Constants.ICONS_FOLDER
import com.muralex.shared.data.model.articles.DetailData
import com.muralex.shared.data.model.json.DetailJsonData
import javax.inject.Inject

class DetailJsonToDataMapper @Inject constructor() : EntityMapper<DetailJsonData, DetailData> {

    override fun mapFromEntity(data: DetailJsonData): DetailData {
        return DetailData(
            id = data.id ?: "",
            title = data.title ?: "",
            desc = data.desc ?: "",
            text = data.text ?: "",
            image = data.image?.let {
                if (URLUtil.isValidUrl(data.image)) data.image
                else ICONS_FOLDER + data.image
            } ?: ""
        )
    }

    fun mapFromEntityList(entitiesList: List<DetailJsonData>): List<DetailData> {
        return entitiesList.map { mapFromEntity(it) }
    }

}
