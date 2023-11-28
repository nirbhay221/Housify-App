package com.example.housify

import android.annotation.SuppressLint
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.example.housify.fragments.FavoritePropertySelectedFragment
import com.example.housify.fragments.chatFragment
import com.example.housify.fragments.homeFragment
import com.example.housify.fragments.profileFragment
import com.example.housify.viewModels.HomeViewModel
import com.example.housify.viewModels.NavigationViewModel

class NavigationActivity : AppCompatActivity() {

    private val viewModel by viewModels<NavigationViewModel>()

    private lateinit var homeLayout:LinearLayout
    private lateinit var favoriteLayout:LinearLayout
    private lateinit var profileLayout: LinearLayout
    private lateinit var chatLayout:LinearLayout

    private lateinit var homeTxt:TextView
    private lateinit var favoriteTxt:TextView
    private lateinit var profileTxt: TextView
    private lateinit var chatTxt:TextView

    private lateinit var homeImage:ImageView
    private lateinit var favoriteImage:ImageView
    private lateinit var profileImage: ImageView
    private lateinit var chatImage:ImageView

    private lateinit var fragmentManager: FragmentManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)



        homeLayout = findViewById(R.id.homeLayout)
        profileLayout= findViewById(R.id.profileLayout)
        chatLayout = findViewById(R.id.chatLayout)
        favoriteLayout = findViewById(R.id.favoriteLayout)

        homeTxt = findViewById(R.id.homeTxt)
        favoriteTxt = findViewById(R.id.favoriteTxt)
        profileTxt = findViewById(R.id.profileTxt)
        chatTxt = findViewById(R.id.chatTxt)

        homeImage = findViewById(R.id.homeImage)
        favoriteImage = findViewById(R.id.favoriteImage)
        profileImage = findViewById(R.id.profileImage)
        chatImage = findViewById(R.id.chatImage)
        Toast.makeText(this,"${viewModel.selectedTab}",Toast.LENGTH_LONG).show()
        fragmentManager = supportFragmentManager
        if(viewModel.selectedTab == 1){

            favoriteTxt.visibility = View.GONE
            chatTxt.visibility = View.GONE
            profileTxt.visibility = View.GONE
            favoriteImage.setImageResource(R.drawable.favorite)
            chatImage.setImageResource(R.drawable.chat)
            profileImage.setImageResource(R.drawable.person)
            favoriteLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            chatLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            profileLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            homeTxt.visibility = View.VISIBLE
            homeImage.setImageResource(R.drawable.home_selected)
            homeLayout.setBackgroundColor(R.drawable.round_home_button_ui)
            var scaleAnim = ScaleAnimation(
                0.0f,
                1.0f,
                1.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f
            )
            scaleAnim.duration = 200
            scaleAnim.fillAfter = true
            homeLayout.startAnimation(scaleAnim)
            fragmentManager.beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer,homeFragment::class.java,null).commit()
        }
        else if(viewModel.selectedTab == 2){
            homeTxt.visibility = View.GONE
            chatTxt.visibility = View.GONE
            profileTxt.visibility = View.GONE
            homeImage.setImageResource(R.drawable.home)
            chatImage.setImageResource(R.drawable.chat)
            profileImage.setImageResource(R.drawable.person)
            homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            favoriteLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            chatLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            profileLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            favoriteTxt.visibility = View.VISIBLE
            favoriteImage.setImageResource(R.drawable.favorite_selected)
            favoriteLayout.setBackgroundColor(R.drawable.round_favorite_button_ui)
            var scaleAnim = ScaleAnimation(
                0.0f,
                1.0f,
                1.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f
            )
            scaleAnim.duration = 200
            scaleAnim.fillAfter = true
            favoriteLayout.startAnimation(scaleAnim)
            fragmentManager.beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer,FavoritePropertySelectedFragment::class.java,null).commit()

        }
        else if(viewModel.selectedTab == 3){
            favoriteTxt.visibility = View.GONE
            homeTxt.visibility = View.GONE
            profileTxt.visibility = View.GONE
            favoriteImage.setImageResource(R.drawable.favorite)
            homeImage.setImageResource(R.drawable.home)
            profileImage.setImageResource(R.drawable.person)
            favoriteLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            chatLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            profileLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            chatTxt.visibility = View.VISIBLE
            chatImage.setImageResource(R.drawable.chat_selected)
            chatLayout.setBackgroundColor(R.drawable.round_chat_button_ui)
            var scaleAnim = ScaleAnimation(
                0.0f,
                1.0f,
                1.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f
            )
            scaleAnim.duration = 200
            scaleAnim.fillAfter = true
            chatLayout.startAnimation(scaleAnim)
            fragmentManager.beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer,chatFragment::class.java,null).commit()
        }
        else if(viewModel.selectedTab == 4){
            favoriteTxt.visibility = View.GONE
            chatTxt.visibility = View.GONE
            homeTxt.visibility = View.GONE
            favoriteImage.setImageResource(R.drawable.favorite)
            chatImage.setImageResource(R.drawable.chat)
            homeImage.setImageResource(R.drawable.home)
            favoriteLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            profileLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            chatLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            profileTxt.visibility = View.VISIBLE
            profileImage.setImageResource(R.drawable.person_selected)
            profileLayout.setBackgroundColor(R.drawable.round_person_button_ui)
            var scaleAnim = ScaleAnimation(
                0.0f,
                1.0f,
                1.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f
            )
            scaleAnim.duration = 200
            scaleAnim.fillAfter = true
            profileLayout.startAnimation(scaleAnim)
            fragmentManager.beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer,profileFragment::class.java,null).commit()

        }

        homeLayout.setOnClickListener {
            if (viewModel.selectedTab != 1) {
                favoriteTxt.visibility = View.GONE
                chatTxt.visibility = View.GONE
                profileTxt.visibility = View.GONE
                favoriteImage.setImageResource(R.drawable.favorite)
                chatImage.setImageResource(R.drawable.chat)
                profileImage.setImageResource(R.drawable.person)
                favoriteLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                chatLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                profileLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                homeTxt.visibility = View.VISIBLE
                homeImage.setImageResource(R.drawable.home_selected)
                homeLayout.setBackgroundColor(R.drawable.round_home_button_ui)
                var scaleAnim = ScaleAnimation(
                    0.0f,
                    1.0f,
                    1.0f,
                    1.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f
                )
                scaleAnim.duration = 200
                scaleAnim.fillAfter = true
                homeLayout.startAnimation(scaleAnim)
                viewModel.selectedTab = 1
                fragmentManager.beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer,homeFragment::class.java,null).commit()


            }
        }
        favoriteLayout.setOnClickListener {
            if (viewModel.selectedTab != 2) {
                homeTxt.visibility = View.GONE
                chatTxt.visibility = View.GONE
                profileTxt.visibility = View.GONE
                homeImage.setImageResource(R.drawable.home)
                chatImage.setImageResource(R.drawable.chat)
                profileImage.setImageResource(R.drawable.person)
                homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                favoriteLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                chatLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                profileLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                favoriteTxt.visibility = View.VISIBLE
                favoriteImage.setImageResource(R.drawable.favorite_selected)
                favoriteLayout.setBackgroundColor(R.drawable.round_favorite_button_ui)
                var scaleAnim = ScaleAnimation(
                    0.0f,
                    1.0f,
                    1.0f,
                    1.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f
                )
                scaleAnim.duration = 200
                scaleAnim.fillAfter = true
                favoriteLayout.startAnimation(scaleAnim)
                viewModel.selectedTab = 2
                fragmentManager.beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer,FavoritePropertySelectedFragment::class.java,null).commit()


            }
        }
        chatLayout.setOnClickListener {
            if (viewModel.selectedTab != 3) {
                favoriteTxt.visibility = View.GONE
                homeTxt.visibility = View.GONE
                profileTxt.visibility = View.GONE
                favoriteImage.setImageResource(R.drawable.favorite)
                homeImage.setImageResource(R.drawable.home)
                profileImage.setImageResource(R.drawable.person)
                favoriteLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                chatLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                profileLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                chatTxt.visibility = View.VISIBLE
                chatImage.setImageResource(R.drawable.chat_selected)
                chatLayout.setBackgroundColor(R.drawable.round_chat_button_ui)
                var scaleAnim = ScaleAnimation(
                    0.0f,
                    1.0f,
                    1.0f,
                    1.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f
                )
                scaleAnim.duration = 200
                scaleAnim.fillAfter = true
                chatLayout.startAnimation(scaleAnim)
                viewModel.selectedTab = 3
                fragmentManager.beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer,chatFragment::class.java,null).commit()


            }
        }
        profileLayout.setOnClickListener {
            if (viewModel.selectedTab != 4) {
                favoriteTxt.visibility = View.GONE
                chatTxt.visibility = View.GONE
                homeTxt.visibility = View.GONE
                favoriteImage.setImageResource(R.drawable.favorite)
                chatImage.setImageResource(R.drawable.chat)
                homeImage.setImageResource(R.drawable.home)
                favoriteLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                profileLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                chatLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                profileTxt.visibility = View.VISIBLE
                profileImage.setImageResource(R.drawable.person_selected)
                profileLayout.setBackgroundColor(R.drawable.round_person_button_ui)
                var scaleAnim = ScaleAnimation(
                    0.0f,
                    1.0f,
                    1.0f,
                    1.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f
                )
                scaleAnim.duration = 200
                scaleAnim.fillAfter = true
                profileLayout.startAnimation(scaleAnim)
                viewModel.selectedTab = 4
                fragmentManager.beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer,profileFragment::class.java,null).commit()

            }
        }
    }}

