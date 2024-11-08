package com.example.projectgroup6.ui.stats

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.projectgroup6.HomeScreenActivity
import com.example.projectgroup6.R


class StatsPopupFragment : DialogFragment() {


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pet = (activity as HomeScreenActivity).getPet()
        view.findViewById<TextView>(R.id.statsMenuTextAge).text  = "Age : ${pet.getAge()} Hours"
        view.findViewById<TextView>(R.id.statsMenuTextLove).text  = "Love : ${pet.getLove()}/100"
        view.findViewById<TextView>(R.id.statsMenuTextFood).text  = "Food : ${pet.getFood()}/100"
        view.findViewById<TextView>(R.id.statsMenuTextClean).text  = "Clean : ${pet.getClean()}/100"
        view.findViewById<TextView>(R.id.statsMenuTextHealth).text  = "Health : ${pet.getHealth()}/100"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_stats_popup, container, false)
    }

}