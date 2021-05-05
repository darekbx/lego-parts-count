package com.darekbx.legopartscount.model

import com.darekbx.legopartscount.repository.database.DefinedPartEntity

class DefinedPart(
    val number: Int,
    val imageUrl: String,
    val name: String
) {

    fun toEntity() = DefinedPartEntity(this.number, this.imageUrl, this.name)

    companion object {

        fun fromEntity(definedPartEntity: DefinedPartEntity) =
            DefinedPart(definedPartEntity.partNumber, definedPartEntity.imageUrl, definedPartEntity.name)
    }
}
