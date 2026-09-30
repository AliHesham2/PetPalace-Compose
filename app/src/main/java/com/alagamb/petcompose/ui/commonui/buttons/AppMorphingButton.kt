package com.alagamb.petcompose.ui.commonui.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alagamb.petcompose.ui.commonui.customize.AppShape
import com.alagamb.petcompose.util.rememberDebounceClick

// ─────────────────────────────────────────────────────────────────────────────
// AppMorphingButton — Smooth Collapsing Morphing Loading Button
// ─────────────────────────────────────────────────────────────────────────────
//
// When [isLoading] is false:
//   Renders as a standard full-width (or custom width) button with [text]
//   and optional [leadingIcon].
//
// When [isLoading] is true:
//   Fluidly animates its width down to [collapsedSize] (a circular pill shape)
//   and morphs into a centered progress spinner.
//
// When loading concludes (success or error):
//   Smoothly springs back to its original expanded dimensions.
//
// FEATURES:
//   • Fully integrated with [AppButtonStyle] & [AppButtonStyles] tokens
//   • Built-in debounce ([debounceTimeMs]) to block rapid multi-clicks
//   • Physics-based spring animation
//   • Custom progress indicator support
//   • Works with any container (fillMaxWidth, fixed width, or wrap content)
//
// USAGE EXAMPLES:
//
//   // 1. Standard usage with ViewModel loading state & style token
//   AppMorphingButton(
//       text = "Sign Up",
//       isLoading = state.isLoading,
//       onClick = { viewModel.onSignUp() },
//       style = AppButtonStyles.primary(),
//       modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp)
//   )
//
//   // 2. Custom danger style and icon
//   AppMorphingButton(
//       text = "Delete Account",
//       isLoading = isDeleting,
//       onClick = { onDelete() },
//       leadingIcon = Icons.Default.Delete,
//       style = AppButtonStyles.danger()
//   )
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun AppMorphingButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    debounceTimeMs: Long = 600L,
    leadingIcon: ImageVector? = null,
    style: AppButtonStyle = AppButtonStyles.primary(),
    height: Dp = 50.dp,
    collapsedSize: Dp = height,
    progressStrokeWidth: Dp = 2.5.dp,
    progressColor: Color? = null,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(),
    animationSpec: AnimationSpec<Dp> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ),
    progressContent: @Composable (() -> Unit)? = null
) {
    val debouncedClick = if (debounceTimeMs > 0L) {
        rememberDebounceClick(debounceTimeMs = debounceTimeMs, onClick = onClick)
    } else {
        onClick
    }

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val fullWidth = maxWidth
        val targetWidth = if (isLoading) collapsedSize else fullWidth
        val animatedWidth by animateDpAsState(
            targetValue = targetWidth,
            animationSpec = animationSpec,
            label = "AppMorphingButtonWidth"
        )

        val shape = style.shape
        val colors = style.colors
        val border = style.border

        Button(
            onClick = debouncedClick,
            enabled = enabled && !isLoading,
            shape = shape,
            colors = colors,
            border = border,
            elevation = elevation,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .width(animatedWidth)
                .height(height)
        ) {
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    fadeIn(animationSpec = tween(150)) togetherWith fadeOut(animationSpec = tween(150))
                },
                label = "AppMorphingButtonContent"
            ) { loading ->
                if (loading) {
                    if (progressContent != null) {
                        progressContent()
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(height * 0.5f),
                            color = progressColor ?: LocalContentColor.current,
                            strokeWidth = progressStrokeWidth
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (leadingIcon != null) {
                            Icon(
                                imageVector = leadingIcon,
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                        }
                        Text(
                            text = text,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}
