package com.muralex.shared.domain.usecases.structure

import com.muralex.shared.app.utils.Constants.SECTION_ROOT_ID
import com.muralex.shared.app.utils.Dispatchers
import com.muralex.shared.domain.data.navstructure.Section
import com.muralex.shared.domain.mappers.SectionDataToSectionMapper
import com.muralex.shared.domain.repositories.NavRepository
import javax.inject.Inject

class GetSectionsListUseCase @Inject constructor(
    private val repository: NavRepository,
    private val mapper: SectionDataToSectionMapper,
    private val dispatcher: Dispatchers,
) {
    suspend operator fun invoke(): List<Section> = dispatcher.background {
         mapper.mapFromEntityList(repository.getSectionsList(SECTION_ROOT_ID))
    }
}