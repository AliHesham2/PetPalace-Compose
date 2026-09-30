package com.alagamb.petcompose.ui.screens.profile

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alagamb.petcompose.R
import com.alagamb.petcompose.data.preferences.ThemeMode
import com.alagamb.petcompose.ui.commonui.cards.AppCard
import com.alagamb.petcompose.ui.commonui.cards.AppElevatedCard
import com.alagamb.petcompose.ui.commonui.customize.AppColors
import com.alagamb.petcompose.ui.commonui.customize.AppShape
import com.alagamb.petcompose.ui.commonui.customize.appBorder
import com.alagamb.petcompose.ui.commonui.customize.appGradientBackground
import com.alagamb.petcompose.ui.commonui.surface.AppSurface
import com.alagamb.petcompose.ui.theme.PetComposeTheme

@Composable
fun ProfileRoute(
    onNavigateToRequests: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    ProfileScreen(
        state = state,
        notificationsEnabled = notificationsEnabled,
        onNotificationsToggle = { notificationsEnabled = it },
        onThemeModeChange = { viewModel.setThemeMode(it) },
        onLanguageChange = { viewModel.setLanguage(it) },
        onNavigateToRequests = onNavigateToRequests,
        onLogoutClick = { showLogoutDialog = true },
        windowAdaptiveInfo = windowAdaptiveInfo,
        modifier = modifier
    )

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.profile_logout_confirm_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.profile_logout_confirm_msg),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.action_confirm),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(
                        text = stringResource(R.string.action_cancel),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            shape = AppShape.Large,
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        )
    }
}

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    notificationsEnabled: Boolean,
    onNotificationsToggle: (Boolean) -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
    onLanguageChange: (String) -> Unit,
    onNavigateToRequests: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val rootHorizontalPadding = when {
        windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> 96.dp
        windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> 48.dp
        else -> 16.dp
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── 1. Hero Profile Header Card ───────────────────────────────────────
            item {
                ProfileHeroCard(
                    username = state.username,
                    email = state.email,
                    requestsCount = state.requestsCount,
                    favoritesCount = state.favoritesCount
                )
            }

            // ── 2. Appearance & Theme Section ─────────────────────────────────────
            item {
                ProfileSectionCard(
                    sectionIcon = Icons.Default.Palette,
                    sectionTitle = stringResource(R.string.profile_section_appearance)
                ) {
                    Text(
                        text = stringResource(R.string.profile_theme_mode),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ThemeModeSegmentedSelector(
                        selectedMode = state.themeMode,
                        onModeSelected = onThemeModeChange
                    )
                }
            }

            // ── 3. Language Selector Section ───────────────────────────────────────
            item {
                ProfileSectionCard(
                    sectionIcon = Icons.Default.Language,
                    sectionTitle = stringResource(R.string.profile_section_language)
                ) {
                    LanguageSelectorRow(
                        selectedLanguage = state.selectedLanguage,
                        onLanguageSelected = onLanguageChange
                    )
                }
            }

            // ── 4. My Activity & Requests Section ─────────────────────────────────
            item {
                ProfileSectionCard(
                    sectionIcon = Icons.Default.Widgets,
                    sectionTitle = stringResource(R.string.profile_section_activity)
                ) {
                    ProfileSettingRow(
                        icon = Icons.AutoMirrored.Filled.Assignment,
                        title = stringResource(R.string.profile_action_my_requests),
                        subtitle = stringResource(R.string.profile_action_my_requests_desc),
                        badgeCount = state.requestsCount,
                        onClick = onNavigateToRequests
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
                    )

                    ProfileSettingRow(
                        icon = Icons.Default.Favorite,
                        title = stringResource(R.string.profile_action_saved_pets),
                        subtitle = stringResource(R.string.profile_action_saved_pets_desc),
                        badgeCount = state.favoritesCount,
                        onClick = {}
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
                    )

                    ProfileSwitchRow(
                        icon = Icons.Default.Notifications,
                        title = stringResource(R.string.profile_action_notifications),
                        subtitle = stringResource(R.string.profile_action_notifications_desc),
                        checked = notificationsEnabled,
                        onCheckedChange = onNotificationsToggle
                    )
                }
            }

            // ── 5. Account & Security Section ──────────────────────────────────────
            item {
                ProfileSectionCard(
                    sectionIcon = Icons.Default.Security,
                    sectionTitle = stringResource(R.string.profile_section_account)
                ) {
                    ProfileSettingRow(
                        icon = Icons.Default.Info,
                        title = stringResource(R.string.profile_action_about),
                        subtitle = null,
                        onClick = {}
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
                    )

                    ProfileSettingRow(
                        icon = Icons.Default.Security,
                        title = stringResource(R.string.profile_action_privacy),
                        subtitle = null,
                        onClick = {}
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
                    )

                    ProfileSettingRow(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        title = stringResource(R.string.profile_action_logout),
                        subtitle = null,
                        isDestructive = true,
                        onClick = onLogoutClick
                    )
                }
            }

            // ── 6. App Version & Copyright Footer ──────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.profile_version),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Profile Hero Header Component
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ProfileHeroCard(
    username: String,
    email: String,
    requestsCount: Int,
    favoritesCount: Int,
    modifier: Modifier = Modifier
) {
    AppElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = AppShape.XLarge
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar with camera badge
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .appGradientBackground(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = username,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(46.dp)
                    )
                }

                // Edit avatar badge
                AppSurface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shadowElevation = 2.dp,
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = stringResource(R.string.profile_edit_avatar),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // User Name
            Text(
                text = username,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // User Email
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Verified Member Badge Chip
            AppSurface(
                shape = AppShape.Pill,
                color = AppColors.Semantic.SuccessSurface,
                border = BorderStroke(1.dp, AppColors.Semantic.Success.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = AppColors.Semantic.Success,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.profile_badge_member),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.Semantic.Success
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3-Column Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AppShape.Medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileStatColumn(
                    value = "$requestsCount",
                    label = stringResource(R.string.profile_stat_requests)
                )

                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .width(1.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                )

                ProfileStatColumn(
                    value = "$favoritesCount",
                    label = stringResource(R.string.profile_stat_favorites)
                )

                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .width(1.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                )

                ProfileStatColumn(
                    value = "150",
                    label = stringResource(R.string.profile_stat_points)
                )
            }
        }
    }
}

@Composable
private fun ProfileStatColumn(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Theme Mode Segmented Selector (System / Light / Dark)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ThemeModeSegmentedSelector(
    selectedMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppShape.Pill)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ThemeModeTab(
            label = stringResource(R.string.profile_theme_system),
            icon = Icons.Default.BrightnessAuto,
            isSelected = selectedMode == ThemeMode.SYSTEM,
            onClick = { onModeSelected(ThemeMode.SYSTEM) },
            modifier = Modifier.weight(1f)
        )

        ThemeModeTab(
            label = stringResource(R.string.profile_theme_light),
            icon = Icons.Default.LightMode,
            isSelected = selectedMode == ThemeMode.LIGHT,
            onClick = { onModeSelected(ThemeMode.LIGHT) },
            modifier = Modifier.weight(1f)
        )

        ThemeModeTab(
            label = stringResource(R.string.profile_theme_dark),
            icon = Icons.Default.DarkMode,
            isSelected = selectedMode == ThemeMode.DARK,
            onClick = { onModeSelected(ThemeMode.DARK) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ThemeModeTab(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else AppColors.Transparent,
        label = "tabContainerColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "tabContentColor"
    )

    AppSurface(
        onClick = onClick,
        shape = AppShape.Pill,
        color = containerColor,
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        modifier = modifier.height(40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = contentColor
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Language Selector Row (English / Arabic)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun LanguageSelectorRow(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LanguageOptionCard(
            title = stringResource(R.string.profile_lang_en),
            subtext = "English",
            flag = "🇺🇸",
            isSelected = selectedLanguage.startsWith("en", ignoreCase = true),
            onClick = { onLanguageSelected("en") },
            modifier = Modifier.weight(1f)
        )

        LanguageOptionCard(
            title = stringResource(R.string.profile_lang_ar),
            subtext = "العربية",
            flag = "🇸🇦",
            isSelected = selectedLanguage.startsWith("ar", ignoreCase = true),
            onClick = { onLanguageSelected("ar") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LanguageOptionCard(
    title: String,
    subtext: String,
    flag: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
        label = "langBorderColor"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f) else MaterialTheme.colorScheme.surface,
        label = "langContainerColor"
    )

    AppSurface(
        onClick = onClick,
        shape = AppShape.Medium,
        color = containerColor,
        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor),
        modifier = modifier.height(68.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = flag,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtext,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Reusable Section Card Layout
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ProfileSectionCard(
    sectionIcon: ImageVector,
    sectionTitle: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AppElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = AppShape.Large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = sectionIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            content()
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Profile Action Row with Icon, Title, Optional Badge, and Chevron
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ProfileSettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    badgeCount: Int? = null,
    isDestructive: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
    val iconColor = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppSurface(
            shape = AppShape.Medium,
            color = if (isDestructive) {
                AppColors.Semantic.ErrorSurface
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            },
            modifier = Modifier.size(38.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = primaryColor
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (badgeCount != null && badgeCount > 0) {
            AppSurface(
                shape = AppShape.Pill,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = "$badgeCount",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(14.dp)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Profile Switch Row for Toggles (e.g. Notifications)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ProfileSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppSurface(
            shape = AppShape.Medium,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            modifier = Modifier.size(38.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true,     device = "spec:width=411dp,height=1500dp,dpi=420")
@Composable
fun ProfileScreenPreview() {
    PetComposeTheme {
        ProfileScreen(
            state = ProfileUiState(
                username = "Aly Mohamed",
                email = "aly@petcompose.app",
                requestsCount = 3,
                favoritesCount = 8,
                themeMode = ThemeMode.SYSTEM,
                selectedLanguage = "en"
            ),
            notificationsEnabled = true,
            onNotificationsToggle = {},
            onThemeModeChange = {},
            onLanguageChange = {},
            onNavigateToRequests = {},
            onLogoutClick = {}
        )
    }
}

@Preview(
    name = "Foldable",
    device = "spec:width=673dp,height=841dp,dpi=420",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileScreenFoldablePreview() {
    PetComposeTheme {
        ProfileScreen(
            state = ProfileUiState(
                username = "Aly Mohamed",
                email = "aly@petcompose.app",
                requestsCount = 3,
                favoritesCount = 8,
                themeMode = ThemeMode.SYSTEM,
                selectedLanguage = "en"
            ),
            notificationsEnabled = true,
            onNotificationsToggle = {},
            onThemeModeChange = {},
            onLanguageChange = {},
            onNavigateToRequests = {},
            onLogoutClick = {}
        )
    }
}

@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=240",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileScreenTabletPreview() {
    PetComposeTheme {
        ProfileScreen(
            state = ProfileUiState(
                username = "Aly Mohamed",
                email = "aly@petcompose.app",
                requestsCount = 3,
                favoritesCount = 8,
                themeMode = ThemeMode.SYSTEM,
                selectedLanguage = "en"
            ),
            notificationsEnabled = true,
            onNotificationsToggle = {},
            onThemeModeChange = {},
            onLanguageChange = {},
            onNavigateToRequests = {},
            onLogoutClick = {}
        )
    }
}

