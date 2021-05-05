package com.darekbx.legopartscount.viewmodel

import androidx.lifecycle.MutableLiveData
import com.darekbx.legopartscount.model.DefinedPart
import com.darekbx.legopartscount.repository.database.DefinedPartDao

class DefinedPartsViewModel(
    private val definedPartDao: DefinedPartDao
) : BaseViewModel() {

    fun addDefinedParts(parts: List<DefinedPart>) {
        launchDataLoad {
            insertDefinedParts(parts)
        }
    }

    fun listDefinedParts(): MutableLiveData<List<DefinedPart>> {
        val result = MutableLiveData<List<DefinedPart>>()
        launchDataLoad {
            val definedParts = definedPartDao.selectAll().map { DefinedPart.fromEntity(it) }
            if (definedParts.isNotEmpty()) {
                result.postValue(definedParts)
            }
        }
        return result
    }

    fun deletePart(definedPart: DefinedPart) {
        launchDataLoad {
            definedPartDao.delete(definedPart.number)
        }
    }

    private suspend fun insertDefinedParts(parts: List<DefinedPart>) {
        /**
         * Map only integer part numbers, to exclude "special" parts like:
         * part_num: 14769pr1029
         * name: Tile Round 2 x 2 with Bottom Stud Holder with Time Gears Print
         */
        val filteredParts = parts
            .map { it.toEntity() }
        definedPartDao.insert(filteredParts)
    }
}
