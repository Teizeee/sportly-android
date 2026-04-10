package com.simple.sportly.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simple.sportly.core.di.AppContainer
import com.simple.sportly.ui.screen.auth.login.LoginScreen
import com.simple.sportly.ui.screen.auth.login.LoginViewModel
import com.simple.sportly.ui.screen.auth.register.RegisterScreen
import com.simple.sportly.ui.screen.auth.register.RegisterViewModel
import com.simple.sportly.ui.screen.client.ClientHomeScreen
import com.simple.sportly.ui.screen.client.ClientHomeViewModel
import com.simple.sportly.ui.screen.trainer.TrainerHomeScreen
import com.simple.sportly.ui.screen.trainer.TrainerHomeViewModel
import com.simple.sportly.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SportlyApp(appContainer: AppContainer) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel(
        factory = MainViewModel.factory(appContainer.sessionStore)
    )
    val mainUiState by mainViewModel.uiState.collectAsState()

    if (mainUiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    SportlyNavGraph(
        navController = navController,
        startDestination = mainUiState.startDestination,
        appContainer = appContainer
    )

    LaunchedEffect(navController, appContainer.sessionStore) {
        appContainer.sessionStore.session.collectLatest { session ->
            if (session == null) {
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                val isAuthScreen = currentRoute == AppDestination.Login.route ||
                    currentRoute == AppDestination.Register.route

                if (!isAuthScreen) {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}

@Composable
private fun SportlyNavGraph(
    navController: NavHostController,
    startDestination: String,
    appContainer: AppContainer
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppDestination.Login.route) {
            val viewModel: LoginViewModel = viewModel(
                factory = LoginViewModel.factory(
                    authRepository = appContainer.authRepository,
                    sessionStore = appContainer.sessionStore
                )
            )
            val state by viewModel.uiState.collectAsState()

            LaunchedEffect(viewModel) {
                viewModel.navigationEvents.collect { route ->
                    navController.navigate(route) {
                        popUpTo(AppDestination.Login.route) { inclusive = true }
                    }
                }
            }

            LoginScreen(
                state = state,
                onEmailChange = viewModel::onEmailChanged,
                onPasswordChange = viewModel::onPasswordChanged,
                onLoginClick = viewModel::login,
                onRegisterClick = { navController.navigate(AppDestination.Register.route) }
            )
        }

        composable(AppDestination.Register.route) {
            val viewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModel.factory(appContainer.authRepository)
            )
            val state by viewModel.uiState.collectAsState()

            LaunchedEffect(viewModel) {
                viewModel.navigationEvents.collect {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.Register.route) { inclusive = true }
                    }
                }
            }

            RegisterScreen(
                state = state,
                onLastNameChange = viewModel::onLastNameChanged,
                onFirstNameChange = viewModel::onFirstNameChanged,
                onPatronymicChange = viewModel::onPatronymicChanged,
                onEmailChange = viewModel::onEmailChanged,
                onPasswordChange = viewModel::onPasswordChanged,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChanged,
                onRegisterClick = viewModel::register,
                onBackToLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppDestination.ClientHome.route) {
            val coroutineScope = rememberCoroutineScope()
            val viewModel: ClientHomeViewModel = viewModel(
                factory = ClientHomeViewModel.factory(appContainer.authRepository)
            )
            val state by viewModel.uiState.collectAsState()

            ClientHomeScreen(
                state = state,
                onTabSelected = viewModel::selectTab,
                onFirstNameChange = viewModel::onFirstNameChanged,
                onLastNameChange = viewModel::onLastNameChanged,
                onPatronymicChange = viewModel::onPatronymicChanged,
                onEmailChange = viewModel::onEmailChanged,
                onPushEnabledChange = viewModel::onPushEnabledChanged,
                onSaveClick = viewModel::saveProfile,
                onLogoutClick = {
                    coroutineScope.launch {
                        appContainer.sessionStore.clearSession()
                    }
                }
            )
        }

        composable(AppDestination.TrainerHome.route) {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val viewModel: TrainerHomeViewModel = viewModel(
                factory = TrainerHomeViewModel.factory(appContainer.authRepository)
            )
            val state by viewModel.uiState.collectAsState()

            TrainerHomeScreen(
                state = state,
                onTabSelected = viewModel::selectTab,
                onFirstNameChange = viewModel::onFirstNameChanged,
                onLastNameChange = viewModel::onLastNameChanged,
                onPatronymicChange = viewModel::onPatronymicChanged,
                onBirthDateChange = viewModel::onBirthDateChanged,
                onEmailChange = viewModel::onEmailChanged,
                onPhoneChange = viewModel::onPhoneChanged,
                onDescriptionChange = viewModel::onDescriptionChanged,
                onSaveClick = viewModel::saveProfile,
                onAvatarSelected = { uri ->
                    viewModel.uploadAvatar(uri, context.contentResolver)
                },
                onLogoutClick = {
                    coroutineScope.launch {
                        appContainer.sessionStore.clearSession()
                    }
                }
            )
        }
    }
}
