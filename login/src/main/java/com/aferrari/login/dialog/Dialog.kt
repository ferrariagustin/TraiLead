package com.aferrari.login.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import com.aferrari.login.R

class Dialog {
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
}