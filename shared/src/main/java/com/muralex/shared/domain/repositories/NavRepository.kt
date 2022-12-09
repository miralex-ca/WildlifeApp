package com.muralex.shared.domain.repositories

import com.muralex.shared.data.model.structure.SectionData
import com.muralex.shared.data.model.structure.SectionDataWithChildren

interface NavRepository {
    suspend fun getSectionsList(parentID: String): List<SectionData>
    suspend fun getSectionWithChildrenData(parentID: String): SectionDataWithChildren
}