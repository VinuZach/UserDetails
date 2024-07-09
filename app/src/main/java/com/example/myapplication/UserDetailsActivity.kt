package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myapplication.apiManager.model.User

class UserDetailsActivity : AppCompatActivity()
{
    lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var progressBar: ProgressBar
    lateinit var userDetailsParentScrollView: ScrollView


    lateinit var userImageView: ImageView
    lateinit var userNameTextView: TextView
    lateinit var eyeColorTextView: TextView
    lateinit var hairTextView: TextView
    lateinit var userBirthDayTextView: TextView
    lateinit var emailTextView: TextView
    lateinit var heightTextView: TextView
    lateinit var weightTextView: TextView
    lateinit var phoneTextView: TextView
    lateinit var accountUsernameTextView: TextView
    lateinit var bloodTypeTextView: TextView
    lateinit var accountPasswordTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        init()
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        retrieveUserDetailsById(intent.extras!!.getInt("UserId", 0))
    }

    fun init(): Unit
    {
        progressBar = findViewById(R.id.progressBar)
        userDetailsParentScrollView = findViewById(R.id.userdetails)
        userNameTextView = findViewById(R.id.name)
        eyeColorTextView = findViewById(R.id.eyecolor)
        hairTextView = findViewById(R.id.hair)
        userBirthDayTextView = findViewById(R.id.birthday)
        bloodTypeTextView = findViewById(R.id.bloodtype)
        emailTextView = findViewById<TextView>(R.id.email)
        heightTextView = findViewById<TextView>(R.id.height)
        weightTextView = findViewById<TextView>(R.id.weight)
        phoneTextView = findViewById<TextView>(R.id.phone)
        accountUsernameTextView = findViewById<TextView>(R.id.account_username)
        accountPasswordTextView = findViewById<TextView>(R.id.account_password)
        userImageView = findViewById<ImageView>(R.id.userimage)
    }

    private fun retrieveUserDetailsById(userId: Int)
    {
        progressBar.visibility = View.VISIBLE
        userDetailsParentScrollView.visibility = View.GONE
        mainActivityViewModel.retrieveUserDetailsById(userId, object : OnResponseObtained
        {

            override fun onResponse(isSuccess: Boolean, responseData: Any?)
            {
                progressBar.visibility = View.GONE
                userDetailsParentScrollView.visibility = View.VISIBLE
                Log.d("asdasdsad", "onResponse: $responseData")
                if (isSuccess)
                {
                    val userDetails = responseData as User

                    val name =
                        userDetails.firstName + " " + (if (userDetails.maidenName.isNotEmpty()) (userDetails.maidenName.first() + " ") else "") + userDetails.lastName
                    userNameTextView.text = name
                    eyeColorTextView.text = buildString {
                        append("Eye Color")
                        append(" : ")
                        append(userDetails.eyeColor)
                    }

                    hairTextView.text = buildString {
                        append("Hair ")
                        append(" : ")
                        append(userDetails.hair!!.color)
                        append(" ")
                        append(userDetails.hair!!.type)
                    }
                    emailTextView.text = userDetails.email
                    heightTextView.text = buildString {
                        append("Height")
                        append(" : ")
                        append(userDetails.height)
                    }

                    weightTextView.text = buildString {
                        append("Weight")
                        append(" : ")
                        append(userDetails.weight)
                    }
                    phoneTextView.text = userDetails.phone
                    accountUsernameTextView.text = userDetails.username
                    bloodTypeTextView.text = buildString {
                        append("Blood Group")
                        append(" : ")
                        append(userDetails.bloodGroup)
                    }
                    userBirthDayTextView.text = buildString {
                        append(userDetails.birthDate)
                        append(" (")
                        append(userDetails.age)
                        append(")")
                    }
                    userDetails.password?.let { password ->
                        accountPasswordTextView.text = password.replaceRange(2..<password.length, "****")
                    }

                    val defaultIcon = if (userDetails.gender == "female") R.drawable.female_default_icon else R.drawable.male_default_icon
                    Glide.with(this@UserDetailsActivity).load(userDetails.image).placeholder(defaultIcon).into(userImageView);
                }
            }

        })
    }
}