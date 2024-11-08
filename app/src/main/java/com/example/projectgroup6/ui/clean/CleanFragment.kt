package com.example.projectgroup6.ui.clean

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.projectgroup6.HomeScreenActivity
import com.example.projectgroup6.R


class CleanFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clean, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init button
        view.findViewById<Button>(R.id.shower_btn).setOnClickListener {
            (activity as HomeScreenActivity).cleanPet()
            parentFragmentManager.popBackStack()
        }

    }

}