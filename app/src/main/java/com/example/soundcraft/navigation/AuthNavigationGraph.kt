package com.example.soundcraft.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.soundcraft.ui.auth.AuthViewModel
import com.example.soundcraft.ui.auth.ForgotPasswordScreen
import com.example.soundcraft.ui.auth.LoginScreen
import com.example.soundcraft.ui.auth.RegisterScreen
import com.example.soundcraft.ui.main.MainScreen
import com.example.soundcraft.ui.splash.SplashScreen

/** Las "direcciones" de cada pantalla. */
sealed class AuthScreen(val route: String) {
    data object Splash : AuthScreen("splash")
    data object Login : AuthScreen("login")
    data object Register : AuthScreen("register")
    data object ForgotPassword : AuthScreen("forgot_password")
    data object Home : AuthScreen("home")
}

/** Mapa de navegación: qué pantalla se muestra y cómo se pasa de una a otra. */
@Composable
fun AuthNavigationGraph(navController: NavHostController) {
    // Un solo ViewModel compartido por las pantallas de autenticación
    val authViewModel: AuthViewModel = viewModel()

    // Ir al Inicio borrando el historial: con "atrás" no se vuelve al login
    val irAlInicio = {
        navController.navigate(AuthScreen.Home.route) {
            popUpTo(AuthScreen.Login.route) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = AuthScreen.Splash.route) {
        composable(AuthScreen.Splash.route) {
            SplashScreen(onTerminar = {
                navController.navigate(AuthScreen.Login.route) {
                    popUpTo(AuthScreen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(AuthScreen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = irAlInicio,
                onNavigateToRegister = { navController.navigate(AuthScreen.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(AuthScreen.ForgotPassword.route) },
            )
        }
        composable(AuthScreen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = irAlInicio,
                onNavigateToLogin = { navController.popBackStack() },
            )
        }
        composable(AuthScreen.ForgotPassword.route) {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() },
            )
        }
        composable(AuthScreen.Home.route) {
            val usuario by authViewModel.usuarioActual.collectAsState()
            MainScreen(
                usuario = usuario,
                onCerrarSesion = {
                    authViewModel.logout()
                    navController.navigate(AuthScreen.Login.route) {
                        popUpTo(AuthScreen.Home.route) { inclusive = true }
                    }
                },
            )
        }
    }
}
