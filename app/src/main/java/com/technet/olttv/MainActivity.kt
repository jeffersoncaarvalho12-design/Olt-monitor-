package com.technet.olttv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.technet.olttv.ui.DashboardTvScreen
import com.technet.olttv.ui.DashboardViewModel
import com.technet.olttv.ui.theme.OLTTVMonitorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OLTTVMonitorTheme {
                val vm: DashboardViewModel = viewModel()
                DashboardTvScreen(viewModel = vm)
            }
        }
    }
}
