package app.jetpack.jetpackfinal.ui.home.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import app.jetpack.jetpackfinal.R
import app.jetpack.jetpackfinal.ui.theme.backgroundColor
import app.jetpack.jetpackfinal.ui.theme.backgroundLightColor
import app.jetpack.jetpackfinal.ui.theme.darkGray
import app.jetpack.jetpackfinal.ui.theme.darkishGray
import app.jetpack.jetpackfinal.ui.theme.darkishOrange
import app.jetpack.jetpackfinal.ui.theme.green
import app.jetpack.jetpackfinal.ui.theme.greyishText
import app.jetpack.jetpackfinal.ui.theme.lightOrange
import app.jetpack.jetpackfinal.ui.theme.spaceFont
import app.jetpack.jetpackfinal.utils.BottomNavigationBar
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun UserPortfolio(){
    var searchQuery by remember { mutableStateOf("") }
    var bottomBarState by remember { mutableStateOf(true) }
    val navController = rememberNavController()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(top = 20.dp)
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp), // Space for bottom nav
            contentPadding = PaddingValues(horizontal = 16.dp)
        ){
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                SearchHolding(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                TotalHolding(
                    amount = 12.00,
                    percentageChange = -14.0,
                    onCardClick = { /* Handle click */ }
                )
            }
            item { HoldingNews()
                Spacer(modifier = Modifier.height(32.dp))
            }

            //Need to add later important change the Implementation here






//            itemsIndexed(sampleCryptoCards,
//                key = { _, card -> card.id }
//            ) { index, card ->
//                TopTokenItem(index, card)
//                if (index < sampleCryptoCards.size - 1) {
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//            }

        }
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
fun TotalHolding(
    amount: Double,
    percentageChange: Double,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1C1C1E)
        )
    ){
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text= "Total",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = spaceFont,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$12,000",
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
                    Text(
                        text = "(ALL TIME)",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontFamily = spaceFont,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(start= 4.dp)
                    )
                }
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
                        text = "$12,000",
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
                    text = "Portfolio",
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
                        text = "$9,019",
                        fontFamily = spaceFont,
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Normal
                    )

                }
                Text(
                    text = "Cash",
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
    Divider()
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HoldingNews(){
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
                text = "top news",
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                fontFamily = spaceFont,
                color = darkishOrange,
                textDecoration = TextDecoration.Underline
            )
        }
        Text(
            text = "on your holdings.",
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            fontFamily = spaceFont,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(28.dp))
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
                text = "is the market bullish about a16z?",
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
private fun SearchHolding(
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
                            text = "Search your holdings",
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

@Preview
@Composable
fun UserPortfolioScreen(){
    UserPortfolio()
}