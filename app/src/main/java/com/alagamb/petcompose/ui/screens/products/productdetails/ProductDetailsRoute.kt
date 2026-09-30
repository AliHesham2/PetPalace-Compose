package com.alagamb.petcompose.ui.screens.products.productdetails

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alagamb.petcompose.R
import com.alagamb.petcompose.ui.commonui.buttons.AppButton
import com.alagamb.petcompose.ui.commonui.buttons.AppButtonStyles
import com.alagamb.petcompose.ui.commonui.buttons.AppIconButton
import com.alagamb.petcompose.ui.commonui.cards.AppElevatedCard
import com.alagamb.petcompose.ui.commonui.customize.AppShape
import com.alagamb.petcompose.ui.commonui.customize.AppColors
import com.alagamb.petcompose.ui.commonui.navigation.AppTopBar
import com.alagamb.petcompose.ui.commonui.surface.AppSurface
import com.alagamb.petcompose.ui.commonui.surface.AppSurfaceStyles
import com.alagamb.petcompose.ui.screens.products.productlist.ProductListMockData
import com.alagamb.petcompose.ui.theme.PetComposeTheme

@Composable
fun ProductDetailsRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ProductDetailsSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
                }
                is ProductDetailsSideEffect.NavigateBack -> {
                    onBackClick()
                }
            }
        }
    }

    ProductDetailsScreen(
        state = state,
        onBackClick = onBackClick,
        onToggleFavorite = { viewModel.processIntent(ProductDetailsIntent.OnToggleFavorite) },
        onIncreaseQuantity = { viewModel.processIntent(ProductDetailsIntent.IncreaseQuantity) },
        onDecreaseQuantity = { viewModel.processIntent(ProductDetailsIntent.DecreaseQuantity) },
        onSendRequest = { viewModel.processIntent(ProductDetailsIntent.SendRequest) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    state: ProductDetailsUiState,
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onSendRequest: () -> Unit,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true
) {
    val product = state.product

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = product?.name ?: stringResource(R.string.product_details_title),
                centerTitle = true,
                navigationIcon = if (showBackButton) Icons.AutoMirrored.Filled.ArrowBack else null,
                onNavigationClick = { if (showBackButton) onBackClick() }
            )
        },
        bottomBar = {
            if (product != null) {
                ProductDetailsBottomBar(
                    quantity = state.quantity,
                    totalPrice = state.totalPrice,
                    isSendingRequest = state.isSendingRequest,
                    isRequestSent = state.isRequestSent,
                    onIncrease = onIncreaseQuantity,
                    onDecrease = onDecreaseQuantity,
                    onSendRequest = onSendRequest
                )
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (product == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.product_not_found_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.product_not_found_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    AppButton(
                        text = stringResource(R.string.action_go_back),
                        onClick = onBackClick,
                        style = AppButtonStyles.primary()
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. Hero Artwork / Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(product.cardGradient)
                ) {
                    // Center decorative illustration
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = product.name,
                        tint = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                        modifier = Modifier
                            .size(110.dp)
                            .align(Alignment.Center)
                    )

                    // Tag Badge (top-start)
                    if (product.tag != null) {
                        AppSurface(
                            style = AppSurfaceStyles.translucent(shape = AppShape.Pill, alpha = 0.45f),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = product.tag,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Floating Favorite Heart Button (top-end)
                    AppSurface(
                        style = AppSurfaceStyles.floatingWhite(shape = CircleShape),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(40.dp)
                    ) {
                        AppIconButton(
                            icon = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = stringResource(R.string.cd_favorite),
                            tint = if (state.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                            onClick = onToggleFavorite,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // 2. Product Information Details
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Category pill
                    AppSurface(
                        shape = AppShape.Pill,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = product.category,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    // Title
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Rating and Reviews
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AppColors.Semantic.Warning,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${product.rating}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.verified_reviews_count, product.reviewCount),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Unit Price
                    Text(
                        text = product.price,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Description Section
                    Text(
                        text = stringResource(R.string.section_description),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    // Highlights & Guarantees Row
                    Text(
                        text = stringResource(R.string.section_highlights),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ProductPerkCard(
                            icon = Icons.Default.LocalShipping,
                            title = stringResource(R.string.perk_shipping_title),
                            subtitle = stringResource(R.string.perk_shipping_subtitle),
                            modifier = Modifier.weight(1f)
                        )
                        ProductPerkCard(
                            icon = Icons.Default.Verified,
                            title = stringResource(R.string.perk_safety_title),
                            subtitle = stringResource(R.string.perk_safety_subtitle),
                            modifier = Modifier.weight(1f)
                        )
                        ProductPerkCard(
                            icon = Icons.Default.Autorenew,
                            title = stringResource(R.string.perk_returns_title),
                            subtitle = stringResource(R.string.perk_returns_subtitle),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                }
            }
        }
    }
}

@Composable
private fun ProductPerkCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    AppElevatedCard(
        modifier = modifier,
        shape = AppShape.Medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProductDetailsBottomBar(
    quantity: Int,
    totalPrice: String,
    isSendingRequest: Boolean,
    isRequestSent: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onSendRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppSurface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Quantity Selector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = AppShape.Pill
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                AppIconButton(
                    icon = Icons.Default.Remove,
                    contentDescription = stringResource(R.string.cd_decrease_quantity),
                    onClick = onDecrease,
                    modifier = Modifier.size(36.dp),
                    tint = if (quantity > 1) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )

                Text(
                    text = "$quantity",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                AppIconButton(
                    icon = Icons.Default.Add,
                    contentDescription = stringResource(R.string.cd_increase_quantity),
                    onClick = onIncrease,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Send Request Button
            val buttonText = when {
                isSendingRequest -> stringResource(R.string.sending_request)
                isRequestSent -> stringResource(R.string.request_sent)
                else -> stringResource(R.string.send_request_with_price, totalPrice)
            }
            AppButton(
                text = buttonText,
                onClick = onSendRequest,
                enabled = !isSendingRequest,
                style = AppButtonStyles.primary(),
                leadingIcon = Icons.AutoMirrored.Filled.Send,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// ──  PREVIEWS  ────────────────────────────────────────────────

@Preview(name = "Product Details - With Item", showBackground = true, showSystemUi = true)
@Composable
fun ProductDetailsScreenWithItemPreview() {
    PetComposeTheme {
        ProductDetailsScreen(
            state = ProductDetailsUiState(
                product = ProductListMockData.allProducts.first(),
                quantity = 2,
                isFavorite = true
            ),
            onBackClick = {},
            onToggleFavorite = {},
            onIncreaseQuantity = {},
            onDecreaseQuantity = {},
            onSendRequest = {},
            modifier = Modifier.statusBarsPadding()
        )
    }
}

@Preview(name = "Product Details - Not Found", showBackground = true, showSystemUi = true)
@Composable
fun ProductDetailsScreenNotFoundPreview() {
    PetComposeTheme {
        ProductDetailsScreen(
            state = ProductDetailsUiState(
                product = null
            ),
            onBackClick = {},
            onToggleFavorite = {},
            onIncreaseQuantity = {},
            onDecreaseQuantity = {},
            onSendRequest = {},
            modifier = Modifier.statusBarsPadding()
        )
    }
}
