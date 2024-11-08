package com.example.projectgroup6.ui.def

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.projectgroup6.HomeScreenActivity
import com.example.projectgroup6.R



class DefaultFragment : Fragment() {
    private lateinit var petAnimation: AnimationDrawable
    private lateinit var petImage: ImageView

    private var idleAnimation = 0
    private var onClickAnimation = 0
    private var onGroundAnimation  = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_default, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petImage =  view.findViewById(R.id.petImageView)

        when ((activity as HomeScreenActivity).getPet().getPetType()) {
            getString(R.string.fox) -> {
                idleAnimation = R.drawable.fox_animation
                onGroundAnimation = R.drawable.fox_on_the_ground_animation
                onClickAnimation = R.drawable.fox_on_click_animation
            }
            getString(R.string.squirrel) -> {
                idleAnimation = R.drawable.squirrel_idle_animation
                onGroundAnimation = R.drawable.squirrel_on_the_ground_animation
                onClickAnimation = R.drawable.squirrel_onclick_animation
            }
        }

        if ((activity as HomeScreenActivity).getPetAndUpdateState().getState() == "Sad"){
            getPetImage(onGroundAnimation)
            petAnimation.start()
        }else {
            getPetImage(idleAnimation)
            petAnimation.start()

            petImage.setOnClickListener {
                petAnimation.stop()
                if((activity as HomeScreenActivity).getPet().getHealth() != 100){
                    (activity as HomeScreenActivity).healthPet()
                }

                getPetImage(onClickAnimation)
                petAnimation.start()
                petAnimation.onAnimationFinished {
                    petAnimation.stop()
                    getPetImage(idleAnimation)
                    petAnimation.start()
                }
            }
        }
    }

    private fun AnimationDrawable.onAnimationFinished(block: () -> Unit) {
        var duration: Long = 0
        for (i in 0..numberOfFrames) {
            duration += getDuration(i)
        }
        Handler().postDelayed({
            block()
        }, duration + 200)
    }
    private fun getPetImage(url: Int):ImageView{
        petImage.apply {
            setBackgroundResource(url)
            petAnimation = background as AnimationDrawable
        }
        return petImage
    }
}