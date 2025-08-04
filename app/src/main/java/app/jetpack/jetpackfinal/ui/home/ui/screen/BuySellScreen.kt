package app.jetpack.jetpackfinal.ui.home.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import app.jetpack.jetpackfinal.R
import app.jetpack.jetpackfinal.ui.home.data.model.SpotlightData
import app.jetpack.jetpackfinal.ui.home.ui.viewmodel.CoinDetailViewModel
import app.jetpack.jetpackfinal.ui.theme.backgroundColor
import app.jetpack.jetpackfinal.ui.theme.darkishGray
import app.jetpack.jetpackfinal.ui.theme.green
import app.jetpack.jetpackfinal.ui.theme.spaceFont
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BuySellScreen(navController: NavController, viewModel: CoinDetailViewModel, coinId: String){
    val coinDetails by viewModel.getCoinDetails(coinId).collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(top = 20.dp)
    ){
        var bottomBarState by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            coinDetails?.let { CoinHeader(coinData = it) }
            TransactionBox()
            Spacer(modifier = Modifier.height(24.dp))
            TransactionEntry()
        }


    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CoinHeader(coinData: SpotlightData){
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Back button aligned to the start (left)
        Icon(
            painter = painterResource(R.drawable.back_icon),
            contentDescription = "Back Icon",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp) // Optional padding for better spacing
        )

        // Centered Row for coin logo and text
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = coinData.staticData.image.large,
                contentDescription = "Crypto Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp)) // Spacing between logo and text
            Text(
                text = "${coinData.staticData.name}",
                fontSize = 16.sp,
                fontFamily = spaceFont,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD9D9D9)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TransactionBox() {
    var transactionAmount by remember { mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
            .padding(top = 30.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1F20)
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .background(
                        color = darkishGray,
                        shape = RoundedCornerShape(32.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF1E1F20),
                            shape = CircleShape
                        )
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.dollar_icon),
                        contentDescription = "Dollar Sign",
                        tint = Color(0xFFD9D9D9),
                        modifier = Modifier.size(14.dp)
                    )
                }

                Text(
                    text = "CASH:$23.09K",
                    fontFamily = spaceFont,
                    fontSize = 13.sp,
                    color = Color(0xFFD9D9D9),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                val scrollState = rememberScrollState()
                BasicTextField(
                    value = transactionAmount,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                            transactionAmount = newValue
                        }
                    },
                    modifier = Modifier
                        .then(
                            if (transactionAmount.isNotEmpty()) {
                                Modifier.horizontalScroll(scrollState)
                            } else {
                                Modifier
                            }
                        ),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            if (transactionAmount.isEmpty()) {
                                Text(
                                    text = "$23,000",
                                    fontFamily = spaceFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 64.sp,
                                    color = darkishGray
                                )
                            }
                            innerTextField()
                        }
                    },
                    textStyle = TextStyle(
                        fontFamily = spaceFont,
                        fontSize = 64.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TransactionEntry(){
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier
                    .background(
                        color = Color(0xFF363739),
                        shape = RoundedCornerShape(24.dp)
                    ).weight(1f)
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "10%",
                    fontFamily = spaceFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
            Row(
                modifier = Modifier
                    .background(
                        color = Color(0xFF363739),
                        shape = RoundedCornerShape(24.dp)
                    ).weight(1f)
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "20%",
                    fontFamily = spaceFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp

                )
            }
            Row(
                modifier = Modifier
                    .background(
                        color = Color(0xFF363739),
                        shape = RoundedCornerShape(24.dp)
                    ).weight(1f)
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "30%",
                    fontFamily = spaceFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp

                )
            }
            Row(
                modifier = Modifier
                    .background(
                        color = Color(0xFF363739),
                        shape = RoundedCornerShape(24.dp)
                    ).weight(1f)
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "MAX",
                    fontFamily = spaceFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp

                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.height(24.dp))

        // First row (1, 2, 3)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = "1",
                fontFamily = spaceFont,
                fontSize = 42.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { }
            )
            Text(
                text = "2",
                fontFamily = spaceFont,
                fontSize = 42.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { }
            )
            Text(
                text = "3",
                fontFamily = spaceFont,
                fontSize = 42.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Second row (4, 5, 6)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = "4",
                fontFamily = spaceFont,
                fontSize = 42.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { }
            )
            Text(
                text = "5",
                fontFamily = spaceFont,
                fontSize = 42.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { }
            )
            Text(
                text = "6",
                fontFamily = spaceFont,
                fontSize = 42.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Third row (7, 8, 9)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = "7",
                fontFamily = spaceFont,
                fontSize = 42.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { }
            )
            Text(
                text = "8",
                fontFamily = spaceFont,
                fontSize = 42.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { }
            )
            Text(
                text = "9",
                fontFamily = spaceFont,
                fontSize = 42.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = ".",
                fontFamily = spaceFont,
                fontSize = 42.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { }
            )
            Text(
                text = "0",
                fontFamily = spaceFont,
                fontSize = 42.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { }
            )
            Icon(
                painter = painterResource(R.drawable.erase_digit_icon),
                contentDescription = "",
                modifier = Modifier.size(width = 27.dp, height = 18.dp),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(42.dp))
        Text(
            text = "Fee: ~$0.40",
            fontFamily = spaceFont,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        SwipeToPayButton(
            modifier = Modifier
                .fillMaxWidth(),
            onSwipeComplete = {
                // Handle payment or purchase completion
            }
        )

    }

}

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeToPayButton(
    modifier: Modifier = Modifier,
    onSwipeComplete: () -> Unit
) {
    val width = remember { mutableStateOf(0f) }
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val density = LocalDensity.current
    var isCompleted by remember { mutableStateOf(false) }

    val anchors = remember(width.value) {
        mapOf(
            0f to 0,
            width.value - with(density) { 72.dp.toPx() } to 1
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)  // Fixed height to prevent resizing
            .clip(RoundedCornerShape(30.dp))
            .background(if (isCompleted) green else Color(0xFF363739))
            .onGloballyPositioned {
                width.value = it.size.width.toFloat()
            }
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.7f) },
                orientation = Orientation.Horizontal
            ),
        contentAlignment = Alignment.Center  // Always center aligned
    ) {
        // Success indicator (only visible when completed)
        AnimatedVisibility(
            visible = isCompleted,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Swipe content (only visible when not completed)
        AnimatedVisibility(
            visible = !isCompleted,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(10.dp)
                        .width(30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left circular swipe button
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                            .height(60.dp)
                            .width(60.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .padding(horizontal = 0.dp)
                            .clip(CircleShape)
                            .background(green),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "»",
                            color = Color.Black,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Right side content
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Swipe to buy",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = spaceFont,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        LaunchedEffect(swipeableState.currentValue) {
            if (swipeableState.currentValue == 1) {
                delay(200)
                isCompleted = true
                delay(100)
                onSwipeComplete()
            }
        }

        LaunchedEffect(swipeableState.offset.value) {
            if (!swipeableState.isAnimationRunning && swipeableState.currentValue == 0 && !isCompleted) {
                if (swipeableState.offset.value > 0 &&
                    swipeableState.offset.value < width.value - with(density) { 72.dp.toPx() }
                ) {
                    swipeableState.animateTo(0)
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
fun TransactionPreview(){
    SwipeToPayButton(
        modifier = Modifier
            .fillMaxWidth(),
        onSwipeComplete = {
            // Handle payment or purchase completion
        }
    )
}

