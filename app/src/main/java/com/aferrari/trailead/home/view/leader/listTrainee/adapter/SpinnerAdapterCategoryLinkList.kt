package com.aferrari.trailead.home.view.leader.listTrainee.adapter

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Toast


class SpinnerAdapterCategoryLinkList(private val context: Context) :
    AdapterView.OnItemSelectedListener {

    var itemSelected: String = EMPTY_STRING

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // On selecting a spinner item
        itemSelected = parent?.getItemAtPosition(position)?.toString() ?: EMPTY_STRING
        Toast.makeText(context, "Item Selected: $itemSelected", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
        // TODO Auto-generated method stub
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}