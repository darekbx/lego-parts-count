package com.darekbx.legopartscount.viewmodel

import com.darekbx.legopartscount.repository.database.DefinedPartDao
import com.darekbx.legopartscount.repository.database.DefinedPartEntity

class DefinedPartsViewModel(
    private val definedPartDao: DefinedPartDao
): BaseViewModel() {

    fun addDefinedParts(parts: List<String>) {
        launchDataLoad {
            /**
             * Map only integer part numbers, to exclude "special" parts like:
             * part_num: 14769pr1029
             * name: Tile Round 2 x 2 with Bottom Stud Holder with Time Gears Print
             */
            val filteredParts = parts
                .mapNotNull { it.toIntOrNull() }
                .map { DefinedPartEntity(it) }
            definedPartDao.insert(filteredParts)
        }
    }
}
