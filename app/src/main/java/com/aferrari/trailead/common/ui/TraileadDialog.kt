package com.aferrari.trailead.common.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.aferrari.trailead.R

class TraileadDialog {
    /**
     * Show dialog with result of registration operation
     */
    fun showDialog(title: String, message: String, context: Context) {
        val builder = AlertDialog.Builder(context)

        with(builder)
        {
            setTitle(title)
            setMessage(message)
            setNeutralButton(context.resources.getString(R.string.neutral_dialog), null)
            show()
        }
    }

    /**
     * Show dialog with result of registration operation
     */
    @Deprecated("Use showDialogWithAction method with fragment")
    fun showDialogWithAction(
        title: String,
        message: String,
        context: Context,
        positiveAction: DialogInterface.OnClickListener? = null,
        negativeAction: DialogInterface.OnClickListener? = null,
        icon: Drawable? = null
    ) {
        val builder = AlertDialog.Builder(context)
        with(builder)
        {
            setTitle(title)
            setMessage(message)
            setIcon(icon)
            setPositiveButton(context.resources.getString(R.string.positive_dialog), positiveAction)
            setNegativeButton(context.resources.getString(R.string.negative_dialog), negativeAction)
            show()
        }
    }

    /**
     * Show Dialog with cancel and success actions
     * @param title Titlo of dialog
     * @param message Message of dialog
     * @param fragment fragment container
     * @param positiveAction positive action listener
     * @param negativeAction negative action listener
     * @param iconRes res of icon include on dialog
     * @param colorRes Color of icon
     */
    fun showDialogWithAction(
        title: String,
        message: String,
        fragment: Fragment,
        positiveAction: DialogInterface.OnClickListener? = null,
        negativeAction: DialogInterface.OnClickListener? = null,
        @DrawableRes iconRes: Int? = null,
        @ColorRes colorRes: Int = R.color.primaryColor
    ) {
        val builder = AlertDialog.Builder(fragment.requireContext())
        with(builder)
        {
            setTitle(title)
            setMessage(message)
            setIcon(getDrawableIcon(iconRes, fragment, colorRes))
            setPositiveButton(context.resources.getString(R.string.positive_dialog), positiveAction)
            setNegativeButton(context.resources.getString(R.string.negative_dialog), negativeAction)
            show()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDrawableIcon(
        iconRes: Int?,
        fragment: Fragment,
        @ColorRes colorRes: Int
    ): Drawable {
        iconRes?.let {
            val drawableIcon = fragment.resources.getDrawable(it, fragment.requireContext().theme)
            DrawableCompat.setTint(
                drawableIcon,
                ContextCompat.getColor(fragment.requireContext(), colorRes)
            )
            return drawableIcon
        }
        return getGenericDrawableIcon(fragment)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getGenericDrawableIcon(fragment: Fragment): Drawable =
        fragment.resources.getDrawable(
            androidx.appcompat.R.drawable.abc_dialog_material_background,
            fragment.requireContext().theme
        )
}