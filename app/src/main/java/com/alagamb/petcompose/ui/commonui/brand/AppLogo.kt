package com.alagamb.petcompose.ui.commonui.brand

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alagamb.petcompose.R
import com.alagamb.petcompose.ui.commonui.customize.AppColors

/**
 * Reusable Petify Brand App Logo Component
 *
 * Renders the official Petify paw & heart logo with ambient elevation.
 */
@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
    shape: Shape = CircleShape,
    elevation: Dp = 6.dp
) {
    Surface(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = AppColors.Brand.Purple.copy(alpha = 0.35f),
                spotColor = AppColors.Brand.Teal.copy(alpha = 0.35f)
            ),
        shape = shape,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(shape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_pet_logo),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.size(size)
            )
        }
    }
}
