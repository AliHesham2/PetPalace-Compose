@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.alagamb.petcompose.ui.screens.addpet

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alagamb.petcompose.R
import com.alagamb.petcompose.data.model.dashboard.PetCategoryItem
import com.alagamb.petcompose.ui.commonui.buttons.AppMorphingButton
import com.alagamb.petcompose.ui.commonui.cards.AppCard
import com.alagamb.petcompose.ui.commonui.chips.AppFilterChip
import com.alagamb.petcompose.ui.commonui.customize.AppColors
import com.alagamb.petcompose.ui.commonui.customize.AppModifier
import com.alagamb.petcompose.ui.commonui.customize.AppShape
import com.alagamb.petcompose.ui.commonui.navigation.AppTopBar
import com.alagamb.petcompose.ui.commonui.textfield.AppOutlinedTextField
import com.alagamb.petcompose.ui.theme.PetComposeTheme
import com.alagamb.petcompose.util.LocalSnackbarHostState

private val PRESET_TAGS = listOf("Best Match", "Playful", "Puppy", "Rescue", "Friendly", "Vaccinated")

@Composable
fun AddPetRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddPetViewModel = hiltViewModel(),
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is AddPetSideEffect.NavigateBack -> onBackClick()
                is AddPetSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
                }
                is AddPetSideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message.asString(context))
                }
            }
        }
    }

    AddPetScreen(
        state = state,
        onBackClick = onBackClick,
        onIntent = viewModel::processIntent,
        windowAdaptiveInfo = windowAdaptiveInfo,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    state: AddPetUiState,
    onBackClick: () -> Unit,
    onIntent: (AddPetIntent) -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val context = LocalContext.current
    val formScrollState = rememberScrollState()
    val previewScrollState = rememberScrollState()
    val isWideScreen = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )

    Scaffold(
        modifier = modifier.then(AddPetModifiers.root),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.add_pet_title),
                centerTitle = true,
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isWideScreen) {
                // Dual-Pane / Supporting Pane layout for Tablets and Foldables
                Row(
                    modifier = AddPetModifiers.dualPaneContainer,
                    horizontalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    // Left Column: Scrollable Form
                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .then(AddPetModifiers.dualPaneFormColumn(formScrollState)),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        AddPetFormFields(
                            state = state,
                            onIntent = onIntent,
                            context = context
                        )
                    }

                    // Right Column: Scrollable Live Preview Card (Supporting Pane)
                    Column(
                        modifier = Modifier
                            .weight(0.9f)
                            .then(AddPetModifiers.dualPanePreviewColumn(previewScrollState)),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SectionHeader(
                            title = stringResource(R.string.add_pet_preview_title),
                            icon = Icons.Default.Pets
                        )
                        AddPetLivePreviewCard(state = state)
                    }
                }
            } else {
                // Single-column layout for Phones
                Column(
                    modifier = AddPetModifiers.singlePaneContent(formScrollState),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    AddPetLivePreviewCard(state = state)
                    AddPetFormFields(
                        state = state,
                        onIntent = onIntent,
                        context = context
                    )
                }
            }
        }
    }
}

@Composable
private fun AddPetFormFields(
    state: AddPetUiState,
    onIntent: (AddPetIntent) -> Unit,
    context: android.content.Context
) {
    // ── Category Selector Section ─────────────────────────────────
    SectionHeader(
        title = stringResource(R.string.add_pet_category_label),
        icon = Icons.Default.Pets
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(state.categories, key = { it.id }) { category ->
            val isSelected = state.selectedCategory?.id == category.id
            CategoryChoiceChip(
                category = category,
                isSelected = isSelected,
                onClick = { onIntent(AddPetIntent.SelectCategory(category)) }
            )
        }
    }

    // ── Basic Information Section ─────────────────────────────────
    SectionHeader(
        title = stringResource(R.string.add_pet_section_basic),
        icon = Icons.Default.Description
    )

    AppOutlinedTextField(
        state = state.nameState,
        label = stringResource(R.string.add_pet_name_label),
        placeholder = stringResource(R.string.add_pet_name_hint),
        leadingIcon = Icons.Default.Pets,
        isError = state.nameError != null,
        errorMessage = state.nameError?.asString(context),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth()
    )

    AppOutlinedTextField(
        state = state.breedState,
        label = stringResource(R.string.add_pet_breed_label),
        placeholder = stringResource(R.string.add_pet_breed_hint),
        leadingIcon = Icons.Default.Pets,
        isError = state.breedError != null,
        errorMessage = state.breedError?.asString(context),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth()
    )

    AppOutlinedTextField(
        state = state.ageState,
        label = stringResource(R.string.add_pet_age_label),
        placeholder = stringResource(R.string.add_pet_age_hint),
        leadingIcon = Icons.Default.Cake,
        isError = state.ageError != null,
        errorMessage = state.ageError?.asString(context),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth()
    )

    // Gender Selection
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.add_pet_gender_label),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GenderOptionChip(
                label = stringResource(R.string.add_pet_gender_male),
                icon = Icons.Default.Male,
                isSelected = state.selectedGender == "Male",
                onClick = { onIntent(AddPetIntent.SelectGender("Male")) },
                modifier = Modifier.weight(1f)
            )
            GenderOptionChip(
                label = stringResource(R.string.add_pet_gender_female),
                icon = Icons.Default.Female,
                isSelected = state.selectedGender == "Female",
                onClick = { onIntent(AddPetIntent.SelectGender("Female")) },
                modifier = Modifier.weight(1f)
            )
        }
    }

    // ── Adoption Details Section ──────────────────────────────────
    SectionHeader(
        title = stringResource(R.string.add_pet_section_adoption),
        icon = Icons.Default.AttachMoney
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppOutlinedTextField(
            state = state.priceState,
            label = stringResource(R.string.add_pet_price_label),
            placeholder = stringResource(R.string.add_pet_price_hint),
            leadingIcon = Icons.Default.AttachMoney,
            isError = state.priceError != null,
            errorMessage = state.priceError?.asString(context),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.weight(1f)
        )

        AppOutlinedTextField(
            state = state.distanceState,
            label = stringResource(R.string.add_pet_distance_label),
            placeholder = stringResource(R.string.add_pet_distance_hint),
            leadingIcon = Icons.Default.LocationOn,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.weight(1f)
        )
    }

    // Tag / Highlight picker
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = stringResource(R.string.add_pet_tag_label),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(PRESET_TAGS) { tag ->
                AppFilterChip(
                    label = tag,
                    selected = state.selectedTag == tag,
                    onSelectedChange = { onIntent(AddPetIntent.SelectTag(tag)) }
                )
            }
        }
    }

    // Feature on Dashboard toggle card
    FeaturedSwitchCard(
        isFeatured = state.isFeatured,
        onCheckedChange = { onIntent(AddPetIntent.ToggleFeatured(it)) }
    )

    // ── Description Section ───────────────────────────────────────
    SectionHeader(
        title = stringResource(R.string.add_pet_section_about),
        icon = Icons.Default.Description
    )

    AppOutlinedTextField(
        state = state.descriptionState,
        label = stringResource(R.string.add_pet_description_label),
        placeholder = stringResource(R.string.add_pet_description_hint),
        singleLine = false,
        isError = state.descriptionError != null,
        errorMessage = state.descriptionError?.asString(context),
        supportingText = "${state.descriptionState.text.length} chars (min 10)",
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))

    // ── Submit Button ─────────────────────────────────────────────
    AppMorphingButton(
        text = stringResource(R.string.add_pet_submit_button),
        isLoading = state.isLoading,
        onClick = { onIntent(AddPetIntent.SubmitPet) },
        leadingIcon = Icons.Default.CheckCircle,
        modifier = AddPetModifiers.submitButton
    )

    Spacer(modifier = Modifier.height(16.dp))
}

// ─────────────────────────────────────────────────────────────────────────────
// Components & Helpers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(
    title: String,
    icon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun CategoryChoiceChip(
    category: PetCategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "chip_border"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        label = "chip_bg"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "chip_content"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .clip(AppShape.Pill)
            .background(backgroundColor)
            .border(1.5.dp, borderColor, AppShape.Pill)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = category.icon,
            contentDescription = category.name,
            tint = category.iconTint,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = contentColor
        )
    }
}

@Composable
private fun GenderOptionChip(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        },
        label = "gender_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
        },
        label = "gender_border"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "gender_content"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(50.dp)
            .clip(AppShape.Medium)
            .background(backgroundColor)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = borderColor,
                shape = AppShape.Medium
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = contentColor,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FeaturedSwitchCard(
    isFeatured: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    AppCard(
        shape = AppShape.Medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = stringResource(R.string.add_pet_featured_label),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.add_pet_featured_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Switch(
                checked = isFeatured,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
private fun AddPetLivePreviewCard(state: AddPetUiState) {
    val name = state.nameState.text.toString().trim().ifBlank { "Pet Name" }
    val breed = state.breedState.text.toString().trim().ifBlank { "Breed" }
    val age = state.ageState.text.toString().trim().ifBlank { "Age" }
    val priceInput = state.priceState.text.toString().trim()
    val price = if (priceInput.isNotBlank()) {
        if (priceInput.startsWith("$")) priceInput else "$$priceInput"
    } else "$100"
    val distance = state.distanceState.text.toString().trim().ifBlank { "1.5 km" }
    val categoryName = state.selectedCategory?.name ?: "Category"

    val (startColor, endColor) = when (state.selectedCategory?.id?.lowercase()) {
        "dogs" -> Pair(0xFFFFE0B2, 0xFFFFCC80)
        "cats" -> Pair(0xFFFFCDD2, 0xFFEF9A9A)
        "birds" -> Pair(0xFFC8E6C9, 0xFFA5D6A7)
        "fish" -> Pair(0xFFB3E5FC, 0xFF81D4FA)
        else -> Pair(0xFFE1BEE7, 0xFFCE93D8)
    }

    Card(
        shape = AppShape.Large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = AddPetModifiers.previewCard
    ) {
        Column {
            // Gradient banner with pet icon and tag badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(startColor), Color(endColor))
                        )
                    )
                    .padding(12.dp)
            ) {
                // Category + Tag badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .clip(AppShape.Pill)
                            .background(Color.White.copy(alpha = 0.9f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = categoryName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(AppShape.Pill)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = state.selectedTag,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                // Centered pet icon
                Icon(
                    imageVector = state.selectedCategory?.icon ?: Icons.Default.Pets,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Center)
                )

                // Price badge bottom-end
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clip(AppShape.Pill)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = price,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Info rows
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "$age • ${state.selectedGender}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = breed,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = distance,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Add Pet Screen - Phone",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=1600dp,dpi=420"
)
@Composable
private fun AddPetScreenPreview() {
    PetComposeTheme {
        AddPetScreen(
            state = AddPetUiState(
                categories = listOf(
                    PetCategoryItem("dogs", "Dogs", Icons.Default.Pets, Color(0xFFFFE8D6), Color(0xFF8D4F27)),
                    PetCategoryItem("cats", "Cats", Icons.Default.Pets, Color(0xFFFFDFC4), Color(0xFFD4691E))
                ),
                selectedCategory = PetCategoryItem("dogs", "Dogs", Icons.Default.Pets, Color(0xFFFFE8D6), Color(0xFF8D4F27)),
                selectedGender = "Female"
            ),
            onBackClick = {},
            onIntent = {}
        )
    }
}

@Preview(
    name = "Add Pet Screen - Tablet Supporting Pane",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Composable
private fun AddPetTabletPreview() {
    PetComposeTheme {
        AddPetScreen(
            state = AddPetUiState(
                categories = listOf(
                    PetCategoryItem("dogs", "Dogs", Icons.Default.Pets, Color(0xFFFFE8D6), Color(0xFF8D4F27)),
                    PetCategoryItem("cats", "Cats", Icons.Default.Pets, Color(0xFFFFDFC4), Color(0xFFD4691E))
                ),
                selectedCategory = PetCategoryItem("dogs", "Dogs", Icons.Default.Pets, Color(0xFFFFE8D6), Color(0xFF8D4F27)),
                selectedGender = "Female"
            ),
            onBackClick = {},
            onIntent = {}
        )
    }
}

@Preview(
    name = "Add Pet Screen - Foldable Supporting Pane",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=673dp,height=841dp,dpi=420"
)
@Composable
private fun AddPetFoldablePreview() {
    PetComposeTheme {
        AddPetScreen(
            state = AddPetUiState(
                categories = listOf(
                    PetCategoryItem("dogs", "Dogs", Icons.Default.Pets, Color(0xFFFFE8D6), Color(0xFF8D4F27)),
                    PetCategoryItem("cats", "Cats", Icons.Default.Pets, Color(0xFFFFDFC4), Color(0xFFD4691E))
                ),
                selectedCategory = PetCategoryItem("dogs", "Dogs", Icons.Default.Pets, Color(0xFFFFE8D6), Color(0xFF8D4F27)),
                selectedGender = "Female"
            ),
            onBackClick = {},
            onIntent = {}
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AddPetModifiers — Centralized layout and styling tokens from AppModifier
// ─────────────────────────────────────────────────────────────────────────────
private typealias AddPetModifiers = AppModifier.AddPet


