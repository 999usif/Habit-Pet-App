package com.example.projectgroup6.task

import java.io.Serializable


class DailyTask(private var name: String,
                private var timeStamp: Long,
                private var description: String,
                private var id: Int,
                private var timeCompleted: Long
) :
    Serializable {


        fun getName(): String {
            return this.name
        }

        fun getId(): Int {
            return this.id
        }
        fun getTimeCompleted():Long{
            return this.timeCompleted
        }
        fun getDescription(): String {
            return this.description
        }


        fun getTimeStamp(): Long {
            return this.timeStamp
        }
        fun setTimeCompleted(time: Long){
            this.timeCompleted = time
        }
        fun currentTime():Long{
            return System.currentTimeMillis()
        }

    override fun toString(): String {
        return "{id:$id, timeStamp:$timeStamp, timeCompleted:$timeCompleted, description:'$description'}"
    }


}