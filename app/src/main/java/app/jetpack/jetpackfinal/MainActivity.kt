package app.jetpack.jetpackfinal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import app.jetpack.jetpackfinal.ui.home.ui.screen.CoinDetailScreen
import app.jetpack.jetpackfinal.ui.home.ui.screen.HomeScreen
import app.jetpack.jetpackfinal.ui.home.ui.viewmodel.AuthViewModel
import app.jetpack.jetpackfinal.ui.home.ui.viewmodel.CoinDetailViewModel
import app.jetpack.jetpackfinal.ui.home.ui.viewmodel.HomeViewModel
import app.jetpack.jetpackfinal.ui.theme.JetpackFinalTheme
import app.jetpack.jetpackfinal.utils.DestinationScreen
import androidx.navigation.compose.composable
import app.jetpack.jetpackfinal.ui.home.ui.screen.BuySellScreen
import app.jetpack.jetpackfinal.ui.home.ui.screen.LoginScreen
import app.jetpack.jetpackfinal.ui.home.ui.screen.UserPortfolioScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackFinalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Navigation(){
    val navController = rememberNavController()
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val authViewModel = hiltViewModel<AuthViewModel>()
    val coinDetailViewModel = hiltViewModel<CoinDetailViewModel>()
    NavHost(navController = navController, startDestination = DestinationScreen.Home.route){
        composable(DestinationScreen.Home.route){
            HomeScreen(navController, homeViewModel)
        }
        composable(DestinationScreen.CoinDetail.route){
            val coinId = it.arguments?.getString("coinId")
            coinId?.let {
                CoinDetailScreen(navController, coinDetailViewModel, coinId)
            }
        }
        composable(DestinationScreen.UserPortfolio.route){
            UserPortfolioScreen()
        }
        composable(DestinationScreen.Login.route) {
            LoginScreen(navController, authViewModel)
        }
        composable(DestinationScreen.BuySellScreen.route){
            val coinId = it.arguments?.getString("coinId")
            coinId?.let {
                BuySellScreen(navController, coinDetailViewModel, coinId)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackFinalTheme {
        Greeting("Android")
    }
}