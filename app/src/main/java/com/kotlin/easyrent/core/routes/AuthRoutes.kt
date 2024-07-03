package com.kotlin.easyrent.core.routes

sealed class AuthRoutes(val destination: String ) {
    data object Login : AuthRoutes(destination = "login")
    data object Signup: AuthRoutes(destination = "signup")
}