package com.example.projectgroup6.ui.task

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projectgroup6.Helper
import com.example.projectgroup6.HomeScreenActivity
import com.example.projectgroup6.R
import com.example.projectgroup6.task.DailyTask
import java.text.DateFormat
import java.text.SimpleDateFormat




class TaskFragment : Fragment() {


    private lateinit var dailyTaskLinearLayout: LinearLayout
    private lateinit var backgroundImageView: ImageView

    private lateinit var inflater: View



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    @SuppressLint("ResourceType")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dailyTaskLinearLayout = view.findViewById(R.id.dailyTaskLinearLayout)
        (activity as HomeScreenActivity).taskBtn.isEnabled = false




        (activity as HomeScreenActivity).getPet().getDailyTask().forEach {
            addTaskToLayout(it, view.context)
        }

        //Get background imageview and hide it when displaying tasks
        backgroundImageView = (activity as HomeScreenActivity).findViewById(R.id.backgroundImageView)
        backgroundImageView.imageAlpha = 0

    }

    override fun onDestroyView() {
        super.onDestroyView()
        //turns of the taskBtn to reduce db calls
        (activity as HomeScreenActivity).taskBtn.isEnabled = true
        //restore background imageview after this fragment view is destroyed
        backgroundImageView.imageAlpha = 255
    }


    @SuppressLint("SimpleDateFormat")
    fun addTaskToLayout(dailyTask: DailyTask, context: Context) {

        inflater = LayoutInflater.from(context).inflate(R.layout.task_list_item, null)
        inflater.findViewById<TextView>(R.id.taskNameTaskListItem).text = dailyTask.getName()

        val obj: DateFormat = SimpleDateFormat("HH:mm")
        inflater.findViewById<TextView>(R.id.taskTimeTaskListItem).text = obj.format(dailyTask.getTimeStamp())


        //if task was completed in the last 12 hours
        if(dailyTask.currentTime() - dailyTask.getTimeCompleted() > 43200000){
            inflater.findViewById<ImageView>(R.id.taskListItemCompleteTask).setOnClickListener {
                dailyTask.setTimeCompleted(dailyTask.currentTime())
              //  (activity as HomeScreenActivity).getPet().removeFromDailyTask(dailyTask)
                (activity as HomeScreenActivity).taskCompleted()

                reLoadList()
            }
        }else{
            val button = inflater.findViewById<ImageView>(R.id.taskListItemCompleteTask)
            button.isEnabled = false
            button.setImageResource(R.drawable.check_mark)
        }
        inflater.findViewById<ImageView>(R.id.taskListItemRemoveTask).setOnClickListener {
            val builder = Helper.Helper.getAlertDialog("Are you sure you want to delete ${dailyTask.getDescription()}", context)
                .setPositiveButton("Yes") { _, _ ->
                    // Delete selected note from database
                    (activity as HomeScreenActivity).getPet().removeFromDailyTask(dailyTask)
                    reLoadList()
                }
                .setNegativeButton("No") { dialog, _ ->

                    dialog.dismiss()
                }

            val alert = builder.create()
            alert.show()



        }
        dailyTaskLinearLayout.addView(inflater)
    }


    private fun reLoadList(){
        dailyTaskLinearLayout.removeAllViews()
        (activity as HomeScreenActivity).getPet().getDailyTask().forEach{
            view?.let { it1 -> addTaskToLayout(it, it1.context) }
        }

    }
}