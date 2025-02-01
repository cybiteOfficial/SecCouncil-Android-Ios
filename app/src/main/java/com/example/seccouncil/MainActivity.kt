package com.example.seccouncil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.seccouncil.navigation.Navigation
import com.example.seccouncil.screens.SplashViewModel
import com.example.seccouncil.ui.theme.SecCouncilTheme
import com.example.seccouncil.utlis.DataStoreManger
import com.example.seccouncil.utlis.preferenceDataStore

class MainActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition{viewModel.isLoading.value}
        setContent {
            SecCouncilTheme {
                val dataStoreContext = LocalContext.current
                val dataStoreManger = DataStoreManger(dataStoreContext)
                Navigation(
                   preferenceDataStore =  preferenceDataStore,
                   dataStoreManger =  dataStoreManger,
                    context = dataStoreContext,
                    scope = lifecycleScope
                )
            }
        }
    }
}
