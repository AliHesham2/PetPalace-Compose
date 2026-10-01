package com.alagamb.petcompose.ui.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import com.alagamb.petcompose.R
import com.alagamb.petcompose.data.model.dashboard.FeaturedPetItem
import com.alagamb.petcompose.ui.commonui.navigation.AppAdaptiveNavigationScaffold
import com.alagamb.petcompose.ui.commonui.navigation.BottomNavItem
import com.alagamb.petcompose.ui.commonui.navigation.RailAlignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alagamb.petcompose.ui.commonui.buttons.AppIconButton
import com.alagamb.petcompose.ui.commonui.navigation.AppModalDrawer
import com.alagamb.petcompose.ui.commonui.navigation.AppTopBar
import com.alagamb.petcompose.ui.commonui.navigation.DrawerNavItem
import com.alagamb.petcompose.ui.nav.AppRoute
import com.alagamb.petcompose.ui.screens.dashboard.DashboardMockData
import com.alagamb.petcompose.ui.screens.dashboard.DashboardRoute
import com.alagamb.petcompose.ui.screens.dashboard.DashboardViewModel
import com.alagamb.petcompose.ui.screens.dashboard.FullScreenSearchOverlay
import com.alagamb.petcompose.ui.screens.dashboard.MainTab
import com.alagamb.petcompose.ui.screens.profile.ProfileRoute
import com.alagamb.petcompose.ui.theme.PetComposeTheme
import kotlinx.coroutines.launch

@Composable
fun MainRoute(
    modifier: Modifier = Modifier,
    onNavigateToRequests: () -> Unit,
    onNavigateToProductList: (title: String) -> Unit = {},
    onNavigateToProductDetails: (productId: String) -> Unit = {},
    onNavigateToAddPet: () -> Unit = {},
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val roomFeaturedPets by viewModel.featuredPets.collectAsStateWithLifecycle()
    val featuredPets = if (roomFeaturedPets.isNotEmpty()) roomFeaturedPets else DashboardMockData.featuredPets

    var isSearchOpen by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val searchHistory = remember { mutableStateListOf("Cat", "Golden Retriever", "Puppy") }

    MainScreen(
        selectedTab = selectedTab,
        onTabSelected = { viewModel.selectTab(it) },
        onNavigateToRequests = onNavigateToRequests,
        onNavigateToAddPet = onNavigateToAddPet,
        isSearchOpen = isSearchOpen,
        searchQuery = searchQuery,
        searchHistory = searchHistory,
        featuredPets = featuredPets,
        onSearchClick = { isSearchOpen = true },
        onCloseSearch = {
            isSearchOpen = false
            searchQuery = ""
        },
        onQueryChange = { searchQuery = it },
        onRemoveHistory = { searchHistory.remove(it) },
        onClearAllHistory = { searchHistory.clear() },
        onPetClick = { petId ->
            if (searchQuery.isNotBlank() && !searchHistory.contains(searchQuery.trim())) {
                searchHistory.add(0, searchQuery.trim())
            }
            isSearchOpen = false
            onNavigateToProductDetails(petId)
        },
        modifier = modifier
    ) { currentTab, innerModifier ->
        when (currentTab) {
            MainTab.DASHBOARD -> DashboardRoute(
                viewModel = viewModel,
                onCategoryClick = { categoryName -> onNavigateToProductList(categoryName) },
                onSeeAllClick = { onNavigateToProductList("Featured Pets") },
                onPetClick = { petId -> onNavigateToProductDetails(petId) },
                onAddPetClick = onNavigateToAddPet,
                onSearchClick = { isSearchOpen = true },
                modifier = innerModifier
            )
            MainTab.PROFILE -> ProfileRoute(
                onNavigateToRequests = onNavigateToRequests,
                modifier = innerModifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    onNavigateToRequests: () -> Unit,
    onNavigateToAddPet: () -> Unit = {},
    isSearchOpen: Boolean = false,
    searchQuery: String = "",
    searchHistory: List<String> = emptyList(),
    featuredPets: List<FeaturedPetItem> = emptyList(),
    onSearchClick: () -> Unit = {},
    onCloseSearch: () -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onRemoveHistory: (String) -> Unit = {},
    onClearAllHistory: () -> Unit = {},
    onPetClick: (String) -> Unit = {},
    content: @Composable (MainTab, Modifier) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val dashboardLabel = stringResource(R.string.nav_dashboard)
    val requestsLabel = stringResource(R.string.nav_requests)
    val addPetLabel = stringResource(R.string.dashboard_add_pet)
    val profileLabel = stringResource(R.string.nav_profile)
    val appTitle = stringResource(R.string.app_name)
    val myProfileTitle = stringResource(R.string.profile_screen_title)

    val drawerNavItems = remember(dashboardLabel, requestsLabel) {
        listOf(
            DrawerNavItem(
                route = AppRoute.MAIN_ROUTE,
                label = dashboardLabel,
                icon = Icons.Default.Dashboard
            ),
            DrawerNavItem(
                route = AppRoute.REQUESTS_ROUTE,
                label = requestsLabel,
                icon = Icons.AutoMirrored.Filled.Assignment
            )
        )
    }

    // Back handling:
    // 1. Close search if open
    // 2. Close drawer if open
    // 3. If on Profile tab, switch back to Dashboard tab
    BackHandler(enabled = isSearchOpen || drawerState.isOpen || selectedTab != MainTab.DASHBOARD) {
        if (isSearchOpen) {
            onCloseSearch()
        } else if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else if (selectedTab != MainTab.DASHBOARD) {
            onTabSelected(MainTab.DASHBOARD)
        }
    }

    val navItems = remember(dashboardLabel, profileLabel) {
        listOf(
            BottomNavItem(
                route = MainTab.DASHBOARD.name,
                label = dashboardLabel,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            ),
            BottomNavItem(
                route = MainTab.PROFILE.name,
                label = profileLabel,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person
            )
        )
    }

    AppModalDrawer(
        drawerState = drawerState,
        items = drawerNavItems,
        currentRoute = AppRoute.MAIN_ROUTE,
        gesturesEnabled = selectedTab == MainTab.DASHBOARD && !isSearchOpen,
        onNavigate = { route ->
            coroutineScope.launch { drawerState.close() }
            if (route == AppRoute.REQUESTS_ROUTE) {
                onNavigateToRequests()
            }
        }
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            AppAdaptiveNavigationScaffold(
                items = navItems,
                currentRoute = selectedTab.name,
                onNavigate = { route ->
                    onTabSelected(MainTab.valueOf(route))
                },
                modifier = Modifier.fillMaxSize(),
                railAlignment = RailAlignment.Center,
                topBar = {
                    when (selectedTab) {
                        MainTab.DASHBOARD -> {
                            AppTopBar(
                                title = appTitle,
                                centerTitle = true,
                                navigationIcon = Icons.Default.Menu,
                                onNavigationClick = {
                                    coroutineScope.launch { drawerState.open() }
                                },
                                actions = {
                                    AppIconButton(
                                        icon = Icons.Default.AddCircleOutline,
                                        contentDescription = addPetLabel,
                                        onClick = onNavigateToAddPet
                                    )
                                    AppIconButton(
                                        icon = Icons.Default.AccountCircle,
                                        contentDescription = profileLabel,
                                        onClick = { onTabSelected(MainTab.PROFILE) }
                                    )
                                }
                            )
                        }
                        MainTab.PROFILE -> {
                            AppTopBar(
                                title = myProfileTitle,
                                centerTitle = true
                            )
                        }
                    }
                }
            ) { innerPadding ->
                content(selectedTab, Modifier.padding(innerPadding))
            }

            AnimatedVisibility(
                visible = isSearchOpen,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                FullScreenSearchOverlay(
                    query = searchQuery,
                    onQueryChange = onQueryChange,
                    onClose = onCloseSearch,
                    historyItems = searchHistory,
                    onRemoveHistory = onRemoveHistory,
                    onClearAllHistory = onClearAllHistory,
                    pets = featuredPets,
                    onPetClick = onPetClick
                )
            }
        }
    }
}

@Preview(
    name = "Main Screen - Phone",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun MainScreenPreview() {
    PetComposeTheme {
        MainScreen(
            selectedTab = MainTab.DASHBOARD,
            onTabSelected = {},
            onNavigateToRequests = {},
            onNavigateToAddPet = {},
            modifier = Modifier.statusBarsPadding()
        ) { _, modifier ->
            Text(
                text = "Content Preview",
                modifier = modifier
            )
        }
    }
}

@Preview(
    name = "Main Screen - Foldable Navigation Rail",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=673dp,height=841dp,dpi=420"
)
@Composable
fun MainScreenFoldablePreview() {
    PetComposeTheme {
        MainScreen(
            selectedTab = MainTab.DASHBOARD,
            onTabSelected = {},
            onNavigateToRequests = {},
            onNavigateToAddPet = {}
        ) { _, modifier ->
            Text(
                text = "Foldable Content with Navigation Rail",
                modifier = modifier
            )
        }
    }
}

@Preview(
    name = "Main Screen - Tablet Navigation Rail",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Composable
fun MainScreenTabletPreview() {
    PetComposeTheme {
        MainScreen(
            selectedTab = MainTab.DASHBOARD,
            onTabSelected = {},
            onNavigateToRequests = {},
            onNavigateToAddPet = {}
        ) { _, modifier ->
            Text(
                text = "Tablet Content with Navigation Rail",
                modifier = modifier
            )
        }
    }
}
