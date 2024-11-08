package com.example.projectgroup6


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import androidx.work.*
import com.example.projectgroup6.firebase.Firebase
import com.example.projectgroup6.pet.Pet
import com.example.projectgroup6.ui.clean.CleanFragment
import com.example.projectgroup6.ui.dead.DeadPetFragment
import com.example.projectgroup6.ui.food.FoodFragment
import com.example.projectgroup6.ui.stats.StatsPopupFragment
import com.example.projectgroup6.ui.task.TaskFragment
import com.example.projectgroup6.worker.StatsWorker
import java.util.concurrent.TimeUnit

class HomeScreenActivity : AppCompatActivity() {


    private lateinit var healthProgressBar: ProgressBar
    private lateinit var foodProgressBar: ProgressBar
    private lateinit var cleanProgressBar: ProgressBar
    private lateinit var loveProgressBar: ProgressBar
    private lateinit var addTask: ImageView
    private lateinit var cleanBtn : Button
    private lateinit var foodBtn : Button
    lateinit var taskBtn : Button

    private lateinit var firebase: Firebase

    private lateinit var petName: TextView

    private lateinit var pet: Pet

    private lateinit var fragmentTransaction : FragmentTransaction



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        addTask = findViewById(R.id.addTask)
        pet = (intent.getSerializableExtra("pet") as? Pet)!!



        firebase = Firebase()

        supportActionBar?.hide()

        petName = findViewById(R.id.petName)

        healthProgressBar = findViewById(R.id.healthProgressBar)
        foodProgressBar = findViewById(R.id.foodProgressBar)
        cleanProgressBar = findViewById(R.id.cleanProgressBar)
        loveProgressBar = findViewById(R.id.loveProgressBar)
        cleanBtn = findViewById(R.id.clean_btn)
        foodBtn = findViewById(R.id.food_btn)
        taskBtn = findViewById(R.id.play_btn)


        //replace FragContainer with CleanFragment
        cleanBtn.setOnClickListener {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            supportFragmentManager.popBackStack()
            fragmentTransaction.replace<CleanFragment>(R.id.fragment_container_view)
            fragmentTransaction.addToBackStack("cleanFragmentTransaction")

            fragmentTransaction.commit()
        }

        foodBtn.setOnClickListener {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            supportFragmentManager.popBackStack()
            fragmentTransaction.replace<FoodFragment>(R.id.fragment_container_view, "taskFragment")
            fragmentTransaction.addToBackStack("foodFragmentTransaction")

            fragmentTransaction.commit()
        }

        taskBtn.setOnClickListener {
            firebase.updateLocalPet(pet)
            //delay to make sure firebase update is completed
            Handler().postDelayed({
                fragmentTransaction = supportFragmentManager.beginTransaction()
                supportFragmentManager.popBackStack()
                fragmentTransaction.replace<TaskFragment>(R.id.fragment_container_view)
                fragmentTransaction.addToBackStack("taskFragmentTransaction")


                fragmentTransaction.commit()
            }, 300)

        }
        findViewById<ImageView>(R.id.menuButton).setOnClickListener {
            val statsPopupFragment = StatsPopupFragment()
            statsPopupFragment.show(supportFragmentManager, "statsFragment")
        }

        addTask.setOnClickListener {
            startActivity(Intent(this@HomeScreenActivity, TaskScheduler::class.java).apply {
                putExtra("pet", pet)
            })
        }

        if (pet.getState() == "Dead") {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            supportFragmentManager.popBackStack()
            fragmentTransaction.replace<DeadPetFragment>(R.id.fragment_container_view)
            fragmentTransaction.addToBackStack("deadFragmentTransaction")

            fragmentTransaction.commit()
        }


        val mainHandler = Handler(Looper.getMainLooper())
        Log.d(TAG, "Pet test : $pet")
        firebase.reduceStats(pet.getUuid())

        //Updates the bars after 10 min
        Handler().postDelayed({
        mainHandler.post(object : Runnable {
            override fun run() {
                firebase.updateLocalPet(pet, ::updateBars)
                Log.d(TAG, "Pet in main handler : $pet")
                mainHandler.postDelayed(this, 100000 * 10)
            }
        })
    }, 500)
        Log.d(TAG, "Pet test : $pet")

        startWorker()

    }


    fun feedPet(){
        firebase.increaseStats(pet, "food", 30, ::updateBars)
    }
    fun cleanPet(){
        firebase.increaseStats(pet, "clean", 30, ::updateBars)
    }
    fun healthPet(){
        firebase.increaseStats(pet, "health", 30, ::updateBars)
    }
    fun taskCompleted(){
        firebase.increaseStats(pet, "love", 30, ::updateBars)
        //updates the db to set time completed on dailyTask
        firebase.updatePet(pet)


    }

    /**
     *  gets the pet and updates the bars on homeScreen
     *
     * @return
     */
    fun getPetAndUpdateState(): Pet{
        pet.updateState()
        return pet
    }
    fun getPet(): Pet{
        return pet
    }

    /**
     * updates the bars on the homeScreen
     *
     * @param p Pet
     */
    fun updateBars(p: Pet){
        pet = p
        petName.text = pet.getName()
        healthProgressBar.progress = pet.getHealth()
        foodProgressBar.progress = pet.getFood()
        cleanProgressBar.progress = pet.getClean()
        loveProgressBar.progress = pet.getLove()
    }

    /**
     * starts the background worker
     *
     */
    private fun startWorker(){
        val myData: Data = workDataOf(
            "uuid" to pet.getUuid(),
        )
        val worker = PeriodicWorkRequestBuilder<StatsWorker>(15, TimeUnit.MINUTES).setInputData(myData).addTag("BackgroundWorker")
            .build()

        //Starts worker and makes sure it only runs once.
        WorkManager.getInstance(this@HomeScreenActivity).enqueueUniquePeriodicWork("Send Data",  ExistingPeriodicWorkPolicy.KEEP, worker)
    }
}