package com.aferrari.login.dialog

import android.app.AlertDialog
import android.content.Context

class Dialog {
    /**
     * Show dialog with result of registration operation
     */
    fun showDialog(title: String,message: String, context: Context) {
        val builder = AlertDialog.Builder(context)

        with(builder)
        {
            setTitle(title)
            setMessage(message)
            setNeutralButton("Entendido", null)
            show()
        }
    }
}