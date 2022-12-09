package com.muralex.shared.data.repository.navstructure.datasource

import com.muralex.shared.data.model.structure.SectionData

interface NavDataSource {
    suspend fun getSectionsList(parentId: String): List<SectionData>
    suspend fun getSectionById(sectionId: String): SectionData
}