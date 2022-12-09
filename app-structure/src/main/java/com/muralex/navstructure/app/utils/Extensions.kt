package com.muralex.navstructure.app.utils

import android.view.View
import android.webkit.URLUtil
import com.muralex.shared.domain.data.navstructure.Section
import com.muralex.shared.app.utils.Constants

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.displayIf(visible: Boolean) {
    visibility = if (visible) View.VISIBLE
    else View.GONE
}

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






