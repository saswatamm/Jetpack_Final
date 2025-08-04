package app.jetpack.jetpackfinal.utils

import androidx.navigation.NavController

fun NavigateTo(navController: NavController, route : String){
    navController.navigate(route){
        popUpTo(route){
            inclusive = true
        }
    }
}