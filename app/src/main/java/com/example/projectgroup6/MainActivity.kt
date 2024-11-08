package com.example.projectgroup6


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.projectgroup6.firebase.Firebase
import com.example.projectgroup6.pet.Pet
import kotlinx.coroutines.runBlocking
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var pet: Pet
    private lateinit var firebase: Firebase
    private lateinit var sharedPreferences: SharedPreferences


    @SuppressLint("MissingInflatedId", "SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?): Unit = runBlocking{
      super.onCreate(savedInstanceState)
        supportActionBar?.hide()

      sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
      firebase = Firebase()

        //get local stored pet and get stats from db
      if(sharedPreferences.getString("pet", null) != null){

          val p: String = sharedPreferences.getString("pet", null)!!
          val t = JSONObject(p)


          pet = Pet(
              t["uuid"] as String
          )
          pet.setPetType(t["petType"].toString())

          Log.d(ContentValues.TAG, "Pet from main : $pet")



          firebase.updateLocalPet(pet, ::startPetScreen)
      //send the user to the makePet activity
     }else{
         startActivity(Intent(this@MainActivity, MakePetActivity::class.java).apply {
      })
      }
  }
    //Starts HomeScreen or Dead screen based on if pet is dead
    private fun startPetScreen(p: Pet){
        p.updateState()
        if(p.getState() == "Dead") {
            //Stops worker with tag backgroundWorker
            WorkManager.getInstance().cancelAllWorkByTag("BackgroundWorker")
            val preferences = getSharedPreferences("Settings", 0)
            preferences.edit().remove("pet").apply()
        }

        startActivity(Intent(this@MainActivity, HomeScreenActivity::class.java).apply {
            // you can add values(if any) to pass to the next class or avoid using `.apply`
            putExtra("pet", p)
        })
    }
}