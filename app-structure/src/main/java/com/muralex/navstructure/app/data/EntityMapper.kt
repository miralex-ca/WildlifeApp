package com.muralex.navstructure.app.data

interface EntityMapper<SRC, DST> {
    fun mapFromEntity(data: SRC): DST
}