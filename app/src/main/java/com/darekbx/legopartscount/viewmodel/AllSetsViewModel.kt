package com.darekbx.legopartscount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.darekbx.legopartscount.BuildConfig
import com.darekbx.legopartscount.repository.brickset.Brickset
import com.darekbx.legopartscount.repository.brickset.model.*
import com.darekbx.legopartscount.repository.database.DefinedPartDao
import com.darekbx.legopartscount.repository.rebrickable.Rebrickable
import com.darekbx.legopartscount.repository.rebrickable.model.LegoSet

class LegoSetDefinedParts(
    val legoSet: LegoSet,
    val definedPartsCount: Int
)

class AllSetsProgress(
    val progress: Int,
    val max: Int
)

class AllSetsViewModel(
    private val brickset: Brickset,
    private val rebrickable: Rebrickable,
    private val definedPartDao: DefinedPartDao
) : BaseViewModel() {

    companion object {
        private const val PAGE_SIZE = 1000
        private const val THEME = "Technic"
    }

    val progress = MutableLiveData<AllSetsProgress>()

    fun loadAllSets(fromYear: Int, minPartsCount: Int): LiveData<LegoSetDefinedParts> {
        val result = MutableLiveData<LegoSetDefinedParts>()
        launchDataLoad {

            val definedParts = definedPartDao.selectAll()
            val loginResult = loginUser()
            val allSets = retrieveSets(loginResult.hash)
            val filterdSets = allSets.sets
                .filter { it.year >= fromYear && it.pieces >= minPartsCount }
            val filteredSetsCount = filterdSets.size

            filterdSets.take(3).forEachIndexed { index, set ->

                val setDefinedPartsCount = rebrickable.fetchSetParts(set.number)
                    .results
                    .filter { legoSetPart ->
                        definedParts.any {
                            "${it.partNumber}" == legoSetPart.part.partNumber
                        }
                    }
                    .sumBy { it.quantity }

                val legoSet =
                    LegoSet(set.number, set.name, set.year, set.pieces, set.image.thumbnailURL)
                result.postValue(LegoSetDefinedParts(legoSet, setDefinedPartsCount))

                progress.postValue(AllSetsProgress(index + 1, filteredSetsCount))
            }
        }
        return result
    }

    private suspend fun retrieveSets(userHash: String): GetSetsResult {
        val params = GetSetsParams(PAGE_SIZE, THEME)
        val getSetRequest = GetSets(BuildConfig.REBRICKABLE_API_KEY, userHash, params)
        return brickset.getSets(getSetRequest)
    }

    private suspend fun loginUser(): LoginResult {
        val loginRequest = Login(
            BuildConfig.BRICKSET_API_KEY,
            BuildConfig.BRICKSET_USER_NAME,
            BuildConfig.BRICKSET_PASSWORD
        )
        return brickset.login(loginRequest)
    }
}
