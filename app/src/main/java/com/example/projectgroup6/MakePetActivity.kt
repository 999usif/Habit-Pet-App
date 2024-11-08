package com.example.projectgroup6

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.example.projectgroup6.firebase.Firebase
import com.example.projectgroup6.pet.Pet


class MakePetActivity : AppCompatActivity() {
    private lateinit var pet: Pet
    private lateinit var firebase: Firebase
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var makePetButton: Button
    private lateinit var addPetEditText: EditText
    private lateinit var radioPetSelected: RadioGroup
    private lateinit var radioButton: RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_pet)
        supportActionBar?.hide()


        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)

        makePetButton = findViewById(R.id.addPetButton)
        addPetEditText = findViewById(R.id.addPetEditText)
        radioPetSelected = findViewById(R.id.radioPetSelected)
        firebase = Firebase()

        //if no radio button is clicked set radioBtn
        radioButton = findViewById<View>(radioPetSelected.checkedRadioButtonId) as RadioButton
        /*
            Makes a listener for on check and get the checked radioButton
            reduces alpha for all radioButtons and set the selected alpha to 1
         */
        radioPetSelected.setOnCheckedChangeListener { _, checkedId ->
            radioButton = findViewById<View>(checkedId) as RadioButton
            radioPetSelected.forEach {
                it.backgroundTintList = getColorStateList(R.color.unselected)
            }
            radioButton.backgroundTintList = getColorStateList(R.color.transparent)
        }

        makePetButton.setOnClickListener{
            if(addPetEditText.text.isEmpty()){
                Helper.Helper.showAlert(this, "Error", "Give your pet a name")
            }else{
                pet = Pet("")
                pet.setName(addPetEditText.text.toString())

                pet.setPetType(radioButton.tag.toString())

                firebase.addPetToDb(pet, ::saveLocal)

                Log.d(ContentValues.TAG, "Pet from create: $pet")
            }
        }
    }

    /**
     * saves the pet locally and starts MainActivity
     *
     * @param pet
     */
    private fun saveLocal(pet: Pet){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()//Storing value
        editor.putString("pet", pet.toString())
        editor.apply()//Accessing values

        intent =  Intent(this@MakePetActivity, MainActivity::class.java)
        startActivity(Intent(intent))
        finish() //makes sure you cant go back to this activity
    }
}