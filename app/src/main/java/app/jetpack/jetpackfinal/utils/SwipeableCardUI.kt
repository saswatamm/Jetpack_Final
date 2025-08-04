package app.jetpack.jetpackfinal.utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.jetpack.jetpackfinal.ui.home.data.model.SpotlightData
import app.jetpack.jetpackfinal.ui.home.ui.screen.CardContent
import app.jetpack.jetpackfinal.ui.home.ui.viewmodel.HomeViewModel
import app.jetpack.jetpackfinal.ui.theme.backgroundLightColor
import app.jetpack.jetpackfinal.utils.Constants.TOP_CARD_INDEX
import app.jetpack.jetpackfinal.utils.Constants.TOP_Z_INDEX
import app.jetpack.jetpackfinal.utils.Constants.paddingOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.collections.getOrNull
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.unaryMinus

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun SwipeableCard(
    spotlightList: List<SpotlightData>,
    onCardRemoved: (SpotlightData) -> Unit,
    viewModel: HomeViewModel
) {
    val spotlightListData by viewModel.spotlightData1.collectAsState()
    val scope = rememberCoroutineScope()

    Box(Modifier.fillMaxWidth()) {
        repeat(min(3, spotlightListData.size)) { index ->
            val card = spotlightListData.getOrNull(index) ?: return@repeat
            val cardModifier = makeCardModifier(
                scope = rememberCoroutineScope(),
                cardIndex = index,
                scale = calculateScale(index),
                zIndex = TOP_Z_INDEX - index,
                offsetY = calculateOffset(index),
                offset = remember {
                    Animatable(Offset(0f, 0f), Offset.VectorConverter)
                },
                rearrangeForward = {
                    scope.launch {
                        viewModel.removeCard(card.staticData.id)
                    }
                },
                rearrangeBackward = {
                    scope.launch {
                        viewModel.removeCard(card.staticData.id)
                    }                },
                animationSpec = tween(
                    durationMillis = 150,
                    easing = LinearEasing
                ),
                card = card,
                viewModel = viewModel
            )

            CardContent(
                card = card,
                modifier = cardModifier.background(
                    color = backgroundLightColor,
                    shape = RoundedCornerShape(2.dp)
                ).shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(2.dp),
                    clip = true
                ),
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("ModifierFactoryExtensionFunction")
fun makeCardModifier(
    scope: CoroutineScope,
    cardIndex: Int,
    scale: Float,
    zIndex: Float,
    offset: Animatable<Offset, AnimationVector2D>,
    animationSpec: FiniteAnimationSpec<Offset>,
    offsetY: Int,
    rearrangeForward: () -> Unit,
    rearrangeBackward: () -> Unit,
    card : SpotlightData,
    viewModel: HomeViewModel
): Modifier {
    return if (cardIndex > TOP_CARD_INDEX) Modifier
        .graphicsLayer {
            translationY =
                if (offset.value.y != 0f) min(
                    abs(offset.value.y),
                    paddingOffset * 1.1f
                ) else 0f
            scaleX = if (offset.value.y != 0f) {
                min(scale + (abs(offset.value.y) / 1000), 1.06f - (cardIndex * 0.03f))
            } else scale
            scaleY = if (offset.value.y != 0f) {
                min(scale + (abs(offset.value.y) / 1000), 1.06f - (cardIndex * 0.03f))
            } else scale
        }
        .scale(scale)
        .offset { IntOffset(0, offsetY) }
        .zIndex(zIndex)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))

    else Modifier
        .scale(scale)
        .offset { IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt()) }
        .zIndex(zIndex)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    scope.launch {
                        Log.e("Index","$cardIndex, ${card.staticData.name}")
                        Log.e("Card", "Card : $card")
                        viewModel.removeCard(viewModel.spotlightData1.value[cardIndex].staticData.id)
                        //rearrangeForward.invoke()
                        offset.animateTo(
                            targetValue = Offset(-600f, 600f),
                            animationSpec = snap()
                        )
                        offset.animateTo(
                            targetValue = Offset(0f, 0f),
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearEasing
                            )
                        )
                    }
                }
            )
        }
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                val dragOffset = Offset(
                    offset.value.x + change.positionChange().x, // Only update x
                    offset.value.y  // Keep original y value
                )
                scope.launch {
                    offset.snapTo(
                        Offset(dragOffset.x, offset.value.y)
                    )
                    change.consumePositionChange()

                    val x = when {
                        offset.value.x > 150 -> size.width.toFloat()
                        offset.value.x < -150 -> -size.width.toFloat()
                        else -> 0f
                    }

                    offset.animateTo(
                        targetValue = Offset(x, 0f),
                        animationSpec = animationSpec
                    )

                    if (abs(offset.value.x) == size.width.toFloat()) {
                        viewModel.removeCard(viewModel.spotlightData1.value[cardIndex].staticData.id)
                        offset.animateTo(
                            targetValue = Offset(0f, 0f),
                            animationSpec = snap()
                        )
                    }
                }
            }
        }
}

private fun calculateScale(idx: Int): Float {
    return when (idx) {
        1 -> 0.97f
        2 -> 0.94f
        else -> 1f
    }
}

private fun calculateOffset(idx: Int): Int {
    return when (idx) {
        1 -> -(paddingOffset * idx * 0.8).toInt()
        2 -> -(paddingOffset * idx * 0.8).toInt()
        else -> -paddingOffset.toInt()
    }
}