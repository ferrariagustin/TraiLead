package com.aferrari.trailead.common

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Activity.applyStatusBarFromTheme() {
    val primary = themeColor(com.google.android.material.R.attr.colorPrimary)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        // Edge-to-edge ON + barra transparente
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT

        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            // Pinteá un scrim del color del tema (o usá tu vista AppBar)
            v.setBackgroundColor(primary)
            v.setPadding(0, status.top, 0, 0)
            insets
        }
    } else {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor = primary
    }

    // Íconos claros u oscuros según el color
    WindowInsetsControllerCompat(window, window.decorView)
        .isAppearanceLightStatusBars = primary.isLight()
}

private fun Int.isLight(): Boolean {
    val r = (this shr 16 and 0xFF) / 255.0
    val g = (this shr 8 and 0xFF) / 255.0
    val b = (this and 0xFF) / 255.0
    val luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b
    return luminance > 0.5
}


@ColorInt
fun Context.themeColor(@AttrRes attr: Int): Int {
    val tv = TypedValue()
    val ok = theme.resolveAttribute(attr, tv, true)
    if (!ok) error("El atributo de tema no existe: $attr")
    return if (tv.resourceId != 0) {
        ContextCompat.getColor(this, tv.resourceId)
    } else {
        tv.data
    }
}