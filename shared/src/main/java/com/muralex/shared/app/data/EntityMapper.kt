package com.muralex.shared.app.data

interface EntityMapper<SRC, DST> {
    fun mapFromEntity(data: SRC): DST
}