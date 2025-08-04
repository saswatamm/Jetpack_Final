package app.jetpack.jetpackfinal.utils

sealed class DestinationScreen (var route : String) {
    object Splash : DestinationScreen("Splash")
    object Login : DestinationScreen("login")
    object Home : DestinationScreen("home")
    object CoinDetail : DestinationScreen("coinDetails/{coinId}"){
        fun createRoute(coinId : String) = "coinDetails/$coinId"
    }
    object UserPortfolio : DestinationScreen("user_portfolio")
    object BuySellScreen : DestinationScreen("buySell/{coinId}"){
        fun createRoute(coinId : String) = "buySell/$coinId"
    }
}