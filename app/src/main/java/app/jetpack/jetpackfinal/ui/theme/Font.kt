package app.jetpack.jetpackfinal.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import app.jetpack.jetpackfinal.R

@RequiresApi(Build.VERSION_CODES.Q)
val spaceFont = FontFamily(
    Font(R.font.spacemono_regular, FontWeight.Normal),
    Font(R.font.spacemono_bold, FontWeight.Bold),
)