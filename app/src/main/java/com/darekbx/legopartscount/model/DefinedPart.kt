package com.darekbx.legopartscount.model

import com.darekbx.legopartscount.repository.database.DefinedPartEntity

class DefinedPart(val number: Int) {

    fun toEntity() = DefinedPartEntity(this.number)

    companion object {

        fun fromEntity(definedPartEntity: DefinedPartEntity) =
            DefinedPart(definedPartEntity.partNumber)
    }
}
