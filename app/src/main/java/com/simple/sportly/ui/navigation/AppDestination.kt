package com.simple.sportly.ui.navigation

sealed class AppDestination(val route: String) {
    data object Login : AppDestination("login")
    data object Register : AppDestination("register")
    data object ClientHome : AppDestination("client_home")
    data object TrainerHome : AppDestination("trainer_home")
}
