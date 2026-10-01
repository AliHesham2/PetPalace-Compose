package com.alagamb.petcompose.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.alagamb.petcompose.ui.screens.addpet.AddPetRoute
import com.alagamb.petcompose.ui.screens.main.MainRoute
import com.alagamb.petcompose.ui.screens.products.productdetails.ProductDetailsRoute
import com.alagamb.petcompose.ui.screens.products.productlist.ProductListRoute
import com.alagamb.petcompose.ui.screens.registration.LandRoute
import com.alagamb.petcompose.ui.screens.registration.LoginRoute
import com.alagamb.petcompose.ui.screens.registration.RegisterRoute
import com.alagamb.petcompose.ui.screens.requests.RequestsRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppNavGraph(
    modifier       : Modifier          = Modifier,
    navController  : NavHostController = rememberNavController(),
    coroutineScope : CoroutineScope    = rememberCoroutineScope(),
    startDestination: String           = AppRoute.AUTH_GRAPH,
    onToggleLanguage: () -> Unit       = {},
    navActions     : AppNavigation     = remember(navController) { AppNavigation(navController) }
){

    NavHost(
        navController    = navController,
        startDestination = startDestination,
        modifier         = modifier,
    ){
        // ── Auth Sub-Graph ─────
        navigation(
            startDestination = AppRoute.LAND_ROUTE,
            route            = AppRoute.AUTH_GRAPH,
        ){
            composable(route = AppRoute.LAND_ROUTE){
                LandRoute(
                    onClick = { isLogin ->
                        if (isLogin) navActions.navToLoginScreen()
                        else navActions.navToRegisterScreen()
                    },
                    onToggleLanguage = onToggleLanguage,
                    modifier = Modifier
                )
            }

            composable(route = AppRoute.LOGIN_ROUTE){
                LoginRoute(
                    onBackClick = { navController.popBackStack() },
                    onNavigateToDashboard = { navActions.navToMain() },
                    onNavigateToRegister = { navActions.navToRegisterScreen() },
                    modifier = Modifier
                )
            }

            composable(route = AppRoute.REGISTER_ROUTE){
                RegisterRoute(
                    onBackClick = { navController.popBackStack() },
                    onNavigateToDashboard = { navActions.navToMain() },
                    onNavigateToLogin = { navActions.navToLoginScreen() },
                    modifier = Modifier
                )
            }
        }

        // ── Main Destination (Hosts Dashboard & Profile with BottomBar) ─────
        composable(route = AppRoute.MAIN_ROUTE) {
            MainRoute(
                onNavigateToRequests = { navActions.navToRequests() },
                onNavigateToProductList = { title -> navActions.navToProductList(title) },
                onNavigateToProductDetails = { productId -> navActions.navToProductDetails(productId) },
                onNavigateToAddPet = { navActions.navToAddPet() },
                modifier = Modifier
            )
        }

        // ── Requests Destination (Secondary screen with Back button, NO BottomBar) ─────
        composable(route = AppRoute.REQUESTS_ROUTE) {
            RequestsRoute(
                onBackClick = { navController.popBackStack() },
                modifier = Modifier
            )
        }

        // ── Product List Destination (Secondary screen with Back button, NO BottomBar) ─────
        composable(
            route = AppRoute.PRODUCT_LIST_ROUTE,
            arguments = listOf(
                navArgument(AppArgs.CATEGORY_TITLE) { type = NavType.StringType }
            )
        ) {
            ProductListRoute(
                onBackClick = { navController.popBackStack() },
                modifier = Modifier
            )
        }

        // ── Product Details Destination (Secondary screen with Back button, NO BottomBar) ─────
        composable(
            route = AppRoute.PRODUCT_DETAILS_ROUTE,
            arguments = listOf(
                navArgument(AppArgs.PRODUCT_ID) { type = NavType.StringType }
            )
        ) {
            ProductDetailsRoute(
                onBackClick = { navController.popBackStack() },
                modifier = Modifier
            )
        }

        // ── Add Pet Destination (Secondary screen with Back button, NO BottomBar) ─────
        composable(route = AppRoute.ADD_PET_ROUTE) {
            AddPetRoute(
                onBackClick = { navController.popBackStack() },
                modifier = Modifier
            )
        }
    }

}