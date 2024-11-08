package com.example.projectgroup6.pet

import android.content.ContentValues.TAG
import android.util.Log
import com.example.projectgroup6.firebase.Firebase
import com.example.projectgroup6.task.DailyTask
import com.google.firebase.firestore.Exclude
import java.io.Serializable



/**
 * @constructor
 * creates a Pet object
 *
 * @param _id of pet and collection in db
 */
open class Pet (_id: String)  :
    Serializable {
    private var name = ""
    private var age = 0

    private var state = ""

    private var timeUpdated = getCurrentTime()
    private var timeCreated = getCurrentTime()

    private var food =  100
    private var clean = 100
    private var love = 100
    private var health = 100

    private var uuid = _id

    private var dailyTask =  mutableListOf<DailyTask>()

    private var petType = ""


    /**
     * set DailyTask
     *
     * @param _dailyTask list of dailyTask
     */
    fun setDailyTask(_dailyTask: List<DailyTask>){
        this.dailyTask = _dailyTask as MutableList<DailyTask>
    }

    /**
     * set uuid
     *
     * @param id
     */
    fun setUuid(id: String){
        this.uuid = id
    }

    /**
     * set Food
     *
     * @param food
     */
    fun setFood(food: Int){
        this.food = food
    }

    /**
     * set clean
     *
     * @param clean
     */
    fun setClean(clean: Int){
        this.clean = clean
    }

    /**
     * set name
     *
     * @param name
     */
    fun setName(name: String){
        this.name = name
    }

    /**
     * set love
     *
     * @param love
     */
    fun setLove(love: Int){
        this.love = love
    }

    /**
     * set health
     *
     * @param health
     */
    fun setHealth(health: Int){
        this.health = health
    }

    /**
     * set timeCreated
     *
     * @param time
     */
    fun setTimeCreated(time: Long){
        this.timeCreated = time
    }

    /**
     * updates and set the state of the pet based on the stats of the pet
     *
     */
    fun updateState(){
        //TODO fix
        val sum = (this.food + this.clean + this.health + this.love)
        Log.d(TAG, "Pet sum $sum")
        if (sum  > 300){
            this.state = State.Happy.toString()
        }
        else if (sum in 200..300){
            this.state = State.Ok.toString()
        }
        else if (sum in 1..199){
            this.state = State.Sad.toString()
        }
        else this.state = State.Dead.toString()
    }

    /**
     * get daily task
     *
     * @return
     */
    fun getDailyTask(): MutableList<DailyTask> {
        return this.dailyTask
    }

    /**
     * remove dailyTask from object and calls firebase to do the same
     *
     * @param dailyTask to remove
     */
    fun removeFromDailyTask(dailyTask: DailyTask){
        val firebase = Firebase()
        firebase.removeDailyTask(this.uuid, dailyTask)
        this.getDailyTask().remove(dailyTask)

    }

    /**
     * adds dailyTask to dailyTask list in Pet
     *
     * @param task to add
     */
    fun updateDailyTask(task: DailyTask){
        this.dailyTask += task
    }



    /**
     * updates food, and makes sure food is not bigger than 100
     *
     * @param amount
     */
    fun updateFood(amount: Int){
        if(this.food + amount < 100){
            this.food += amount
        }else{
            this.food = 100
        }
    }
    /**
     * updates health, and makes sure health is not bigger than 100
     *
     * @param amount
     */
    fun updateHealth(amount: Int){
        if(this.health + amount < 100){
            this.health += amount
        }else{
            this.health = 100
        }
    }
    /**
     * updates clean, and makes sure clean is not bigger than 100
     *
     * @param amount
     */
    fun updateClean(amount: Int){
        if(this.clean + amount < 100){
            this.clean += amount
        }else{
            this.clean = 100
        }
    }
    /**
     * updates love, and makes sure love is not bigger than 100
     *
     * @param amount
     */
    fun updateLove(amount: Int){
        if(this.love + amount < 100){
            this.love += amount
        }else{
            this.love = 100
        }
    }

    /**
     * get uuid
     *
     * @return uuid
     */
    fun getUuid():String {
        return this.uuid
    }

    /**
     * get health
     *
     * @return health
     */
    fun getHealth(): Int{
        return this.health
    }

    /**
     * get age
     *
     * @return age in hours
     */
    fun getAge(): Long {
        return (this.getCurrentTime() - this.timeCreated)/60/60
    }

    /**
     * get love
     *
     * @return love
     */
    fun getLove(): Int {
        return this.love
    }

    /**
     * get food
     *
     * @return food
     */
    fun getFood(): Int {
        return this.food
    }

    /**
     * get clean
     *
     * @return clean
     */
    fun getClean(): Int {
        return this.clean
    }

    /**
     * get name
     *
     * @return name
     */
    fun getName(): String{
        return this.name
    }
    /**
     * get state of pet
     *
     * @return
     */
    fun getState():String{
        return this.state
    }

    /**
     * get timeUpdated
     *
     * @return timeUpdated
     */
    fun getTimeUpdated(): Long {
        return this.timeUpdated
    }

    /**
     * get timeCreated
     *
     * @return
     */
    fun getTimeCreated(): Long {
        return this.timeCreated
    }

    /**
     * enum with the states the pet can have
     *
     */
    enum class State {
        Happy, Ok, Sad, Dead
    }

    /**
     * enum with the types of pet
     *
     */
    enum class PetType {
        Fox, Squirrel

    }
    /**
     * returns pet type and excludes from firebase
     * @return petType
     */
    @Exclude
    fun getPetType(): String{
        return this.petType
    }

    /**
     * checks if pet type is in enum and sets it
     *
     * @param petType
     */
    fun setPetType(petType: String){
        if(PetType.values().map { it.name }.contains(petType)){
            this.petType = petType
        }
    }

    /**
     * pet to string
     *
     * @return
     */
    override fun toString(): String {
        return """{name: "${this.name}", age: "${this.age}", timeUpdated: "${this.timeUpdated}", timeCreated: "${this.timeCreated}", food: "${this.food}", clean: "${this.clean}", love: "${this.love}", health: "${this.health}", State: "${this.state}", uuid: "$uuid", petType: ${this.petType}}"""
    }

    /**
     * @return system current time as seconds
     */
    private fun getCurrentTime(): Long {
        return System.currentTimeMillis()/1000
    }
}
