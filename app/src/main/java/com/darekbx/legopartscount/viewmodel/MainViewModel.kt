package com.darekbx.legopartscount.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darekbx.legopartscount.BuildConfig
import com.darekbx.legopartscount.repository.rebrickable.Rebrickable
import com.darekbx.legopartscount.repository.rebrickable.model.LegoPart
import com.darekbx.legopartscount.repository.rebrickable.model.LegoSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Flow:
 *
 *  Page size should be everywhere at least 10000
 *
 * At first user should define parts to search for
 *  - parts are defined by design number
 *  - there is autocomplete with dynamic search through GET /api/v3/lego/parts endpoint
 *  - user can search by design number or by name
 *  - from autocomplete parts are being added to the room database
 *
 * There will be theme autocomplete, filled during app start
 *  - when parts are not defined then there is no ability to serach by theme
 *  - themes are taken from api/v3/lego/themes?page_size=1000
 *  - themes should be groupped by parent_id to find all child themes
 *  - child themes should be displayed like: Techinc / Construction
 *
 * When theme is choosed and parts are defined then user can press button search
 *  - search is being executed by GET /api/v3/lego/sets/?theme_id= for main theme and child themes
 *  - every searched set should be searched with GET /api/v3/lego/sets/{set_num}/parts/ endpoint
 *  - parts should searched using defined parts and added to the list
 *  - during certain set search, list is visible and search results are displayed dynamically on the list
 *  - determinate progress is updating after every set
 *  - add button "Stop" to cancel search
 *
 */

class MainViewModel(
    private val rebrickable: Rebrickable
) : ViewModel() {

    val loadingState = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<Exception>()
    val setSearchResult = MutableLiveData<LegoSet>()
    val partSearchResult = MutableLiveData<List<LegoPart>>()

    fun searchForSet(setNumber: String) {
        launchDataLoad {
            setSearchResult.postValue(LegoSet("$setNumber-1", "Wheel", 2016, 3984, "url"))
        }
    }

    fun searchForPart(query: String) {
        launchDataLoad {
            val result = rebrickable.searchParts(query).results
            partSearchResult.postValue(result)
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                loadingState.value = true
                block()
            } catch (error: Exception) {
                if (BuildConfig.DEBUG) {
                    error.printStackTrace()
                }
                errorState.postValue(error)
            } finally {
                loadingState.value = false
            }
        }
    }
}
