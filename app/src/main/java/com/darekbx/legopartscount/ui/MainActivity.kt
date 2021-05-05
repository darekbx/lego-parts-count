package com.darekbx.legopartscount.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.darekbx.legopartscount.viewmodel.DefinedPartsViewModel
import com.darekbx.legopartscount.viewmodel.RebrickableViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModel<RebrickableViewModel>()
    private val definedPartsViewModel by viewModel<DefinedPartsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LegoPartsCountTheme(darkTheme = false) {
                NavigationApp(mainViewModel, definedPartsViewModel)
            }
        }
    }
}
