package com.technet.olttv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.technet.olttv.ui.AuthViewModel
import com.technet.olttv.ui.DashboardTvScreen
import com.technet.olttv.ui.DashboardViewModel
import com.technet.olttv.ui.PinLoginScreen
import com.technet.olttv.ui.theme.OLTTVMonitorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OLTTVMonitorTheme {
                val authViewModel: AuthViewModel = viewModel()
                val dashboardViewModel: DashboardViewModel = viewModel()

                if (authViewModel.isAuthenticated) {
                    DashboardTvScreen(
                        viewModel = dashboardViewModel,
                        onLogout = { authViewModel.logout() }
                    )
                } else {
                    PinLoginScreen(viewModel = authViewModel)
                }
            }
        }
    }
}
