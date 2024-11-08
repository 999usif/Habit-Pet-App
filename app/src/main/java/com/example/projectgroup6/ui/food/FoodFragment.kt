package com.example.projectgroup6.ui.food

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.example.projectgroup6.HomeScreenActivity
import com.example.projectgroup6.R



class FoodFragment : Fragment() {


    private lateinit var foodButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init button
        foodButton = view.findViewById(R.id.food_f_btn)

        foodButton.setOnClickListener {
            // create method to add cleanliness cleanPet(50)
            (activity as HomeScreenActivity).feedPet()
            parentFragmentManager.popBackStack()
        }
    }

}