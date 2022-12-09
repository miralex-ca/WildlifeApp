package com.muralex.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.muralex.compose.ui.theme.ComposeTheme
import com.muralex.compose.viewmodel.HomeContract
import com.muralex.compose.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        processUiEvent(HomeContract.UserAction.LaunchScreen)

        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { uiState ->
                Timber.d("data: ${uiState.toString()}")
            }
        }

        setContent {
            ComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }

    private fun processUiEvent(userAction: HomeContract.UserAction) {
        when (userAction) {
            is HomeContract.UserAction.LaunchScreen -> setIntent(HomeContract.ViewIntent.GetData)
            is HomeContract.UserAction.ListItemClick -> setIntent(HomeContract.ViewIntent.Navigate(userAction.section))
        }
    }

    private fun setIntent(intent: HomeContract.ViewIntent) {
        viewModel.setIntent(intent)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTheme {
        Greeting("Android")
    }
}