package com.cristianwer.pepinillorick.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cristianwer.pepinillorick.ui.theme.PortalGreenLight
import kotlin.math.sin
import kotlin.random.Random

internal data class HeartParticle(
    val id: Int,
    val xFraction: Float,
    val size: Dp,
    val swayPx: Float,
    val speedFactor: Float,
    val rotation: Float,
    val tint: Color
)

@Composable
internal fun FloatingHeartsOverlay(
    triggerKey: Int,
    modifier: Modifier = Modifier,
    particleCount: Int = 24,
    primaryColor: Color = MaterialTheme.colorScheme.primary
) {
    if (triggerKey == 0) return

    val density = LocalDensity.current
    val progress = remember(triggerKey) { Animatable(0f) }
    val particles = remember(triggerKey) {
        val heartColors = listOf(
            primaryColor,
            Color(0xFFFF4081),
            Color(0xFFFF80AB),
            Color(0xFFE91E63),
            PortalGreenLight
        )
        List(particleCount) { id ->
            HeartParticle(
                id = id,
                xFraction = Random.nextFloat() * 0.86f + 0.07f,
                size = Random.nextInt(18, 40).dp,
                swayPx = Random.nextFloat() * 90f - 45f,
                speedFactor = Random.nextFloat() * 0.4f + 0.8f,
                rotation = Random.nextFloat() * 40f - 20f,
                tint = heartColors.random()
            )
        }
    }

    LaunchedEffect(triggerKey) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2200, easing = LinearOutSlowInEasing)
        )
    }

    val currentProgress = progress.value
    if (currentProgress > 0f && currentProgress < 1f) {
        BoxWithConstraints(modifier = modifier) {
            val heightPx = with(density) { maxHeight.toPx() }
            val widthPx = with(density) { maxWidth.toPx() }

            particles.forEach { particle ->
                val p = (currentProgress * particle.speedFactor).coerceIn(0f, 1f)
                val yPx = heightPx * (0.85f - p * 0.95f)
                val xPx = widthPx * particle.xFraction + sin(p * 3.14159f * 3f) * particle.swayPx
                val alpha = if (p < 0.15f) (p / 0.15f).coerceIn(0f, 1f)
                            else if (p > 0.65f) (1f - (p - 0.65f) / 0.35f).coerceIn(0f, 1f)
                            else 1f
                val scale = if (p < 0.2f) (p / 0.2f) else (1f - (p - 0.2f) * 0.15f)

                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = particle.tint,
                    modifier = Modifier
                        .size(particle.size)
                        .graphicsLayer {
                            translationX = xPx
                            translationY = yPx
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                            rotationZ = particle.rotation + (p * 35f)
                        }
                )
            }
        }
    }
}

@Composable
internal fun LocalFloatingHeartsBurst(
    triggerKey: Int,
    modifier: Modifier = Modifier,
    particleCount: Int = 5,
    primaryColor: Color = MaterialTheme.colorScheme.primary
) {
    if (triggerKey == 0) return

    val progress = remember(triggerKey) { Animatable(0f) }
    val particles = remember(triggerKey) {
        val heartColors = listOf(
            primaryColor,
            Color(0xFFFF4081),
            Color(0xFFFF80AB),
            Color(0xFFE91E63),
            PortalGreenLight
        )
        List(particleCount) { id ->
            val centerOffset = (id - (particleCount - 1) / 2f)
            HeartParticle(
                id = id,
                xFraction = 0.5f,
                size = Random.nextInt(14, 24).dp,
                swayPx = centerOffset * 24f + (Random.nextFloat() * 12f - 6f),
                speedFactor = Random.nextFloat() * 0.3f + 0.85f,
                rotation = Random.nextFloat() * 30f - 15f,
                tint = heartColors.random()
            )
        }
    }

    LaunchedEffect(triggerKey) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1300, easing = LinearOutSlowInEasing)
        )
    }

    val currentProgress = progress.value
    if (currentProgress > 0f && currentProgress < 1f) {
        Box(modifier = modifier) {
            particles.forEach { particle ->
                val p = (currentProgress * particle.speedFactor).coerceIn(0f, 1f)
                val yPx = -p * 140f
                val xPx = sin(p * 3.14159f * 2f) * 12f + particle.swayPx * p
                val alpha = if (p < 0.15f) (p / 0.15f).coerceIn(0f, 1f)
                            else if (p > 0.6f) (1f - (p - 0.6f) / 0.4f).coerceIn(0f, 1f)
                            else 1f
                val scale = if (p < 0.25f) (p / 0.25f) else (1f - (p - 0.25f) * 0.2f)

                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = particle.tint,
                    modifier = Modifier
                        .size(particle.size)
                        .graphicsLayer {
                            translationX = xPx
                            translationY = yPx
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                            rotationZ = particle.rotation + (p * 25f)
                        }
                )
            }
        }
    }
}
