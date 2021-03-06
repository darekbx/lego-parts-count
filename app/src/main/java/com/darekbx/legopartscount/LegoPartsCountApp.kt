package com.darekbx.legopartscount

import android.app.Application
import com.darekbx.legopartscount.repository.brickset.getBricketService
import com.darekbx.legopartscount.repository.database.AppDatabase
import com.darekbx.legopartscount.repository.rebrickable.getRebrickableService
import com.darekbx.legopartscount.viewmodel.AllSetsViewModel
import com.darekbx.legopartscount.viewmodel.DefinedPartsViewModel
import com.darekbx.legopartscount.viewmodel.RebrickableViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class LegoPartsCountApp: Application() {

    private val commonModule = module {
        single { getRebrickableService() }
        single { getBricketService() }
    }

    private val databaseModule = module {
        single { AppDatabase.getDatabase(get()) }
        single { (get() as AppDatabase).definedPartsDao() }
        single { (get() as AppDatabase).legoSetDefinedPartsDao() }
    }

    private val viewModelModule = module {
        viewModel { RebrickableViewModel(get(), get()) }
        viewModel { AllSetsViewModel(get(), get(), get(), get()) }
        viewModel { DefinedPartsViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidContext(this@LegoPartsCountApp)
            modules(commonModule, viewModelModule, databaseModule)
        }
    }
}
