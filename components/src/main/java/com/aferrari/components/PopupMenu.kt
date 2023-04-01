package com.aferrari.components

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment

/**
 * This is a trailead popupmenu
 * @param view is the view that selected open popup Menu
 * @param fragment container popup
 */
class TraileadPopupMenu(
    private val view: View,
    private val fragment: Fragment
) {
    private var popupMenu: PopupMenu = PopupMenu(fragment.requireContext(), view)

    /**
     * Create PopupMenu
     * @param menu layout open popup
     */
    fun create(
        @MenuRes menu: Int,
        @IdRes primaryColor: Int
    ): TraileadPopupMenu {
        fragment.requireActivity().menuInflater.inflate(
            menu,
            popupMenu.menu
        )
        popupMenu.setForceShowIcon(true)
        configStyleMenu(primaryColor)
        return this
    }

    fun setOnClickListener(listener: PopupMenu.OnMenuItemClickListener): TraileadPopupMenu {
        popupMenu.setOnMenuItemClickListener(listener)
        return this
    }


    /**
     * Show PopupMenu
     */
    fun show() {
        popupMenu.show()
    }

    @SuppressLint("ResourceType")
    private fun configStyleMenu(@IdRes primaryColor: Int) {
        popupMenu.menu.children.forEach {
            it.icon?.let { icon ->
                DrawableCompat.setTint(
                    icon,
                    ContextCompat.getColor(fragment.requireContext(), primaryColor)
                );
            }
        }
    }
}