package app.jetpack.jetpackfinal.utils

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import app.jetpack.jetpackfinal.ui.theme.green
import app.jetpack.jetpackfinal.ui.theme.lightOrange

// Data class to hold highlighted values
data class HighlightedValue(
    val text: String
)

@Composable
fun AnimatedPerformanceChart(
    modifier: Modifier = Modifier,
    list: List<Float>,
    animationDuration: Int = 1000
) {
    if (list.size < 2) return

    Row(modifier = modifier) {
        val max = list.maxOrNull() ?: return
        val min = list.minOrNull() ?: return
        val zipList = list.zipWithNext()
        val lineColor = if (list.last() > list.first()) green else lightOrange
        val gradientColor = if (list.last() > list.first())
            green.copy(alpha = 0.2f) else lightOrange.copy(alpha = 0.2f)

        // Single progress for the entire chart
        val progress = remember { Animatable(0f) }

        LaunchedEffect(list) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = animationDuration,
                    easing = FastOutSlowInEasing
                )
            )
        }

        // Store all points to create the gradient path
        val points = mutableListOf<Offset>()

        zipList.forEachIndexed { index, pair ->
            val fromValuePercentage = getValuePercentageForRange(pair.first, max, min)
            val toValuePercentage = getValuePercentageForRange(pair.second, max, min)

            val segmentStart = index.toFloat() / (zipList.size - 1)
            val segmentEnd = (index + 1).toFloat() / (zipList.size - 1)

            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                onDraw = {
                    val currentProgress = progress.value

                    if (currentProgress > segmentStart) {
                        val segmentProgress = ((currentProgress - segmentStart) / (segmentEnd - segmentStart))
                            .coerceIn(0f, 1f)

                        val startY = size.height * (1 - fromValuePercentage)
                        val endY = size.height * (1 - toValuePercentage)

                        val currentEndX = size.width * segmentProgress
                        val currentEndY = lerp(startY, endY, segmentProgress)

                        // Create path for gradient
                        val path = Path()
                        path.moveTo(0f, size.height)  // Start from bottom-left
                        path.lineTo(0f, startY)       // Line to start point
                        path.lineTo(currentEndX, currentEndY)  // Line to end point
                        path.lineTo(currentEndX, size.height)  // Line to bottom
                        path.close()

                        // Draw gradient fill
                        drawPath(
                            path = path,
                            brush = Brush.verticalGradient(
                                colors = listOf(gradientColor, Color.Transparent),
                                startY = 0f,
                                endY = size.height
                            )
                        )

                        // Draw line on top of gradient
                        drawLine(
                            color = lineColor,
                            start = Offset(0f, startY),
                            end = Offset(currentEndX, currentEndY),
                            strokeWidth = 5f
                        )
                    }
                }
            )
        }
    }
}

// Helper function for linear interpolation
private fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + (end - start) * fraction
}

fun vibrateDevice(context: Context) {
    val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(50,128))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(100)
    }
}