package app.jetpack.jetpackfinal.utils

import android.util.Base64

fun ByteArray.toCustomString(): String {
    return Base64.encodeToString(this, Base64.DEFAULT)
}
fun String.toCustomByteArray(): ByteArray {
    return Base64.decode(this, Base64.DEFAULT)
}
fun formatToFourDecimalPlaces(number: Double): String {
    return String.format("%.4f", number)
}
fun formatToTwoDecimalPlaces(number: Double): String {
    return String.format("%.2f", number)
}
fun formatNumber(number: Long): String {
    return when {
        number >= 1_000_000_000 -> "%.1fB".format(number / 1_000_000_000.0)
        number >= 1_000_000 -> "%.1fM".format(number / 1_000_000.0)
        number >= 1_000 -> "%.1fK".format(number / 1_000.0)
        else -> number.toString()
    }
}
fun getValuePercentageForRange(value: Float, max: Float, min: Float) =
    (value - min) / (max - min)