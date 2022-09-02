package com.aferrari.login.dialog

import android.app.AlertDialog
import android.content.Context

class Dialog {
    /**
     * Show dialog with result of registration operation
     */
    fun showDialog(message: String, context: Context) {
        val builder = AlertDialog.Builder(context)

        with(builder)
        {
            setTitle("Registrarse")
            setMessage(message)
            setNeutralButton("Entendido", null)
            show()
        }
    }
}