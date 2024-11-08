package com.example.projectgroup6.firebase

import android.content.ContentValues.TAG
import android.util.Log
import com.example.projectgroup6.Helper


import com.example.projectgroup6.pet.Pet
import com.example.projectgroup6.task.DailyTask
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore



class Firebase {
    /**
     * Saves a pet to the db, only to be run on pet create
     *
     * @param pet
     * @param save to be called after firebase update is done
     */
    fun addPetToDb(pet: Pet, save: (pet: Pet) -> Unit) {
        db().collection("pet")
            .add(pet)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Pet DocumentSnapshot added with ID: " + documentReference.id)
                pet.setUuid(documentReference.id)
                save(pet)
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    /**
     * reduces stats of pet in db
     *
     * @param uuid id of pet in collection
     * @param sendNotification to be called after firebase update is done (send notification)
     */
    fun reduceStats(uuid: String, sendNotification: (type: String)  -> Unit) {
        val docRef = db().collection("pet").document(uuid)
        docRef.get().addOnSuccessListener { t ->
            docRef.update(
                "food",
                FieldValue.increment(calculateCost(t.get("timeUpdated").toString().toLong())),
                "clean",
                FieldValue.increment(calculateCost(t.get("timeUpdated").toString().toLong())),
                "love",
                FieldValue.increment(calculateCost(t.get("timeUpdated").toString().toLong())),
                "health",
                FieldValue.increment(calculateCost(t.get("timeUpdated").toString().toLong())),
                "timeUpdated",
                Helper.Helper.getTime(),
            )
            //send notifications if stats are under 20
            docRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.get("food").toString().toInt() <= 20
                    && documentSnapshot.get("love").toString().toInt() <= 20
                    && documentSnapshot.get("clean").toString().toInt() <= 20
                ) {
               //     sendNotification("StatsLow")
                }
            }
        }
    }
    /**
     * reduces stats of pet in db
     *
     * @param uuid id of pet in collection
     */
    fun reduceStats(uuid: String) {
        val docRef = db().collection("pet").document(uuid)
        docRef.get().addOnSuccessListener { t ->
            docRef.update(
                "food",
                FieldValue.increment(calculateCost(t.get("timeUpdated").toString().toLong())),
                "clean",
                FieldValue.increment(calculateCost(t.get("timeUpdated").toString().toLong())),
                "love",
                FieldValue.increment(calculateCost(t.get("timeUpdated").toString().toLong())),
                "health",
                FieldValue.increment(calculateCost(t.get("timeUpdated").toString().toLong())),
                "timeUpdated",
                Helper.Helper.getTime(),
            )
        }
    }

    /**
     * saves a dailyTask to a document
     *
     * @param uuid id of document to save dailyTask in
     * @param dailyTask dailyTask to save
     */
    fun saveDailyTask(uuid: String, dailyTask: DailyTask) {
        val docRef = db().collection("pet").document(uuid)
        docRef.get().addOnSuccessListener {
            docRef.update(
                "dailyTask", (FieldValue.arrayUnion(dailyTask))
            )
        }
    }

    /**
     * removes a dailyTask from db
     *
     * @param uuid id of document to remove from
     * @param dailyTask dailyTask to remove
     */
    fun removeDailyTask(uuid: String, dailyTask: DailyTask) {
        val docRef = db().collection("pet").document(uuid)
        docRef.get().addOnSuccessListener {
            docRef.update(
            "dailyTask", (FieldValue.arrayRemove(dailyTask))
            )
        }.addOnFailureListener{
                Log.d(TAG, "Error : $it")
        }
    }

    /**
     * update the document
     *
     * @param pet the object to be updated in the db
     */
    fun updatePet(pet: Pet){
        db().collection("pet").document(pet.getUuid()).set(pet)

    }

    /**
     * increases a stat in the db and makes sure the field cant be bigger than 100
     *
     * @param pet object to increase stats
     * @param statToIncrement which stat to increment
     * @param howMuchToIncrement the amount to increment
     * @param save function to be called after firebase is done
     */
    fun increaseStats(
        pet: Pet,
        statToIncrement: String,
        howMuchToIncrement: Int,
        save: (pet: Pet) -> Unit
    ) {
        //TODO bør ha nå error handling på statToIncrement
        var stat = 0
        if (statToIncrement == "food") {
            pet.updateFood(howMuchToIncrement)
            stat = pet.getFood()
        }
        if (statToIncrement == "clean") {
            pet.updateClean(howMuchToIncrement)
            stat = pet.getClean()
        }
        if (statToIncrement == "love") {
            pet.updateLove(howMuchToIncrement)
            stat = pet.getLove()
        }
        if (statToIncrement == "health") {
            pet.updateHealth(howMuchToIncrement)
            stat = pet.getHealth()
        }
        val docRef = db().collection("pet").document(pet.getUuid())
        docRef.get().addOnSuccessListener { t ->
            if (t.get(statToIncrement) != 100) {
                docRef.update(
                    statToIncrement, stat.toLong()
                )
            }
            save(pet)
        }
    }

    /**
     * gets fields from object in db and sets then to a local Pet object
     *
     * @param pet the pet to update
     * @param save function to be called after firebase is done
     */
    fun updateLocalPet(pet: Pet, save: (pet: Pet) -> Unit) {

        val docRef = db().collection("pet").document(pet.getUuid())
        docRef.get().addOnSuccessListener { documentSnapshot ->
            pet.setFood(documentSnapshot.get("food").toString().toInt())
            pet.setLove(documentSnapshot.get("love").toString().toInt())
            pet.setClean(documentSnapshot.get("clean").toString().toInt())
            pet.setHealth(documentSnapshot.get("health").toString().toInt())
            pet.setTimeCreated(documentSnapshot.get("timeCreated").toString().toLong())
            pet.setName(documentSnapshot.get("name").toString())


            val t = documentSnapshot.get("dailyTask") as List<HashMap<String, String>>
            val l = mutableListOf<DailyTask>()

            t.forEach {
                l += DailyTask(
                    it["name"].toString(),
                    it["timeStamp"].toString().toLong(),
                    it["description"].toString(),
                    it["id"].toString().toInt(),
                    it["timeCompleted"].toString().toLong()
                )
            }

            pet.setDailyTask(l)
            Log.d(TAG, "Pet test : ${pet.getDailyTask()}")

            save(pet)
        }
    }

    /**
     * gets fields from object in db and sets then to a local Pet object
     *
     * @param pet the pet to update
     */
    fun updateLocalPet(pet: Pet) {
        val docRef = db().collection("pet").document(pet.getUuid())
        docRef.get().addOnSuccessListener { documentSnapshot ->
            pet.setFood(documentSnapshot.get("food").toString().toInt())
            pet.setLove(documentSnapshot.get("love").toString().toInt())
            pet.setClean(documentSnapshot.get("clean").toString().toInt())
            pet.setHealth(documentSnapshot.get("health").toString().toInt())
            pet.setTimeCreated(documentSnapshot.get("timeCreated").toString().toLong())
            pet.setName(documentSnapshot.get("name").toString())


            val t = documentSnapshot.get("dailyTask") as List<HashMap<String, String>>
            val l = mutableListOf<DailyTask>()

            t.forEach {
                l += DailyTask(
                    it["name"].toString(),
                    it["timeStamp"].toString().toLong(),
                    it["description"].toString(),
                    it["id"].toString().toInt(),
                    it["timeCompleted"].toString().toLong()
                )
            }

            pet.setDailyTask(l)

        }
    }

    /**
     * calculates how much the field is to be decreased
     *
     * @param timeStamp
     * @return last time pet was updated - current time divided by how much we want to decrease pr second
     */
    private fun calculateCost(timeStamp: Long): Long {
        Log.d(TAG, "Pet time ${(timeStamp - Helper.Helper.getTime() * 0.001157).toLong()}")
        return ((timeStamp - Helper.Helper.getTime()) * 0.001157).toLong()
    }

    /**
     * gets a Instance of firebase
     *
     * @return firestore instance
     */
    private fun db(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


}
