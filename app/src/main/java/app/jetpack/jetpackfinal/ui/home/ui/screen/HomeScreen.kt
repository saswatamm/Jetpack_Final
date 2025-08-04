package app.jetpack.jetpackfinal.ui.home.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import app.jetpack.jetpackfinal.R
import app.jetpack.jetpackfinal.ui.home.data.model.CryptoCard
import app.jetpack.jetpackfinal.ui.home.data.model.RecommendationData
import app.jetpack.jetpackfinal.ui.home.data.model.SpotlightData
import app.jetpack.jetpackfinal.ui.home.ui.viewmodel.HomeViewModel
import app.jetpack.jetpackfinal.ui.home.ui.viewmodel.RecommendationUiState
import app.jetpack.jetpackfinal.ui.home.ui.viewmodel.SpotlightUiState
import app.jetpack.jetpackfinal.ui.theme.backgroundColor
import app.jetpack.jetpackfinal.ui.theme.backgroundLightColor
import app.jetpack.jetpackfinal.ui.theme.darkGray
import app.jetpack.jetpackfinal.ui.theme.darkishGray
import app.jetpack.jetpackfinal.ui.theme.darkishOrange
import app.jetpack.jetpackfinal.ui.theme.green
import app.jetpack.jetpackfinal.ui.theme.greyishText
import app.jetpack.jetpackfinal.ui.theme.lightBlack
import app.jetpack.jetpackfinal.ui.theme.lightOrange
import app.jetpack.jetpackfinal.ui.theme.spaceFont
import app.jetpack.jetpackfinal.utils.AnimatedPerformanceChart
import app.jetpack.jetpackfinal.utils.BottomNavigationBar
import app.jetpack.jetpackfinal.utils.DestinationScreen
import app.jetpack.jetpackfinal.utils.NavigateTo
import app.jetpack.jetpackfinal.utils.SwipeableCard
import app.jetpack.jetpackfinal.utils.formatNumber
import app.jetpack.jetpackfinal.utils.formatToFourDecimalPlaces
import app.jetpack.jetpackfinal.utils.formatToTwoDecimalPlaces
import coil3.compose.AsyncImage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    var currentRoute by remember { mutableStateOf("home") }
    var bottomBarState by remember { mutableStateOf(true) }

    var searchQuery by remember { mutableStateOf("") }

    val spotlightState by viewModel.spotlightData.collectAsState()
    val recommendationState by viewModel.recommendationData.collectAsState()



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp), // Space for bottom nav
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Search Bar
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Portfolio Card
            item {
                PortfolioCard(
                    amount = 12.00,
                    percentageChange = -14.0,
                    onCardClick = { /* Handle click */ }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Profile Section
            item {
                HomeProfile()
                Spacer(modifier = Modifier.height(40.dp))
            }

            // Card Stack
            item {
                when (spotlightState) {
                    is SpotlightUiState.Success -> {
                        val spotlightData =
                            (spotlightState as SpotlightUiState.Success).data.spotlightData
                        Spacer(modifier = Modifier.height(20.dp))
                        SwipeableCard(
                            spotlightList = spotlightData,
                            onCardRemoved = { removedCard ->
                                //viewModel.removeCard(removedCard.staticData.id)
                            },
                            viewModel = viewModel
                        )
//                        SwipeableCardStack(
//                            cards = spotlightData,
//                            onCardRemoved = { removedCard ->
//                                viewModel.removeCard(removedCard.staticData.id)
//                            }
//                        )
                    }

                    is SpotlightUiState.Loading -> {
                        var cards = emptyList<SpotlightData>()
                        CircularProgressIndicator()
//                        SwipeableCardStack(
//                            cards = cards,
//                            onCardRemoved = { removedCard ->
//                                cards = cards.filter { it.staticData.id != removedCard.staticData.id }
//                            }
//                        )
                    }

                    is SpotlightUiState.Error -> {
                        var cards = emptyList<SpotlightData>()
                        Text("Error ${(spotlightState as SpotlightUiState.Error).message}")
//                        SwipeableCardStack(
//                            cards = cards,
//                            onCardRemoved = { removedCard ->
//                                cards = cards.filter { it.staticData.id != removedCard.staticData.id }
//                            }
//                        )
                    }

                    SpotlightUiState.Initial -> {
                        var cards = emptyList<SpotlightData>()
                        SwipeableCardStack(
                            cards = cards,
                            onCardRemoved = { removedCard ->
                                cards =
                                    cards.filter { it.staticData.id != removedCard.staticData.id }
                            },

                            )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Divider()
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Portfolio Section
            item {
                HomePortfolio()
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                when (recommendationState) {
                    is RecommendationUiState.Error -> {
                        Text(
                            "Error ${(recommendationState as RecommendationUiState.Error).message}",
                            color = Color.White
                        )
                    }

                    RecommendationUiState.Initial -> {

                    }

                    RecommendationUiState.Loading -> {

                        CircularProgressIndicator(color = darkishOrange)

                    }

                    is RecommendationUiState.Success -> {
                        val recommendationData =
                            (recommendationState as RecommendationUiState.Success).data.recommendationData

                        Column {
                            recommendationData.forEachIndexed { index, recommendation ->
                                TopTokenItem(index,
                                    recommendation,
                                    onClick = {
                                        NavigateTo(navController, DestinationScreen.CoinDetail.createRoute(recommendation.staticData.id!!))
                                    })
                                if (index < recommendationData.size - 1) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }


            // Bottom spacing
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }

        // Bottom Navigation with FAB
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavigationBar(
                navController = navController,
                bottomBarState = bottomBarState
            )

            FloatingActionButton(
                onClick = { /* Handle click */ },
                containerColor = darkishOrange,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-34).dp)
                    .shadow(2.dp, CircleShape),
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(R.drawable.chat_filled),
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundLightColor,
                shape = RoundedCornerShape(32.dp)
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .padding(start = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = spaceFont,
                fontWeight = FontWeight.Normal
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search any task",
                            color = Color(0xFF8E8E93),
                            fontSize = 16.sp,
                            fontFamily = spaceFont,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        Box(
            modifier = Modifier
                .background(shape = CircleShape, color = darkGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun HomeProfile() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Text(
                text = "HEY SAYUDH",
                fontFamily = spaceFont,
                fontSize = 14.sp,
                color = greyishText
            )
        }

        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "Here's your",
                fontFamily = spaceFont,
                fontSize = 24.sp,
                color = Color.White
            )
            Text(
                text = "spotlight",
                fontFamily = spaceFont,
                fontSize = 24.sp,
                color = darkishOrange,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.padding(start = 14.dp)
            )
        }

        Text(
            text = "for the day.",
            fontFamily = spaceFont,
            fontSize = 24.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(26.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = darkishGray,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(horizontal = 5.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = darkishOrange,
                        shape = CircleShape
                    )
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.chat_unfulfilled),
                    contentDescription = "Chat Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "list the most talked about coins on X",
                fontFamily = spaceFont,
                fontSize = 13.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun HomePortfolio() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "Explore",
                fontFamily = spaceFont,
                fontSize = 24.sp,
                color = Color.White
            )
            Text(
                text = "all tokens",
                fontFamily = spaceFont,
                fontSize = 24.sp,
                color = darkishOrange,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.padding(start = 14.dp)
            )
        }

        Text(
            text = "on jetpack",
            fontFamily = spaceFont,
            fontSize = 24.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(26.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = darkishGray,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(horizontal = 5.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFFF5722),
                        shape = CircleShape
                    )
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.chat_unfulfilled),
                    contentDescription = "Chat Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "filter out the top gainers of the day",
                fontFamily = spaceFont,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TopTokenItem(index: Int, card: RecommendationData,  onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = lightBlack, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 16.dp, horizontal = 12.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row() {
            AsyncImage(
                model = card.staticData.image.large,
                contentDescription = "Crypto Icon",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(start = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = card.staticData.name,
                    fontFamily = spaceFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                Text(
                    text = "$${formatNumber(card.marketData.marketCap)} Mkt. Cap",
                    fontSize = 11.sp,
                    fontFamily = spaceFont,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

        }
        Row {
            Text(
                text = if (card.marketData.priceChangePercentage24hInCurrency > 0) "+${
                    formatToTwoDecimalPlaces(
                        card.marketData.priceChangePercentage24hInCurrency
                    )
                }%" else "${formatToTwoDecimalPlaces(card.marketData.priceChangePercentage24hInCurrency)}%",
                fontFamily = spaceFont,
                fontWeight = FontWeight.Bold,
                color = if (card.marketData.priceChangePercentage24hInCurrency > 0) green else lightOrange,
                fontSize = 16.sp
            )


        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun PortfolioCard(
    amount: Double,
    percentageChange: Double,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1C1C1E)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "PORTFOLIO",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = spaceFont,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = amount.formatAsCurrency(),
                    fontSize = 32.sp,
                    color = Color.White,
                    fontFamily = spaceFont,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = if (percentageChange < 0)
                            Icons.Default.KeyboardArrowDown
                        else
                            Icons.Default.KeyboardArrowUp,
                        contentDescription = if (percentageChange < 0)
                            "Decrease"
                        else
                            "Increase",
                        tint = lightOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${abs(percentageChange)}%",
                        color = lightOrange,
                        fontSize = 16.sp,
                        fontFamily = spaceFont,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            CircularButton(
                onClick = onCardClick,
                icon = R.drawable.arrow_outward,
                backgroundColor = darkishOrange,
                contentDescription = "View Details"
            )
        }
    }
}

@Composable
private fun CircularButton(
    onClick: () -> Unit,
    icon: Int,
    backgroundColor: Color,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .background(color = backgroundColor, shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun Divider(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(backgroundLightColor)
    )
}

val sampleCryptoCards = listOf(
    CryptoCard(
        id = 1,
        name = "Unicorn Fart Dust",
        marketCap = "178M",
        price = "0.009289",
        change = 24f,
        iconUrl = "https://placeholder.com/48"
    ),
    CryptoCard(
        id = 2,
        name = "Magic Moon Dust",
        marketCap = "156M",
        price = "0.008123",
        change = 12f,
        iconUrl = "https://placeholder.com/48"
    ),
    CryptoCard(
        id = 3,
        name = "Star Shine Token",
        marketCap = "203M",
        price = "0.012445",
        change = -8f,
        iconUrl = "https://placeholder.com/48"
    ),
    CryptoCard(
        id = 4,
        name = "Rainbow Crystal",
        marketCap = "178M",
        price = "0.009289",
        change = 24f,
        iconUrl = "https://placeholder.com/48"
    ),
    CryptoCard(
        id = 5,
        name = "Cosmic Dust",
        marketCap = "178M",
        price = "0.009289",
        change = 24f,
        iconUrl = "https://placeholder.com/48"
    )
)


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun SwipeableCardStack(
    cards: List<SpotlightData>,
    onCardRemoved: (SpotlightData) -> Unit,
    modifier: Modifier = Modifier
) {
    val visibleCards = cards.take(3)
    val scope = rememberCoroutineScope()
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    val context = LocalContext.current

    Box(modifier.fillMaxWidth()) {
        // Background cards first
        visibleCards.drop(1).forEachIndexed { index, card ->
            val scale = calculateScale(index + 1)
            val offsetY = calculateOffset(index + 1)

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationY = -offsetY * context.resources.displayMetrics.density
                    }
                    .shadow(elevation = (8 - index * 2).dp, shape = RoundedCornerShape(24.dp))
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFF1C1C1E), RoundedCornerShape(24.dp))
                    .zIndex(2f + index)
            ) {
                CardContent(card = card, modifier = Modifier.alpha(0.3f))
            }
        }

        // Top card
        if (visibleCards.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(offset.value.x.roundToInt(), 0) }
                    .shadow(12.dp, RoundedCornerShape(24.dp))
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFF1C1C1E), RoundedCornerShape(24.dp))
                    .zIndex(4f)
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            scope.launch {
                                offset.snapTo(Offset(offset.value.x + delta, 0f))
                            }
                        },
                        onDragStopped = { velocity ->
                            scope.launch {
                                if (abs(offset.value.x) > 200) {
                                    val direction = if (offset.value.x > 0) 1 else -1
                                    offset.animateTo(
                                        Offset(direction * 2000f, 0f),
                                        tween(500, easing = LinearOutSlowInEasing)
                                    )
                                    onCardRemoved(visibleCards[0])
                                    offset.snapTo(Offset.Zero)
                                } else {
                                    offset.animateTo(
                                        Offset.Zero,
                                        spring(stiffness = Spring.StiffnessLow)
                                    )
                                }
                            }
                        }
                    )
            ) {
                CardContent(card = visibleCards[0], modifier = Modifier)
            }
        }
    }
}

//private fun calculateOffset(index: Int) = when(index) {
//    1 -> 16
//    2 -> 32
//    else -> 0
//}

private fun calculateScale(index: Int) = when (index) {
    1 -> 0.96f
    2 -> 0.92f
    else -> 1f
}

private fun calculateOffset(index: Int) = when (index) {
    1 -> 16
    2 -> 32
    else -> 0
}


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CardContent(
    key: String? = null,
    card: SpotlightData, modifier: Modifier
) {
    Log.e("Card Values", "${card.marketData}")
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)


    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(backgroundLightColor),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = card.staticData.image.large,
                    contentDescription = "Crypto Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Text(
                        text = card.staticData.name,
                        fontSize = 14.sp,
                        fontFamily = spaceFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "$${formatNumber(card.marketData.marketCap)} Mkt. Cap",
                        fontSize = 11.sp,
                        fontFamily = spaceFont,
                        color = Color(0xFF8C8C8C)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Chart placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Transparent)
        ) {
            val priceData = card.graphs.oneMonth.getPriceValues()
            val chartColor = if (priceData.isNotEmpty() && priceData.last() > priceData.first())
                green.copy(alpha = 0.2f) else lightOrange.copy(alpha = 0.2f)

            Box(
                modifier = Modifier
                    .fillMaxSize()
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colors = listOf(chartColor, Color.Transparent),
//                            startY = 0f,
//                            endY = Float.POSITIVE_INFINITY
//                        )
//                    )
            )
            Log.e("Price Data", "$priceData")
            if (priceData.isNotEmpty()) {
                AnimatedPerformanceChart(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    list = priceData
                )
            } else {
                Text(
                    text = "No price data available",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Footer
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "$${formatToFourDecimalPlaces(card.marketData.currentPrice)}",
                    fontSize = 20.sp,
                    fontFamily = spaceFont,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFD9D9D9)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = if (card.marketData.priceChangePercentage24hInCurrency >= 0) R.drawable.growth_icon else R.drawable.decline_icon,
                        contentDescription = "Growth",
                        modifier = Modifier.size(8.dp),
                    )

                    Text(
                        text = "${formatToTwoDecimalPlaces(card.marketData.priceChangePercentage24hInCurrency)}%",
                        fontSize = 12.sp,
                        fontFamily = spaceFont,
                        fontWeight = FontWeight.Bold,
                        color = if (card.marketData.priceChangePercentage24hInCurrency >= 0) green else lightOrange,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

            }

            Row(
                modifier = Modifier
                    .background(
                        color = darkishGray,
                        shape = RoundedCornerShape(32.dp)
                    )
                    .padding(horizontal = 5.dp, vertical = 6.dp)
                    .wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = darkishOrange,
                            shape = CircleShape
                        )
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.chat_unfulfilled),
                        contentDescription = "Chat Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "spotlight story",
                    fontFamily = spaceFont,
                    fontSize = 13.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

private fun Modifier.swipeToRemove(
    isTopCard: Boolean,
    offsetX: Animatable<Float, AnimationVector1D>,
    rotation: Animatable<Float, AnimationVector1D>,
    onMoveToBack: () -> Unit
) = composed {
    if (!isTopCard) return@composed this

    pointerInput(Unit) {
        val decay = splineBasedDecay<Float>(this)
        coroutineScope {
            while (true) {
                val velocityTracker = VelocityTracker()

                awaitPointerEventScope {
                    val pointerId = awaitFirstDown().id

                    horizontalDrag(pointerId) { change ->
                        // Check if horizontal movement is greater than vertical
                        val horizontalChange = change.positionChange().x
                        val verticalChange = change.positionChange().y

                        if (abs(verticalChange) < abs(horizontalChange)) {
                            val horizontalDragOffset = offsetX.value + horizontalChange
                            launch {
                                offsetX.snapTo(horizontalDragOffset)
                                rotation.snapTo(horizontalDragOffset * 0.015f)
                            }

                            velocityTracker.addPosition(change.uptimeMillis, change.position)
                            change.consume()
                        }
                    }
                }

                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)

                if (targetOffsetX.absoluteValue <= size.width * 0.6) {
                    // Reset position with quick animation if not swiped far enough
                    launch {
                        offsetX.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(
                                durationMillis = 200,
                                easing = LinearOutSlowInEasing
                            )
                        )
                        rotation.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(
                                durationMillis = 200,
                                easing = LinearOutSlowInEasing
                            )
                        )
                    }
                } else {
                    // Remove card if swiped far enough
                    val direction = if (targetOffsetX > 0) 1 else -1
                    launch {
                        offsetX.animateTo(
                            targetValue = direction * size.width * 1.5f,
                            initialVelocity = velocity,
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearOutSlowInEasing
                            )
                        )
                        onMoveToBack()
                        offsetX.snapTo(0f)
                        rotation.snapTo(0f)
                    }
                }
            }
        }
    }
}


fun Double.formatAsCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(this)
        .replace(".00", ",000") // To match the design's format
}