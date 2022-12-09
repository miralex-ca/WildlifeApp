package com.muralex.shared.app.utils

import android.webkit.URLUtil
import com.muralex.shared.domain.data.navstructure.Section


fun Section.hasType(type: Constants.SectionType): Boolean {
    return this.type.lowercase() == type.name.lowercase()
}

fun String.checkArticleImageUrl(): String {
    return when {
        URLUtil.isValidUrl(this) -> this
        this.isBlank() -> this
        else -> Constants.ICONS_FOLDER + this
    }
}






