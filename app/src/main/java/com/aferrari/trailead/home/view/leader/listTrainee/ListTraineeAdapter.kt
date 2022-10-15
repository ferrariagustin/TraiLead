package com.aferrari.trailead.home.view.leader.listTrainee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.db.Trainee
import com.aferrari.trailead.R

class ListTraineeAdapter(private val dataSet: List<Trainee>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_trainee_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = dataSet.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

}
