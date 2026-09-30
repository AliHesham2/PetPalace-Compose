package com.alagamb.petcompose.ui.screens.products.productlist

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.alagamb.petcompose.R
import com.alagamb.petcompose.data.model.product.ProductItem
import com.alagamb.petcompose.ui.commonui.buttons.AppIconButton
import com.alagamb.petcompose.ui.commonui.cards.AppElevatedCard
import com.alagamb.petcompose.ui.commonui.chips.AppFilterChip
import com.alagamb.petcompose.ui.commonui.customize.AppShape
import com.alagamb.petcompose.ui.commonui.customize.AppColors
import com.alagamb.petcompose.ui.commonui.navigation.AppTopBar
import com.alagamb.petcompose.ui.commonui.surface.AppSurface
import com.alagamb.petcompose.ui.commonui.surface.AppSurfaceStyles
import com.alagamb.petcompose.ui.commonui.textfield.AppOutlinedTextField
import com.alagamb.petcompose.ui.commonui.textfield.AppTextFieldStyles
import com.alagamb.petcompose.ui.screens.products.productdetails.ProductDetailsIntent
import com.alagamb.petcompose.ui.screens.products.productdetails.ProductDetailsRoute
import com.alagamb.petcompose.ui.screens.products.productdetails.ProductDetailsScreen
import com.alagamb.petcompose.ui.screens.products.productdetails.ProductDetailsSideEffect
import com.alagamb.petcompose.ui.screens.products.productdetails.ProductDetailsViewModel
import com.alagamb.petcompose.ui.theme.PetComposeTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ProductListRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel = hiltViewModel(),
    detailsViewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val petPagingItems = viewModel.petsPagingFlow.collectAsLazyPagingItems()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val navigator = rememberListDetailPaneScaffoldNavigator<String>()

    BackHandler(enabled = navigator.canNavigateBack()) {
        coroutineScope.launch {
            navigator.navigateBack()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ProductListSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
                }
                is ProductListSideEffect.NavigateToProductDetails -> {
                    coroutineScope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, effect.productId)
                    }
                }
            }
        }
    }

    LaunchedEffect(detailsViewModel) {
        detailsViewModel.sideEffect.collect { effect ->
            when (effect) {
                is ProductDetailsSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
                }
                is ProductDetailsSideEffect.NavigateBack -> {
                    coroutineScope.launch {
                        if (navigator.canNavigateBack()) {
                            navigator.navigateBack()
                        } else {
                            onBackClick()
                        }
                    }
                }
            }
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            ProductListScreen(
                state = state,
                petPagingItems = petPagingItems,
                onBackClick = onBackClick,
                onSelectTag = { viewModel.processIntent(ProductListIntent.OnSelectTag(it)) },
                onToggleFavorite = { id, currentFav ->
                    viewModel.processIntent(ProductListIntent.OnToggleFavorite(id, currentFav))
                },
                onProductClick = { productId ->
                    coroutineScope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, productId)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        },
        detailPane = {
            val selectedProductId = navigator.currentDestination?.contentKey
            if (selectedProductId != null) {
                LaunchedEffect(selectedProductId) {
                    detailsViewModel.processIntent(ProductDetailsIntent.LoadProduct(selectedProductId))
                }
                val detailsState by detailsViewModel.uiState.collectAsStateWithLifecycle()
                val isListHidden = navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Hidden

                ProductDetailsScreen(
                    state = detailsState,
                    onBackClick = {
                        coroutineScope.launch {
                            navigator.navigateBack()
                        }
                    },
                    onToggleFavorite = { detailsViewModel.processIntent(ProductDetailsIntent.OnToggleFavorite) },
                    onIncreaseQuantity = { detailsViewModel.processIntent(ProductDetailsIntent.IncreaseQuantity) },
                    onDecreaseQuantity = { detailsViewModel.processIntent(ProductDetailsIntent.DecreaseQuantity) },
                    onSendRequest = { detailsViewModel.processIntent(ProductDetailsIntent.SendRequest) },
                    showBackButton = isListHidden,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                PetDetailPlaceholder(modifier = Modifier.fillMaxSize())
            }
        },
        modifier = modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    state: ProductListUiState,
    petPagingItems: LazyPagingItems<ProductItem>,
    onBackClick: () -> Unit,
    onSelectTag: (String?) -> Unit,
    onToggleFavorite: (String, Boolean) -> Unit,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val defaultTitle = stringResource(R.string.product_list_default_title)
    val displayTitle = state.title.ifEmpty { defaultTitle }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = displayTitle,
                centerTitle = true,
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search field using AppOutlinedTextField with TextFieldState
            AppOutlinedTextField(
                state = state.searchState,
                placeholder = stringResource(R.string.search_placeholder, displayTitle),
                leadingIcon = Icons.Default.Search,
                trailingContent = if (state.searchState.text.isNotEmpty()) {
                    {
                        AppIconButton(
                            icon = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.cd_clear),
                            onClick = { state.searchState.clearText() }
                        )
                    }
                } else null,
                style = AppTextFieldStyles.outlined(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Filter Chips synced from Room DB
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.filterTags) { tag ->
                    val isAll = tag.equals("All", ignoreCase = true)
                    val isSelected = if (isAll) state.selectedTag == null else state.selectedTag == tag
                    val chipLabel = if (isAll) stringResource(R.string.tag_all) else tag

                    AppFilterChip(
                        label = chipLabel,
                        selected = isSelected,
                        onSelectedChange = {
                            if (isAll) onSelectTag(null) else onSelectTag(tag)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Append states
            val refreshState = petPagingItems.loadState.refresh
            val appendState = petPagingItems.loadState.append

            // Results count / state header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (refreshState !is LoadState.Loading) {
                    Text(
                        text = stringResource(R.string.items_available_count, petPagingItems.itemCount),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            when {
                // Initial Load state
                refreshState is LoadState.Loading && petPagingItems.itemCount == 0 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                // Initial Error state
                refreshState is LoadState.Error && petPagingItems.itemCount == 0 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = refreshState.error.localizedMessage ?: stringResource(R.string.failed_to_load_more),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            TextButton(onClick = { petPagingItems.retry() }) {
                                Text(stringResource(R.string.action_retry))
                            }
                        }
                    }
                }
                // Empty state
                refreshState is LoadState.NotLoading && petPagingItems.itemCount == 0 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.no_products_found_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.no_products_found_subtitle),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                // Content: Adaptive Product Grid using LazyVerticalGrid with Paging 3
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 170.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(
                            count = petPagingItems.itemCount,
                            key = petPagingItems.itemKey { it.id }
                        ) { index ->
                            val product = petPagingItems[index]
                            if (product != null) {
                                ProductGridCard(
                                    product = product,
                                    onFavoriteToggle = { onToggleFavorite(product.id, product.isFavorite) },
                                    onClick = { onProductClick(product.id) }
                                )
                            }
                        }

                        // Appending loading footer
                        if (appendState is LoadState.Loading) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        // Appending error footer with Retry
                        if (appendState is LoadState.Error) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.failed_to_load_more),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    TextButton(onClick = { petPagingItems.retry() }) {
                                        Text(stringResource(R.string.action_retry))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductGridCard(
    product: ProductItem,
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
            // Header Image area with soft gradient & badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(product.cardGradient)
            ) {
                // Decorative icon illustration
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = product.name,
                    tint = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                    modifier = Modifier
                        .size(52.dp)
                        .align(Alignment.Center)
                )

                // Optional Tag badge (top-start)
                if (product.tag != null) {
                    AppSurface(
                        style = AppSurfaceStyles.translucent(shape = AppShape.Pill, alpha = 0.4f),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = product.tag,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                // Favorite heart button (top-end)
                AppSurface(
                    style = AppSurfaceStyles.floatingWhite(shape = CircleShape),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(30.dp)
                ) {
                    AppIconButton(
                        icon = if (product.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.cd_favorite),
                        tint = if (product.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = onFavoriteToggle,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Product Information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = MaterialTheme.typography.bodySmall.lineHeight
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AppColors.Semantic.Warning,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${product.rating}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = stringResource(R.string.review_count_format, product.reviewCount),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price
                Text(
                    text = product.price,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(name = "Product List - With Items", showBackground = true, showSystemUi = true)
@Composable
fun ProductListScreenWithItemsPreview() {
    val items = flowOf(PagingData.from(ProductListMockData.getProductsForTitle("Dogs"))).collectAsLazyPagingItems()
    PetComposeTheme {
        ProductListScreen(
            state = ProductListUiState(title = "Dogs"),
            petPagingItems = items,
            onBackClick = {},
            onSelectTag = {},
            onToggleFavorite = { _, _ -> },
            onProductClick = {},
            modifier = Modifier.statusBarsPadding()
        )
    }
}

@Preview(name = "Product List - Empty State", showBackground = true, showSystemUi = true)
@Composable
fun ProductListScreenEmptyPreview() {
    val items = flowOf(PagingData.from(emptyList<ProductItem>())).collectAsLazyPagingItems()
    PetComposeTheme {
        ProductListScreen(
            state = ProductListUiState(title = "Dogs"),
            petPagingItems = items,
            onBackClick = {},
            onSelectTag = {},
            onToggleFavorite = { _, _ -> },
            onProductClick = {},
            modifier = Modifier.statusBarsPadding()
        )
    }
}

@Composable
private fun PetDetailPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(AppShape.Pill)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(44.dp)
                )
            }
            Text(
                text = stringResource(R.string.select_pet_placeholder_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.select_pet_placeholder_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(
    name = "Product List - Tablet Dual Pane",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Composable
fun ProductListTabletDualPanePreview() {
    val items = flowOf(PagingData.from(ProductListMockData.getProductsForTitle("Dogs"))).collectAsLazyPagingItems()
    val sampleProduct = ProductListMockData.allProducts.first()
    PetComposeTheme {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                ProductListScreen(
                    state = ProductListUiState(title = "Dogs"),
                    petPagingItems = items,
                    onBackClick = {},
                    onSelectTag = {},
                    onToggleFavorite = { _, _ -> },
                    onProductClick = {}
                )
            }
            Box(modifier = Modifier.weight(1.2f)) {
                ProductDetailsScreen(
                    state = com.alagamb.petcompose.ui.screens.products.productdetails.ProductDetailsUiState(
                        product = sampleProduct,
                        isFavorite = sampleProduct.isFavorite
                    ),
                    onBackClick = {},
                    onToggleFavorite = {},
                    onIncreaseQuantity = {},
                    onDecreaseQuantity = {},
                    onSendRequest = {},
                    showBackButton = false
                )
            }
        }
    }
}

@Preview(
    name = "Product List - Foldable Dual Pane",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=673dp,height=841dp,dpi=420"
)
@Composable
fun ProductListFoldableDualPanePreview() {
    val items = flowOf(PagingData.from(ProductListMockData.getProductsForTitle("Dogs"))).collectAsLazyPagingItems()
    val sampleProduct = ProductListMockData.allProducts.first()
    PetComposeTheme {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                ProductListScreen(
                    state = ProductListUiState(title = "Dogs"),
                    petPagingItems = items,
                    onBackClick = {},
                    onSelectTag = {},
                    onToggleFavorite = { _, _ -> },
                    onProductClick = {}
                )
            }
            Box(modifier = Modifier.weight(1.2f)) {
                ProductDetailsScreen(
                    state = com.alagamb.petcompose.ui.screens.products.productdetails.ProductDetailsUiState(
                        product = sampleProduct,
                        isFavorite = sampleProduct.isFavorite
                    ),
                    onBackClick = {},
                    onToggleFavorite = {},
                    onIncreaseQuantity = {},
                    onDecreaseQuantity = {},
                    onSendRequest = {},
                    showBackButton = false
                )
            }
        }
    }
}

