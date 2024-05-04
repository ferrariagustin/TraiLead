package com.aferrari.trailead.common.ui

import android.content.Context
import android.view.View
import com.aferrari.trailead.R
import com.aferrari.trailead.common.common_enum.ErrorView
import com.google.android.material.snackbar.Snackbar


class TraiLeadSnackbar {

    fun init(
        context: Context,
        view: View,
        message: String,
        type: ErrorView = ErrorView.INFO,
        mode: Int = Snackbar.LENGTH_SHORT,
        disable: (() -> Unit)? = null
    ) {

        val snackbar =
            Snackbar.make(view, message, mode)

        // Personaliza el color de fondo del Snackbar (opcional)
        snackbar.view.setBackgroundColor(
            context.resources.getColor(
                getBackgroundColor(type),
                context.theme
            )
        )

        // Personaliza el color del texto del Snackbar (opcional)
        snackbar.setActionTextColor(
            context.resources.getColor(
                R.color.primaryTextColor,
                context.theme
            )
        )

        disable?.let {
            // Puedes agregar una acción al Snackbar si lo deseas
            snackbar.setAction("Des Hacer") {
                it
            }
        }

        snackbar.animationMode = Snackbar.ANIMATION_MODE_SLIDE

        snackbar.show()
    }

    fun errorConection(context: Context, view: View) {
        init(context, view, context.getString(R.string.internet_connection), ErrorView.ERROR)
    }

    private fun getBackgroundColor(type: ErrorView) = when (type) {
        ErrorView.ERROR -> {
            R.color.red
        }

        ErrorView.SUCCESS -> {
            R.color.success
        }

        ErrorView.INFO -> {
            R.color.primaryColor
        }

        ErrorView.WARNING -> {
            R.color.warning
        }
    }
}