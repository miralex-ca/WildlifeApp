package com.muralex.shared.data.model.structure

data class SectionDataWithChildren (
    val section: SectionData,
    val subSections: List<SectionData>,
)

