package com.alagamb.petcompose.ui.screens.requests

import android.widget.Toast
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Pets
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alagamb.petcompose.R
import com.alagamb.petcompose.data.model.request.PetRequestItem
import com.alagamb.petcompose.ui.commonui.buttons.AppIconButton
import com.alagamb.petcompose.ui.commonui.cards.AppElevatedCard
import com.alagamb.petcompose.ui.commonui.customize.AppColors
import com.alagamb.petcompose.ui.commonui.customize.AppModifier
import com.alagamb.petcompose.ui.commonui.customize.AppShape
import com.alagamb.petcompose.ui.commonui.navigation.AppTopBar
import com.alagamb.petcompose.ui.commonui.surface.AppSurface
import com.alagamb.petcompose.ui.commonui.surface.AppSurfaceStyles
import com.alagamb.petcompose.ui.theme.PetComposeTheme

@Composable
fun RequestsRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RequestsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is RequestsSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    RequestsScreen(
        state = state,
        onBackClick = onBackClick,
        onDeleteRequest = { viewModel.processIntent(RequestsIntent.DeleteRequest(it)) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen(
    state: RequestsUiState,
    onBackClick: () -> Unit,
    onDeleteRequest: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val isTablet = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )
    val isWideScreen = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )

    Scaffold(
        modifier = modifier.then(RequestsModifiers.root),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.requests_title),
                centerTitle = true,
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = onBackClick
            )
        }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            state.requests.isEmpty() -> {
                Box(
                    modifier = RequestsModifiers.emptyContainer
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = RequestsModifiers.emptyCard,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Assignment,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.no_requests_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.no_requests_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.TopCenter
                ) {
                    LazyVerticalGrid(
                        columns = if (isWideScreen) GridCells.Fixed(2) else GridCells.Fixed(1),
                        modifier = RequestsModifiers.contentContainer(
                            if (isTablet) 860.dp else if (isWideScreen) 680.dp else 500.dp
                        ),
                        contentPadding = PaddingValues(
                            horizontal = if (isWideScreen) 24.dp else 16.dp,
                            vertical = 16.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Text(
                                text = stringResource(R.string.active_requests_count, state.requests.size),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        items(
                            items = state.requests,
                            key = { it.id }
                        ) { request ->
                            RequestCard(
                                request = request,
                                onDelete = { onDeleteRequest(request.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RequestCard(
    request: PetRequestItem,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppElevatedCard(
        modifier = modifier.then(RequestsModifiers.requestCard),
        shape = AppShape.Large
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Card Header Banner
            Box(
                modifier = RequestsModifiers.cardHeader
                    .background(request.cardGradient)
                    .padding(12.dp)
            ) {
                // Background Pet Silhouette Icon
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.CenterStart)
                )

                // Category pill on start
                AppSurface(
                    style = AppSurfaceStyles.translucent(shape = AppShape.Pill, alpha = 0.4f),
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Text(
                        text = request.petCategory,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }

                // Status pill on end
                val statusText = if (request.status.equals("Pending", ignoreCase = true)) {
                    stringResource(R.string.status_pending)
                } else {
                    stringResource(R.string.status_approved)
                }

                AppSurface(
                    style = AppSurfaceStyles.floatingWhite(shape = AppShape.Pill),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (request.status.equals("Pending", ignoreCase = true)) {
                                        AppColors.Semantic.Warning
                                    } else {
                                        AppColors.Semantic.Success
                                    },
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Card Body
            Column(
                modifier = RequestsModifiers.cardContent
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = request.petName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = request.petCategory + (request.tag?.let { " • $it" } ?: ""),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Delete / Cancel Button
                    AppIconButton(
                        icon = Icons.Default.DeleteOutline,
                        contentDescription = stringResource(R.string.cd_cancel_request),
                        tint = MaterialTheme.colorScheme.error,
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(10.dp))

                // Quantity & Price Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.request_quantity_format, request.quantity),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = request.formattedDate,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                    Text(
                        text = request.totalPrice,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RequestsScreenPreview() {
    PetComposeTheme {
        RequestsScreen(
            state = RequestsUiState(
                requests = listOf(
                    PetRequestItem(
                        id = "1",
                        petId = "pet1",
                        petName = "Golden Retriever Puppy",
                        petCategory = "Dogs",
                        petPrice = "$120.00",
                        petDescription = "Friendly pup",
                        tag = "Popular",
                        quantity = 1,
                        totalPrice = "$120.00",
                        status = "Pending",
                        createdAt = System.currentTimeMillis()
                    )
                ),
                isLoading = false
            ),
            onBackClick = {}
        )
    }
}

@Preview(name = "Foldable", device = "spec:width=673dp,height=841dp,dpi=420", showBackground = true)
@Composable
fun RequestsScreenFoldablePreview() {
    PetComposeTheme {
        RequestsScreen(
            state = RequestsUiState(
                requests = listOf(
                    PetRequestItem(
                        id = "1",
                        petId = "pet1",
                        petName = "Golden Retriever Puppy",
                        petCategory = "Dogs",
                        petPrice = "$120.00",
                        petDescription = "Friendly pup",
                        tag = "Popular",
                        quantity = 1,
                        totalPrice = "$120.00",
                        status = "Pending",
                        createdAt = System.currentTimeMillis()
                    ),
                    PetRequestItem(
                        id = "2",
                        petId = "pet2",
                        petName = "Siamese Cat",
                        petCategory = "Cats",
                        petPrice = "$90.00",
                        petDescription = "Playful kitten",
                        tag = "New",
                        quantity = 2,
                        totalPrice = "$180.00",
                        status = "Approved",
                        createdAt = System.currentTimeMillis()
                    )
                ),
                isLoading = false
            ),
            onBackClick = {}
        )
    }
}

@Preview(name = "Tablet", device = "spec:width=1280dp,height=800dp,dpi=240", showBackground = true)
@Composable
fun RequestsScreenTabletPreview() {
    PetComposeTheme {
        RequestsScreen(
            state = RequestsUiState(
                requests = listOf(
                    PetRequestItem(
                        id = "1",
                        petId = "pet1",
                        petName = "Golden Retriever Puppy",
                        petCategory = "Dogs",
                        petPrice = "$120.00",
                        petDescription = "Friendly pup",
                        tag = "Popular",
                        quantity = 1,
                        totalPrice = "$120.00",
                        status = "Pending",
                        createdAt = System.currentTimeMillis()
                    ),
                    PetRequestItem(
                        id = "2",
                        petId = "pet2",
                        petName = "Siamese Cat",
                        petCategory = "Cats",
                        petPrice = "$90.00",
                        petDescription = "Playful kitten",
                        tag = "New",
                        quantity = 2,
                        totalPrice = "$180.00",
                        status = "Approved",
                        createdAt = System.currentTimeMillis()
                    )
                ),
                isLoading = false
            ),
            onBackClick = {}
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// RequestsModifiers — Centralized layout and styling tokens from AppModifier
// ─────────────────────────────────────────────────────────────────────────────
private typealias RequestsModifiers = AppModifier.Requests

