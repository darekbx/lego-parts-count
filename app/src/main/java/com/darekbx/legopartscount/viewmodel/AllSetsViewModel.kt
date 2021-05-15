package com.darekbx.legopartscount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.darekbx.legopartscount.BuildConfig
import com.darekbx.legopartscount.repository.brickset.Brickset
import com.darekbx.legopartscount.repository.brickset.model.*
import com.darekbx.legopartscount.repository.brickset.model.Set
import com.darekbx.legopartscount.repository.database.DefinedPartDao
import com.darekbx.legopartscount.repository.database.DefinedPartEntity
import com.darekbx.legopartscount.repository.database.LegoSetDefinedPartsDao
import com.darekbx.legopartscount.repository.database.LegoSetDefinedPartsEntity
import com.darekbx.legopartscount.repository.rebrickable.Rebrickable
import com.darekbx.legopartscount.repository.rebrickable.model.LegoSet
import com.google.gson.Gson
import kotlinx.coroutines.delay

class LegoSetDefinedParts(
    val legoSet: LegoSet,
    val definedPartsCount: Int
) {

    val ratio = definedPartsCount / legoSet.partsCount.toFloat()

    fun toEntity() = LegoSetDefinedPartsEntity(
        legoSet.setNumber,
        definedPartsCount,
        legoSet.name,
        legoSet.year,
        legoSet.partsCount,
        legoSet.setImageUrl
    )

    companion object {

        fun fromEntity(entity: LegoSetDefinedPartsEntity) = LegoSetDefinedParts(
            LegoSet(
                entity.setNumber,
                entity.setName,
                entity.setYear,
                entity.partsCount,
                entity.setImageUrl
            ),
            entity.definedPartsCount
        )
    }
}

class AllSetsProgress(
    val progress: Int,
    val max: Int
)

class AllSetsViewModel(
    private val brickset: Brickset,
    private val rebrickable: Rebrickable,
    private val definedPartDao: DefinedPartDao,
    private val legoSetDefinedPartsDao: LegoSetDefinedPartsDao
) : BaseViewModel() {

    companion object {
        private const val LOAD_ONLINE = true
        private const val PAGE_SIZE = 1000
        private const val THEME = "Technic"
    }

    private val _result = MutableLiveData<LegoSetDefinedParts>()
    val result: LiveData<LegoSetDefinedParts> = _result

    private val _setsAdded = MutableLiveData<Int?>(null)
    val setsAdded: LiveData<Int?> = _setsAdded

    val progress = MutableLiveData<AllSetsProgress>()

    fun loadAllSets(fromYear: Int, minPartsCount: Int) {
        launchDataLoad {

            // Hide loading for stored sets
            loadingState.value = false
            val definedSets = legoSetDefinedPartsDao.selectAll()
            val definedSetsCount = definedSets.size
            definedSets.forEachIndexed { index, set ->

                _result.value = LegoSetDefinedParts.fromEntity(set)
                progress.value = AllSetsProgress(index + 1, definedSetsCount)

                // Delay loading, to deal with backpressure (should be refacored to flow)
                delay(50)
            }

            if (LOAD_ONLINE) {
                loadingState.value = true
                val storedDefinedSetNumbers = legoSetDefinedPartsDao.selectSetNumbers()
                val definedParts = definedPartDao.selectAll()
                val allSets = retrieveSets()
                val filterdSets = allSets.sets
                    .filter {
                        // it.number does not contain "-1"
                        !storedDefinedSetNumbers.contains(it.number)
                                && it.year >= fromYear
                                && it.pieces >= minPartsCount
                    }
                val filteredSetsCount = filterdSets.size

                filterdSets.forEachIndexed { index, set ->
                    val legoSetDefinedParts = fetchSetDetails(set, definedParts)
                    saveLegoSetDefinedParts(legoSetDefinedParts)

                    _result.postValue(legoSetDefinedParts)
                    progress.postValue(AllSetsProgress(index + 1, filteredSetsCount))
                }

                _setsAdded.postValue(filteredSetsCount)
            }
        }
    }

    private suspend fun saveLegoSetDefinedParts(legoSetDefinedParts: LegoSetDefinedParts) {
        legoSetDefinedPartsDao.insert(legoSetDefinedParts.toEntity())
    }

    private suspend fun fetchSetDetails(
        set: Set,
        definedParts: List<DefinedPartEntity>
    ): LegoSetDefinedParts {
        val setParts = rebrickable.fetchSetParts("${set.number}-1").results
        val setDefinedPartsCount =
            setParts
                .filter { legoSetPart ->
                    definedParts.any {
                        "${it.partNumber}" == legoSetPart.part.partNumber
                    }
                }
                .sumBy { it.quantity }

        val legoSet = LegoSet(set.number, set.name, set.year, set.pieces, set.image.thumbnailURL)
        return LegoSetDefinedParts(legoSet, setDefinedPartsCount)
    }

    private suspend fun retrieveSets(): GetSetsResult {
        val loginResult = loginUser()
        val params = GetSetsParams(PAGE_SIZE, THEME)
        return brickset.getSets(BuildConfig.BRICKSET_API_KEY, loginResult.hash, gson.toJson(params))
    }

    private suspend fun loginUser(): LoginResult {
        return brickset.login(
            BuildConfig.BRICKSET_API_KEY,
            BuildConfig.BRICKSET_USER_NAME,
            BuildConfig.BRICKSET_PASSWORD
        )
    }

    private val gson by lazy { Gson() }
}
