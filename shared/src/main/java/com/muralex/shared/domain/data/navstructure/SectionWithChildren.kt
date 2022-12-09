package com.muralex.shared.domain.data.navstructure

data class SectionWithChildren (
    val section: Section,
    val subSections: List<Section>,
)

