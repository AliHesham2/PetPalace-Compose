package com.alagamb.petcompose.ui.nav

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

private object AppScreens {
    const val AUTH_GRAPH_ROUTE = "auth_graph"
    const val LANDING_PAGE = "land_screen"
    const val LOGIN_PAGE = "login_screen"
    const val REGISTER_PAGE = "register_screen"
    const val MAIN_PAGE = "main_screen"
    const val DASHBOARD_PAGE = "dashboard_screen"
    const val PROFILE_PAGE = "profile_screen"
    const val REQUESTS_PAGE = "requests_screen"
    const val PRODUCT_LIST_PAGE = "product_list_screen"
    const val PRODUCT_DETAILS_PAGE = "product_details_screen"
    const val ADD_PET_PAGE = "add_pet_screen"
}

object AppArgs {
    const val CATEGORY_TITLE = "category_title"
    const val PRODUCT_ID = "product_id"
}

object AppRoute {
    const val AUTH_GRAPH = AppScreens.AUTH_GRAPH_ROUTE
    const val LAND_ROUTE = AppScreens.LANDING_PAGE
    const val LOGIN_ROUTE = AppScreens.LOGIN_PAGE
    const val REGISTER_ROUTE = AppScreens.REGISTER_PAGE
    const val MAIN_ROUTE = AppScreens.MAIN_PAGE
    const val DASHBOARD_ROUTE = AppScreens.DASHBOARD_PAGE
    const val PROFILE_ROUTE = AppScreens.PROFILE_PAGE
    const val REQUESTS_ROUTE = AppScreens.REQUESTS_PAGE
    const val PRODUCT_LIST_ROUTE = "${AppScreens.PRODUCT_LIST_PAGE}/{${AppArgs.CATEGORY_TITLE}}"
    const val PRODUCT_DETAILS_ROUTE = "${AppScreens.PRODUCT_DETAILS_PAGE}/{${AppArgs.PRODUCT_ID}}"
    const val ADD_PET_ROUTE = AppScreens.ADD_PET_PAGE
}

class AppNavigation(private val navController: NavHostController) {

    fun navToLandScreen() {
        navController.navigate(AppRoute.LAND_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navToLoginScreen() {
        navController.navigate(AppRoute.LOGIN_ROUTE) {
            launchSingleTop = true
        }
    }

    fun navToRegisterScreen() {
        navController.navigate(AppRoute.REGISTER_ROUTE) {
            launchSingleTop = true
        }
    }

    fun navToMain() {
        navController.navigate(AppRoute.MAIN_ROUTE) {
            popUpTo(AppRoute.AUTH_GRAPH) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navToAuth() {
        navController.navigate(AppRoute.AUTH_GRAPH) {
            popUpTo(AppRoute.MAIN_ROUTE) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navToRequests() {
        navController.navigate(AppRoute.REQUESTS_ROUTE) {
            launchSingleTop = true
        }
    }

    fun navToProductList(title: String) {
        val encodedTitle = android.net.Uri.encode(title)
        navController.navigate("${AppScreens.PRODUCT_LIST_PAGE}/$encodedTitle") {
            launchSingleTop = true
        }
    }

    fun navToProductDetails(productId: String) {
        val encodedId = android.net.Uri.encode(productId)
        navController.navigate("${AppScreens.PRODUCT_DETAILS_PAGE}/$encodedId") {
            launchSingleTop = true
        }
    }

    fun navToAddPet() {
        navController.navigate(AppRoute.ADD_PET_ROUTE) {
            launchSingleTop = true
        }
    }
}