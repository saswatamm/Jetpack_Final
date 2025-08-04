package app.jetpack.jetpackfinal.ui.home.ui.screen

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.jetpack.jetpackfinal.R
import app.jetpack.jetpackfinal.ui.home.data.model.SpotlightData
import app.jetpack.jetpackfinal.ui.home.ui.viewmodel.CoinDetailViewModel
import app.jetpack.jetpackfinal.ui.theme.backgroundColor
import app.jetpack.jetpackfinal.ui.theme.darkishGray
import app.jetpack.jetpackfinal.ui.theme.darkishOrange
import app.jetpack.jetpackfinal.ui.theme.green
import app.jetpack.jetpackfinal.ui.theme.greyishText
import app.jetpack.jetpackfinal.ui.theme.lightOrange
import app.jetpack.jetpackfinal.ui.theme.spaceFont
import app.jetpack.jetpackfinal.utils.AnimatedPerformanceChart
import app.jetpack.jetpackfinal.utils.BottomNavigationWithFloatingButtons
import app.jetpack.jetpackfinal.utils.BulletPoint
import app.jetpack.jetpackfinal.utils.formatNumber
import app.jetpack.jetpackfinal.utils.formatToFourDecimalPlaces
import app.jetpack.jetpackfinal.utils.vibrateDevice
import coil3.compose.AsyncImage

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CoinDetailScreen(navController: NavController, viewModel: CoinDetailViewModel, coinId : String) {
    val coinDetails by viewModel.getCoinDetails(coinId).collectAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(top = 20.dp)
    ) {
        val navController = rememberNavController()
        var bottomBarState by remember { mutableStateOf(true) }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp), // Space for bottom nav
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item {
                coinDetails?.let {
                    CoinDetailsHeader(coinDetails!!)
                } }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item { coinDetails?.let {
                CoinDetailChart(coinDetails!!)
            } }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { coinDetails?.let {
                CoinNews(coinDetails!!)
            } }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item { coinDetails?.let {
                CurrentHolding(coinDetails!!)
            } }

        }

        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavigationWithFloatingButtons(
                navController = navController,
                bottomBarState = bottomBarState,

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
fun CoinDetailsHeader(coinData : SpotlightData) {
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
                color = Color.White
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CoinDetailChart(coinData: SpotlightData) {
    val UFD = "UFD"
    var selectedChartDuration = remember { mutableStateOf("4H") }
    var priceData = remember { mutableStateOf(coinData.graphs.oneDay.getPriceValues()) }
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val chartData by remember(selectedChartDuration.value) {
        derivedStateOf {
            when (selectedChartDuration.value) {
                "LIVE" -> coinData.graphs.live.getPriceValues()
                "1D" -> coinData.graphs.oneDay.getPriceValues()
                "4H" -> coinData.graphs.fourHour.getPriceValues()
                "1W" -> coinData.graphs.sevenDay.getPriceValues()
                "1M" -> coinData.graphs.oneMonth.getPriceValues()
                "1Y" -> coinData.graphs.oneYear.getPriceValues()
                else -> coinData.graphs.fourHour.getPriceValues()
            }
        }
    }
    // Update priceData whenever chartData changes
    LaunchedEffect(chartData) {
        priceData.value = chartData
        Log.e("Price Triggered", "New price data: ${priceData.value}")
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1C1C1E)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "${formatNumber(coinData.marketData.marketCap)} Mkt. Cap",
                    fontFamily = spaceFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color(0xFF8E8E93)
                )
                BulletPoint()
                Text(
                    "$${coinData.staticData.symbol.toUpperCase()}",
                    fontFamily = spaceFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color(0xFF8E8E93)
                )

            }
            Text(
                text = "$${formatToFourDecimalPlaces(coinData.marketData.currentPrice)}",
                fontFamily = spaceFont,
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.growth_icon),
                    contentDescription = "Growth",
                    tint = green
                )
                Text(
                    text = "24%",
                    fontFamily = spaceFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = green,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
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
                    text = "what is the current market sentiment?",
                    fontFamily = spaceFont,
                    fontSize = 13.sp,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Transparent)
            ) {
                val chartColor =
                    if (priceData.value.isNotEmpty() && priceData.value.last() > priceData.value.first())
                        green.copy(alpha = 0.2f) else lightOrange.copy(alpha = 0.2f)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
//                        .background(
//                            brush = Brush.verticalGradient(
//                                colors = listOf(chartColor, Color.Transparent),
//                                startY = 0f,
//                                endY = Float.POSITIVE_INFINITY
//                            )
//                        )
                )
                Log.e("Price Data", "$priceData")
                if (priceData.value.isNotEmpty()) {
                    AnimatedPerformanceChart(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        list = priceData.value
                    )
                } else {
                    Text(
                        text = "No price data available",
                        color = Color.Gray,
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val durations = listOf("LIVE", "4H", "1D", "1W", "1M","1Y")
                var selectedDuration by remember { mutableStateOf("4H") } // Default selected

                durations.forEach { duration ->
                    Box(
                        modifier = Modifier
                            .clickable {
                                vibrateDevice(context)
                                selectedDuration = duration
                                selectedChartDuration.value = duration}
                            .then(
                                if (selectedDuration == duration) {
                                    Modifier
                                        .background(
                                            color = green,
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                } else {
                                    Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                }
                            )
                    ) {
                        Text(
                            text = duration,
                            fontSize = 11.sp,
                            fontFamily = spaceFont,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedDuration == duration) Color.Black else green
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


        }


    }
}


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CoinNews(coinData: SpotlightData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Divider()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "AT A GLANCE",
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            fontFamily = spaceFont,
            color = greyishText,
        )
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "Here are the ",
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                fontFamily = spaceFont,
                color = Color.White,
            )
            Text(
                text = "top stories",
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                fontFamily = spaceFont,
                color = green,
                textDecoration = TextDecoration.Underline
            )
        }
        Text(
            text = "for you.",
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            fontFamily = spaceFont,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(10.dp))

    }


}


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CurrentHolding(coinDetails: SpotlightData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Divider()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "CURRENT HOLDING",
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            fontFamily = spaceFont,
            color = greyishText,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1C1C1E)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Column() {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$122.90",
                            fontFamily = spaceFont,
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = "+24%",
                            fontFamily = spaceFont,
                            color = green,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .padding(top = 4.dp)
                        )
                    }
                    Text(
                        text = "Gains (last 67 days)",
                        fontFamily = spaceFont,
                        fontSize = 12.sp,
                        color = greyishText,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 4.dp)

                    )
                }
                Column() {
                    Box(
                        modifier = Modifier
                            .background(
                                color = darkishOrange,
                                shape = CircleShape
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.share_icon),
                            contentDescription = "Chat Icon",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1C1C1E)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Column() {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$0.004567",
                            fontFamily = spaceFont,
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Text(
                        text = "Price bought at (Avg.)",
                        fontFamily = spaceFont,
                        fontSize = 12.sp,
                        color = greyishText,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 4.dp)

                    )
                }
                Column() {
                    Box(
                        modifier = Modifier
                            .background(
                                color = darkishOrange,
                                shape = CircleShape
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.share_icon),
                            contentDescription = "Chat Icon",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1C1C1E)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Column() {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "128,900",
                            fontFamily = spaceFont,
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Text(
                        text = "Quantity",
                        fontFamily = spaceFont,
                        fontSize = 12.sp,
                        color = greyishText,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 4.dp)

                    )
                }

            }

        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = darkishGray,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(horizontal = 5.dp, vertical = 5.dp),
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
                text = "what are my total gains from UDF?",
                fontFamily = spaceFont,
                fontSize = 13.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Card(modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
                .padding(end = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1C1C1E)
                ),
                shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "234K",
                            fontFamily = spaceFont,
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = "+4%",
                            fontFamily = spaceFont,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = green,
                            modifier = Modifier.padding(top = 6.dp, start = 4.dp)
                        )
                    }
                    Text(
                        text = "Holders",
                        fontFamily = spaceFont,
                        fontSize = 12.sp,
                        color = greyishText,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Card(modifier = Modifier
                .wrapContentHeight()
                .weight(1f)
                .padding(start = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1C1C1E)
                ),
                shape = RoundedCornerShape(16.dp)){
                Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$19.74M",
                            fontFamily = spaceFont,
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = "-91%",
                            fontFamily = spaceFont,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = darkishOrange,
                            modifier = Modifier.padding(top = 6.dp, start = 4.dp)
                        )
                    }
                    Text(
                        text = "Holders",
                        fontFamily = spaceFont,
                        fontSize = 12.sp,
                        color = greyishText,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Card(modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
                .padding(end = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1C1C1E)
                ),
                shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "12d ago",
                            fontFamily = spaceFont,
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Text(
                        text = "Created",
                        fontFamily = spaceFont,
                        fontSize = 12.sp,
                        color = greyishText,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Card(modifier = Modifier
                .wrapContentHeight()
                .weight(1f)
                .padding(start = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1C1C1E)
                ),
                shape = RoundedCornerShape(16.dp)){
                Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "1B",
                            fontFamily = spaceFont,
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Text(
                        text = "Total Supply",
                        fontFamily = spaceFont,
                        fontSize = 12.sp,
                        color = greyishText,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "ABOUT",
            fontFamily = spaceFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = darkishGray
        )
        Spacer(modifier = Modifier.height(20.dp))
        NewsArticleWithLinks(coinDetails!!)
        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "DISCLAIMER",
            fontFamily = spaceFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = darkishGray
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Jetpack is a cryptocurrency trading platform offering meme-based coins (memcoins) for exchange. Please be aware that we do not provide financial, investment, or trading advice. Memcoins are highly volatile, speculative, and not backed by any tangible assets, meaning their value can fluctuate rapidly and without warning. Trading memcoins carries significant risk, and you should only invest what you can afford to lose. Always conduct your own research and consider consulting with a licensed financial professional before making any decisions. By using Jetpack, you acknowledge and accept these risks.",
            fontFamily = spaceFont,
            fontWeight = FontWeight.Normal,
            fontSize = 8.sp,
            color = greyishText
        )
        Spacer(modifier = Modifier.height(30.dp))


    }
}


// Usage example:
@Composable
fun NewsArticleWithLinks(coinData : SpotlightData) {
    val context = LocalContext.current
    val text = coinData.staticData.description

    ClickableNewsText(
        text = text,
        onLinkClick = { url ->
            // Handle URL click - open in browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    )
}

@Composable
fun ClickableNewsText(
    text: String,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val linkRegex = "https?://[^\\s]+".toRegex()
    val links = linkRegex.findAll(text)

    val annotatedString = buildAnnotatedString {
        var currentPosition = 0

        // Find and mark all URLs in the text
        links.forEach { result ->
            // Add text before the link
            append(text.substring(currentPosition, result.range.first))

            // Add the link with styling and tag
            pushStringAnnotation(
                tag = "URL",
                annotation = result.value
            )
            withStyle(
                style = SpanStyle(
                    color = Color.White,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(result.value)
            }
            pop()

            currentPosition = result.range.last + 1
        }

        // Add remaining text after the last link
        if (currentPosition < text.length) {
            append(text.substring(currentPosition))
        }
    }

    ClickableText(
        text = annotatedString,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        style = TextStyle(
            fontSize = 12.sp,
            color = Color.White,
            lineHeight = 18.sp,
            fontFamily = spaceFont
        ),
        onClick = { offset ->
            annotatedString
                .getStringAnnotations("URL", offset, offset)
                .firstOrNull()?.let { annotation ->
                    onLinkClick(annotation.item)
                }
        }
    )
}
