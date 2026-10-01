package com.alagamb.petcompose.ui.screens.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alagamb.petcompose.util.adaptiveCentered
import com.alagamb.petcompose.data.model.dashboard.AnnouncementItem
import com.alagamb.petcompose.data.model.dashboard.FeaturedPetItem
import com.alagamb.petcompose.data.model.dashboard.PetCategoryItem
import com.alagamb.petcompose.ui.commonui.buttons.AppExtendedFab
import com.alagamb.petcompose.ui.commonui.buttons.AppIconButton
import com.alagamb.petcompose.ui.commonui.buttons.AppTextButton
import com.alagamb.petcompose.ui.commonui.cards.AppCard
import com.alagamb.petcompose.ui.commonui.cards.AppElevatedCard
import com.alagamb.petcompose.ui.commonui.customize.AppModifier
import com.alagamb.petcompose.ui.commonui.customize.AppShape
import com.alagamb.petcompose.ui.commonui.pager.AppHorizontalPager
import com.alagamb.petcompose.ui.commonui.pager.AppPagerStyles
import com.alagamb.petcompose.ui.commonui.pager.rememberAdaptivePageSize
import com.alagamb.petcompose.ui.commonui.surface.AppSurface
import com.alagamb.petcompose.ui.commonui.surface.AppSurfaceStyles
import com.alagamb.petcompose.ui.commonui.customize.AppColors
import com.alagamb.petcompose.ui.theme.PetComposeTheme
import androidx.compose.ui.res.stringResource
import com.alagamb.petcompose.R

import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DashboardRoute(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel? = null,
    onCategoryClick: (categoryName: String) -> Unit = {},
    onSeeAllClick: () -> Unit = {},
    onPetClick: (petId: String) -> Unit = {},
    onAddPetClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    DashboardScreen(
        viewModel = viewModel,
        onCategoryClick = onCategoryClick,
        onSeeAllClick = onSeeAllClick,
        onPetClick = onPetClick,
        onAddPetClick = onAddPetClick,
        onSearchClick = onSearchClick,
        windowAdaptiveInfo = windowAdaptiveInfo,
        modifier = modifier
    )
}

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel? = null,
    onCategoryClick: (categoryName: String) -> Unit = {},
    onSeeAllClick: () -> Unit = {},
    onPetClick: (petId: String) -> Unit = {},
    onAddPetClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val roomAnnouncements = viewModel?.announcements?.collectAsStateWithLifecycle()?.value.orEmpty()
    val roomCategories = viewModel?.categories?.collectAsStateWithLifecycle()?.value.orEmpty()
    val roomFeaturedPets = viewModel?.featuredPets?.collectAsStateWithLifecycle()?.value.orEmpty()

    val announcements = if (roomAnnouncements.isNotEmpty()) roomAnnouncements else DashboardMockData.announcements
    val categories = if (roomCategories.isNotEmpty()) roomCategories else DashboardMockData.categories
    val featuredPetsList = if (roomFeaturedPets.isNotEmpty()) roomFeaturedPets else DashboardMockData.featuredPets
    val pagerState = rememberPagerState(pageCount = { announcements.size })

    val columnCount = when {
        windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> 4
        windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> 3
        else -> 2
    }

    val isCompactHeight = !windowAdaptiveInfo.windowSizeClass.isHeightAtLeastBreakpoint(
        WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
    )

    Box(modifier = modifier.then(DashboardModifiers.root)) {

        // Main Dashboard content
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount),
            modifier = DashboardModifiers.grid,
            contentPadding = DashboardModifiers.gridContentPadding,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Search Bar (pill-shaped, centered, NO filter, click to animate full screen)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = DashboardModifiers.itemSpanFull,
                    contentAlignment = Alignment.Center
                ) {
                    DashboardSearchTriggerBar(
                        onClick = onSearchClick,
                        modifier = DashboardModifiers.searchTriggerContainer
                    )
                }
            }

            // 2. Announcements Carousel (Using AppHorizontalPager from commonUI)
            item(span = { GridItemSpan(maxLineSpan) }) {
                AppHorizontalPager(
                    state = pagerState,
                    style = AppPagerStyles.worm(),
                    showIndicator = true,
                    indicatorPadding = if (isCompactHeight) 6.dp else 10.dp,
                    pageSize = rememberAdaptivePageSize(windowAdaptiveInfo, expandedVisiblePages = 3f),
                    contentPadding = PaddingValues(0.dp),
                    pageSpacing = 16.dp,
                    beyondViewportPageCount = 1,
                    modifier = DashboardModifiers.itemSpanFull
                ) { page ->
                    val announcement = announcements[page]
                    AnnouncementCard(
                        announcement = announcement,
                        isCompactHeight = isCompactHeight,
                        modifier = DashboardModifiers.itemSpanFull
                    )
                }
            }

            // 3. Pet Categories (Matching reference image: circular pastel icons with label below)
            item(span = { GridItemSpan(maxLineSpan) }) {
                DashboardCategoriesSection(
                    categories = categories,
                    onCategoryClick = onCategoryClick,
                    windowAdaptiveInfo = windowAdaptiveInfo,
                    contentPadding = PaddingValues(0.dp)
                )
            }

            // 4. Featured Section Header ("Featured Pets" left, "See All" right)
            item(span = { GridItemSpan(maxLineSpan) }) {
                FeaturedPetsHeader(
                    onSeeAllClick = onSeeAllClick,
                    modifier = DashboardModifiers.itemSpanFull
                )
            }

            // 5. Featured Pets Adaptive Grid (Native individual items with direct recycling)
            items(featuredPetsList, key = { it.id }) { pet ->
                FeaturedPetCard(
                    pet = pet,
                    onFavoriteToggle = {
                        viewModel?.toggleFavorite(pet.id, pet.isFavorite)
                    },
                    onClick = { onPetClick(pet.id) },
                    modifier = DashboardModifiers.itemSpanFull
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 1. Search Bar Trigger (Pill shaped, exactly as before, NO filter button)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun DashboardSearchTriggerBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(R.string.dashboard_search_placeholder)
) {
    AppSurface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
        modifier = modifier.then(DashboardModifiers.searchTriggerSurface)
    ) {
        Row(
            modifier = DashboardModifiers.searchTriggerRow,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.cd_search),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 1B. Full Screen Search Overlay (White/Surface background, History & Results)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun FullScreenSearchOverlay(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    historyItems: List<String>,
    onRemoveHistory: (String) -> Unit,
    onClearAllHistory: () -> Unit,
    pets: List<FeaturedPetItem>,
    onPetClick: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val filteredPets = remember(query, pets) {
        if (query.isBlank()) emptyList()
        else {
            pets.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.breed.contains(query, ignoreCase = true) ||
                        it.gender.contains(query, ignoreCase = true)
            }
        }
    }

    AppSurface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // Search Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppIconButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    onClick = onClose
                )

                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .focusRequester(focusRequester),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = stringResource(R.string.dashboard_search_placeholder),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                        innerTextField()
                    }
                )

                if (query.isNotEmpty()) {
                    AppIconButton(
                        icon = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.cd_clear),
                        onClick = { onQueryChange("") }
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
            )

            // Content below search header: History or Results
            if (query.isBlank()) {
                // Search History Section
                if (historyItems.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard_recent_searches),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        AppTextButton(
                            text = stringResource(R.string.dashboard_clear_all),
                            onClick = onClearAllHistory
                        )
                    }

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(historyItems, key = { it }) { history ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onQueryChange(history) }
                                    .padding(horizontal = 20.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = history,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                AppIconButton(
                                    icon = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.cd_remove),
                                    onClick = { onRemoveHistory(history) },
                                    modifier = Modifier.size(28.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard_no_recent_searches),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Search Results Section
                if (filteredPets.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.dashboard_search_results_count, filteredPets.size),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredPets, key = { it.id }) { pet ->
                            AppElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = AppShape.Medium,
                                onClick = { onPetClick(pet.id) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Mini pet avatar with gradient background and icon
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(pet.cardGradient, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Pets,
                                            contentDescription = pet.name,
                                            tint = AppColors.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = pet.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${pet.breed} • ${pet.age}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Text(
                                        text = pet.distance,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard_no_pets_found, query),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 2. Announcement Card (Using AppElevatedCard and AppSurface from commonUI)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun AnnouncementCard(
    announcement: AnnouncementItem,
    modifier: Modifier = Modifier,
    isCompactHeight: Boolean = false,
    onClick: () -> Unit = {}
) {
    val pawIconSize = if (isCompactHeight) 76.dp else 96.dp

    AppElevatedCard(
        modifier = modifier.then(DashboardModifiers.announcementCardAdaptive(isCompactHeight)),
        shape = AppShape.XLarge,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .background(announcement.backgroundBrush)
                .then(DashboardModifiers.announcementContentAdaptive(isCompactHeight))
        ) {
            // Decorative subtle paw watermark in the background
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = null,
                tint = AppColors.White.copy(alpha = 0.12f),
                modifier = Modifier
                    .size(pawIconSize)
                    .align(Alignment.BottomEnd)
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = DashboardModifiers.itemSpanFull) {
                    AppSurface(
                        shape = AppShape.Pill,
                        color = AppColors.White.copy(alpha = 0.25f)
                    ) {
                        Text(
                            text = announcement.tag,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.White,
                            modifier = DashboardModifiers.announcementTagPadding(isCompactHeight)
                        )
                    }
                    Spacer(modifier = Modifier.height(if (isCompactHeight) 4.dp else 8.dp))
                    Text(
                        text = announcement.title,
                        style = if (isCompactHeight) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(if (isCompactHeight) 2.dp else 4.dp))
                    Text(
                        text = announcement.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.White.copy(alpha = 0.9f),
                        maxLines = if (isCompactHeight) 1 else 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                AppSurface(
                    shape = AppShape.Pill,
                    color = AppColors.White,
                    shadowElevation = 2.dp
                ) {
                    Text(
                        text = announcement.ctaText,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.Dark.Background,
                        modifier = DashboardModifiers.announcementCtaPadding(isCompactHeight)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 3. Pet Categories Section (Circular pastel icons with label below)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun DashboardCategoriesSection(
    categories: List<PetCategoryItem>,
    onCategoryClick: (categoryName: String) -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp)
) {
    val spacing = if (windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) {
        24.dp
    } else {
        16.dp
    }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.adaptiveCentered(spacing)
    ) {
        items(categories, key = { it.id }) { category ->
            PetCategoryCircularItem(
                category = category,
                onClick = { onCategoryClick(category.name) }
            )
        }
    }
}

@Composable
fun PetCategoryCircularItem(
    category: PetCategoryItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .then(DashboardModifiers.categoryItemColumn)
            .clickable(onClick = onClick)
    ) {
        // Pastel circular icon container (Using AppSurface from commonUI)
        AppSurface(
            shape = CircleShape,
            color = category.backgroundColor,
            shadowElevation = 1.dp,
            modifier = DashboardModifiers.categoryIconSurface
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = category.iconTint,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Category Name below
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 4. Featured Section Header (Using AppTextButton from commonUI)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun FeaturedPetsHeader(
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.dashboard_featured_pets),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        AppTextButton(
            text = stringResource(R.string.dashboard_see_all),
            onClick = onSeeAllClick,
            leadingIcon = Icons.AutoMirrored.Filled.ArrowForward
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 5. Featured Pet Card (Using AppElevatedCard, AppSurface, AppIconButton with tint)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun FeaturedPetCard(
    pet: FeaturedPetItem,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = AppShape.Large,
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Image area with soft gradient & decorative pet silhouette
            Box(
                modifier = DashboardModifiers.featuredCardImage
                    .background(pet.cardGradient)
            ) {
                // Central pet illustration
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = pet.name,
                    tint = AppColors.White.copy(alpha = 0.45f),
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.Center)
                )

                // Age badge using AppSurface from commonUI (bottom-left)
                AppSurface(
                    style = AppSurfaceStyles.translucent(shape = AppShape.Pill, alpha = 0.35f),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = pet.age,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                // Favorite heart button using AppSurface + AppIconButton with tint (top-right)
                AppSurface(
                    style = AppSurfaceStyles.floatingWhite(shape = CircleShape),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                ) {
                    AppIconButton(
                        icon = if (pet.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.cd_favorite),
                        tint = if (pet.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = onFavoriteToggle,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Pet Info details
            Column(
                modifier = DashboardModifiers.featuredCardContent
            ) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = pet.breed,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = pet.distance,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = pet.gender,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Phone",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun DashboardScreenPreview() {
    PetComposeTheme {
        DashboardScreen()
    }
}

@Preview(
    name = "Foldable",
    device = "spec:width=673dp,height=841dp,dpi=420",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun DashboardScreenFoldablePreview() {
    PetComposeTheme {
        DashboardScreen()
    }
}

@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=240",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun DashboardScreenTabletPreview() {
    PetComposeTheme {
        DashboardScreen()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// DashboardModifiers — Centralized layout and styling tokens from AppModifier
// ─────────────────────────────────────────────────────────────────────────────
private typealias DashboardModifiers = AppModifier.Dashboard


