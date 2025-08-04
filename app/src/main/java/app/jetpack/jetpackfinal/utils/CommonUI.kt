package app.jetpack.jetpackfinal.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.jetpack.jetpackfinal.R
import app.jetpack.jetpackfinal.ui.theme.darkishOrange
import app.jetpack.jetpackfinal.ui.theme.green
import app.jetpack.jetpackfinal.ui.theme.lightBlack
import app.jetpack.jetpackfinal.ui.theme.spaceFont

@Composable
fun BottomNavigationBar(
    navController: NavController,
    bottomBarState: Boolean,
    modifier: Modifier = Modifier,
) {
    val withPx = LocalContext.current.resources.displayMetrics.widthPixels
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AnimatedVisibility(visible = bottomBarState) {
        val barShape = BarShape(
            offset = withPx / 2f,
            circleRadius = 28.dp,
            cornerRadius = 15.dp,
            circleGap = 8.dp
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = lightBlack,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 15.dp  // Reduced elevation to work better with overlapping FAB
            ),
            shape = barShape,
            modifier = modifier
                .fillMaxWidth()
                .background(shape = barShape, color = lightBlack)
                .shadow(
                    elevation = 15.dp,
                    shape = barShape
                )
        ) {
            NavigationBar(
                modifier = Modifier
                    .background(lightBlack)
                    .fillMaxWidth()
                    .height(80.dp)
                    .graphicsLayer {
                        shape = barShape
                        clip = true
                    },
                contentColor = lightBlack,
                containerColor = lightBlack
            ) {
                val items = listOf(
                    app.jetpack.jetpackfinal.utils.BottomNavItem.Home,
                    app.jetpack.jetpackfinal.utils.BottomNavItem.Search,
                    app.jetpack.jetpackfinal.utils.BottomNavItem.Null,
                    app.jetpack.jetpackfinal.utils.BottomNavItem.Notifications,
                    app.jetpack.jetpackfinal.utils.BottomNavItem.Profile
                )

                items.forEach { item ->
                    if (item.route.isEmpty()) {
                        Spacer(
                            modifier = Modifier
                                .width(72.dp)
                                .fillMaxHeight()
                        )
                    } else {
                        var selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = if (selected) painterResource(item.selectedIcon) else painterResource(item.icon),
                                    contentDescription = item.title,
                                    modifier = Modifier.size(24.dp),
                                    tint = if (selected) Color.White
                                    else Color.Gray.copy(alpha = 0.6f)
                                )
                            },

                            selected = selected,
                            onClick = {
//                                navController.navigate(item.route) {
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.Gray.copy(alpha = 0.6f),
                                indicatorColor = Color.Transparent // This removes the background indicator
                            )

                        )
                    }
                }
            }
        }
    }
}



class BarShape(
    private val offset: Float,
    private val circleRadius: Dp,
    private val cornerRadius: Dp,
    private val circleGap: Dp = 5.dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(getPath(size, density))
    }

    private fun getPath(size: Size, density: Density): Path {
        val cutoutCenterX = offset
        val cutoutRadius = density.run { (circleRadius + circleGap).toPx() }
        val cornerRadiusPx = density.run { cornerRadius.toPx() }
        val cornerDiameter = cornerRadiusPx * 2

        return Path().apply {
            val cutoutEdgeOffset = cutoutRadius * 2.5f
            val cutoutLeftX = cutoutCenterX - cutoutEdgeOffset
            val cutoutRightX = cutoutCenterX + cutoutEdgeOffset

            // Bottom left
            moveTo(x = 0F, y = size.height)

            // Top left corner
            if (cutoutLeftX > 0) {
                val realLeftCornerDiameter = if (cutoutLeftX >= cornerRadiusPx) {
                    cornerDiameter
                } else {
                    cutoutLeftX * 2
                }
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = 0f,
                        right = realLeftCornerDiameter,
                        bottom = realLeftCornerDiameter
                    ),
                    startAngleDegrees = 180.0f,
                    sweepAngleDegrees = 90.0f,
                    forceMoveTo = false
                )
            }

            lineTo(cutoutLeftX, 0f)

            // Center cutout
            cubicTo(
                x1 = cutoutCenterX - cutoutRadius,
                y1 = 0f,
                x2 = cutoutCenterX - cutoutRadius,
                y2 = cutoutRadius,
                x3 = cutoutCenterX,
                y3 = cutoutRadius,
            )
            cubicTo(
                x1 = cutoutCenterX + cutoutRadius,
                y1 = cutoutRadius,
                x2 = cutoutCenterX + cutoutRadius,
                y2 = 0f,
                x3 = cutoutRightX,
                y3 = 0f,
            )

            // Top right corner
            if (cutoutRightX < size.width) {
                val realRightCornerDiameter = if (cutoutRightX <= size.width - cornerRadiusPx) {
                    cornerDiameter
                } else {
                    (size.width - cutoutRightX) * 2
                }
                arcTo(
                    rect = Rect(
                        left = size.width - realRightCornerDiameter,
                        top = 0f,
                        right = size.width,
                        bottom = realRightCornerDiameter
                    ),
                    startAngleDegrees = -90.0f,
                    sweepAngleDegrees = 90.0f,
                    forceMoveTo = false
                )
            }

            // Bottom right
            lineTo(x = size.width, y = size.height)
            close()
        }
    }
}

// Bottom navigation items
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int,
    val selectedIcon: Int
) {
    object Home : BottomNavItem("home", "Home", R.drawable.home_unselected , R.drawable.home_selected)
    object Search : BottomNavItem("search", "Search", R.drawable.trending_icon, R.drawable.trending_icon)
    object Null : BottomNavItem("", "",R.drawable.wallet_icon, R.drawable.home_unselected)
    object Notifications : BottomNavItem("notifications", "Notifications", R.drawable.wallet_icon, R.drawable.wallet_icon)
    object Profile : BottomNavItem("profile", "Profile", R.drawable.profile_icon, R.drawable.profile_icon)
}

//@Composable
//fun BottomNavigationBar(
//    navController: NavController,
//    bottomBarState: Boolean,
//    modifier: Modifier = Modifier,
//) {
//    val withPx = LocalContext.current.resources.displayMetrics.widthPixels
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = navBackStackEntry?.destination
//
//    AnimatedVisibility(visible = bottomBarState) {
//        val barShape = BarShape(
//            offset = withPx / 2f,
//            circleRadius = 28.dp,
//            cornerRadius = 15.dp,
//            circleGap = 8.dp
//        )
//
//        Card(
//            colors = CardDefaults.cardColors(
//                containerColor = lightBlack,
//            ),
//            elevation = CardDefaults.cardElevation(
//                defaultElevation = 15.dp  // Reduced elevation to work better with overlapping FAB
//            ),
//            shape = barShape,
//            modifier = modifier
//                .fillMaxWidth()
//                .background(shape = barShape, color = lightBlack)
//                .shadow(
//                    elevation = 15.dp,
//                    shape = barShape
//                )
//        ) {
//            NavigationBar(
//                modifier = Modifier
//                    .background(lightBlack)
//                    .fillMaxWidth()
//                    .height(80.dp)
//                    .graphicsLayer {
//                        shape = barShape
//                        clip = true
//                    },
//                contentColor = lightBlack,
//                containerColor = lightBlack
//            ) {
//                val items = listOf(
//                    BottomNavItem.Home,
//                    BottomNavItem.Search,
//                    BottomNavItem.Null,
//                    BottomNavItem.Notifications,
//                    BottomNavItem.Profile
//                )
//
//                items.forEach { item ->
//                    if (item.route.isEmpty()) {
//                        Spacer(
//                            modifier = Modifier
//                                .width(72.dp)
//                                .fillMaxHeight()
//                        )
//                    } else {
//                        var selected = currentDestination?.hierarchy?.any {
//                            it.route == item.route
//                        } == true
//
//                        NavigationBarItem(
//                            icon = {
//                                Icon(
//                                    painter = if (selected) painterResource(item.selectedIcon) else painterResource(item.icon),
//                                    contentDescription = item.title,
//                                    modifier = Modifier.size(24.dp),
//                                    tint = if (selected) Color.White
//                                    else Color.Gray.copy(alpha = 0.6f)
//                                )
//                            },
//
//                            selected = selected,
//                            onClick = {
////                                navController.navigate(item.route) {
////                                    launchSingleTop = true
////                                    restoreState = true
////                                }
//                            },
//                            colors = NavigationBarItemDefaults.colors(
//                                selectedIconColor = Color.White,
//                                unselectedIconColor = Color.Gray.copy(alpha = 0.6f),
//                                indicatorColor = Color.Transparent // This removes the background indicator
//                            )
//
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BottomNavigationWithFloatingButtons(
    navController: NavController,
    bottomBarState: Boolean
    // Add this parameter to control visibility
) {
    Box{
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavigationBar(
                navController = navController,
                bottomBarState = bottomBarState
            )

            // Sell Token Button (Left)
            Button(
                onClick = { /* Handle sell click */ },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = (-130).dp, y = (-60).dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkishOrange
                ),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(8.dp)  // Reduced button padding
            ) {
                Row(
                    modifier = Modifier.width(124.dp).height(36.dp),
                    horizontalArrangement = Arrangement.Start,  // Changed to Start alignment
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.Black, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "-",
                            color = darkishOrange,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))  // Reduced spacing
                    Text(
                        text = "Sell Token",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = spaceFont,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

// Buy Token Button (Right)
            Button(
                onClick = { /* Handle buy click */ },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = 130.dp, y = (-60).dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = green
                ),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(8.dp)  // Reduced button padding
            ) {
                Row(
                    modifier = Modifier.width(124.dp).height(36.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start  // Changed to Start alignment
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.Black, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            color = green,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))  // Reduced spacing
                    Text(
                        text = "Buy Token",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontFamily = spaceFont,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
fun BottomNavBarPreview(){
    val navController = rememberNavController()
    BottomNavigationWithFloatingButtons(navController,true)
}
