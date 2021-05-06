package com.darekbx.legopartscount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.darekbx.legopartscount.repository.database.DefinedPartDao
import com.darekbx.legopartscount.repository.rebrickable.Rebrickable
import com.darekbx.legopartscount.repository.rebrickable.model.LegoPart
import com.darekbx.legopartscount.repository.rebrickable.model.LegoSet
import com.darekbx.legopartscount.repository.rebrickable.model.LegoSetPart

class RebrickableViewModel(
    private val rebrickable: Rebrickable,
    private val definedPartDao: DefinedPartDao
) : BaseViewModel() {

    val setSearchResult = MutableLiveData<LegoSet>()
    val setPartsSearchResult = MutableLiveData<List<LegoSetPart>>()
    val partSearchResult = MutableLiveData<List<LegoPart>>()

    fun searchForSet(query: String) {
        launchDataLoad {
            val legoSets = rebrickable.searchForSets(query)
            setSearchResult.postValue(legoSets.results.first())
        }
    }

    fun fetchSet(setNumber: String): LiveData<LegoSet> {
        val setResult = MutableLiveData<LegoSet>()
        launchDataLoad {
            val legoSet = rebrickable.fetchSet(setNumber)
            setResult.postValue(legoSet)
        }
        return setResult
    }

    fun fetchSetParts(setNumber: String) {
        launchDataLoad {
            val definedParts = definedPartDao.selectAll()
            val parts = rebrickable.fetchSetParts(setNumber)
                .results
                .filter { legoSetPart ->
                    definedParts.any {
                        "${it.partNumber}" == legoSetPart.part.partNumber
                    }
                }
            setPartsSearchResult.postValue(parts)
        }
    }

    fun searchForPart(query: String) {
        launchDataLoad {
            val result = rebrickable.searchParts(query).results

            /**
             * Map only integer part numbers, to exclude "special" parts like:
             * part_num: 14769pr1029
             * name: Tile Round 2 x 2 with Bottom Stud Holder with Time Gears Print
             */
            val filteredResults = result
                .filter { it.partNumber.toIntOrNull() != null && it.partImageUrl != null }
            partSearchResult.postValue(filteredResults)
        }
    }
}
