package com.aferrari.trailead.home.view.leader.listTrainee.adapter

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData


class SpinnerAdapterCategoryLinkList :
    AdapterView.OnItemSelectedListener {

    var itemSelected: MutableLiveData<String> = MutableLiveData<String>(EMPTY_STRING)

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // On selecting a spinner item
        itemSelected.value = parent?.getItemAtPosition(position)?.toString() ?: EMPTY_STRING
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
        // TODO Auto-generated method stub
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}