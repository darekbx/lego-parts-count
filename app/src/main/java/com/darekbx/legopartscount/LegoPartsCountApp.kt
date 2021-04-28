package com.darekbx.legopartscount

import android.app.Application
import com.darekbx.legopartscount.repository.rebrickable.getRebrickableService
import com.darekbx.legopartscount.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class LegoPartsCountApp: Application() {

    private val commonModule = module {
        single { getRebrickableService() }
    }

    private val viewModelModule = module {
        viewModel { MainViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidContext(this@LegoPartsCountApp)
            modules(commonModule, viewModelModule)
        }
    }
}
