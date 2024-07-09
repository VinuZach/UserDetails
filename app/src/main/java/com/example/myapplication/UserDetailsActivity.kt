package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.myapplication.apiManager.model.User

class UserDetailsActivity : AppCompatActivity()
{
    lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var progressBar: ProgressBar
    lateinit var userDetailsParentScrollView: ConstraintLayout
    lateinit var messageTextView: TextView


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
    lateinit var genderImageView: ImageView
    lateinit var addressTextView: TextView
    lateinit var homeTextView: TextView
    lateinit var companyTextView: TextView
    lateinit var bankTextView: TextView

    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_details)
        init()
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        retrieveUserDetailsById(intent.extras!!.getInt("UserId", 1))

    }

    fun init()
    {
        progressBar = findViewById(R.id.progressBar)
        userDetailsParentScrollView = findViewById(R.id.userdetails)
        messageTextView = findViewById(R.id.message)
        userNameTextView = findViewById(R.id.name)
        eyeColorTextView = findViewById(R.id.eyecolor)
        hairTextView = findViewById(R.id.hair)
        userBirthDayTextView = findViewById(R.id.birthday)
        bloodTypeTextView = findViewById(R.id.bloodtype)
        emailTextView = findViewById(R.id.email)
        heightTextView = findViewById(R.id.height)
        weightTextView = findViewById(R.id.weight)
        phoneTextView = findViewById(R.id.phone)
        accountUsernameTextView = findViewById(R.id.account_username)
        accountPasswordTextView = findViewById(R.id.account_password)
        genderImageView = findViewById(R.id.gender)
        homeTextView = findViewById(R.id.home)
        companyTextView = findViewById(R.id.company)
        bankTextView = findViewById(R.id.bank)
        addressTextView = findViewById(R.id.address)
        userImageView = findViewById(R.id.userimage)

        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
        swipeRefreshLayout.setOnRefreshListener {

            retrieveUserDetailsById(intent.extras!!.getInt("UserId", 1))
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun retrieveUserDetailsById(userId: Int)
    {
        progressBar.visibility = View.VISIBLE
        messageTextView.visibility = View.GONE
        userDetailsParentScrollView.visibility = View.GONE
        mainActivityViewModel.retrieveUserDetailsById(userId, object : OnResponseObtained
        {

            override fun onResponse(isSuccess: Boolean, responseData: Any?)
            {
                progressBar.visibility = View.GONE


                if (isSuccess)
                {
                    userDetailsParentScrollView.visibility = View.VISIBLE
                    messageTextView.visibility = View.GONE
                    val userDetails = responseData as User

                    val name =
                        userDetails.firstName + " " + (if (userDetails.maidenName.isNotEmpty()) (userDetails.maidenName.first() + " ") else "") + userDetails.lastName
                    userNameTextView.text = name
                    eyeColorTextView.text = buildString {

                        append(userDetails.eyeColor)
                    }

                    homeTextView.setOnClickListener {


                        homeTextView.setTextColor(Color.RED)
                        bankTextView.setTextColor(Color.BLACK)
                        companyTextView.setTextColor(Color.BLACK)
                        addressTextView.text = buildString {
                            userDetails.address?.let { address ->
                                append(address.address)
                                append(",")
                                append(address.city)
                                append(",")
                                append(address.state)
                                append(",")
                                append(address.country)
                            }

                        }
                    }
                    homeTextView.performClick()
                    companyTextView.setOnClickListener {
                        companyTextView.setTextColor(Color.RED)
                        bankTextView.setTextColor(Color.BLACK)
                        homeTextView.setTextColor(Color.BLACK)
                        addressTextView.text = buildString {
                            userDetails.company?.let { company ->
                                append(company.name)
                                append("\n")
                                append(company.title)
                                append("\n")
                                append(company.department)
                                append("\n\n")
                                company.address?.let { companyAddress ->
                                    append(companyAddress.address)
                                    append(",")
                                    append(companyAddress.city)
                                    append(",")
                                    append(companyAddress.state)
                                    append(",")
                                    append(companyAddress.country)
                                }

                            }

                        }
                    }
                    bankTextView.setOnClickListener {
                        homeTextView.setTextColor(Color.BLACK)
                        companyTextView.setTextColor(Color.BLACK)
                        bankTextView.setTextColor(Color.RED)


                        addressTextView.text = buildString {
                            userDetails.bank?.let { bankDetails ->
                                append(bankDetails.cardNumber)
                                append("\n")
                                append(bankDetails.cardType)
                                append("\n")
                                append(bankDetails.cardExpire)
                                append(",")
                                append(bankDetails.currency)
                            }

                        }
                    }
                    val genderIconImage = if (userDetails.gender == "female") R.drawable.baseline_female_24 else R.drawable.baseline_male_24
                    hairTextView.text = buildString {

                        append(userDetails.hair!!.color)
                        append(" ")
                        append(userDetails.hair!!.type)
                    }
                    emailTextView.text = userDetails.email
                    heightTextView.text = buildString {

                        append(userDetails.height)
                    }

                    weightTextView.text = buildString {

                        append(userDetails.weight)
                    }
                    phoneTextView.text = userDetails.phone
                    accountUsernameTextView.text = userDetails.username
                    bloodTypeTextView.text = buildString {
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
                    Glide.with(this@UserDetailsActivity).load(userDetails.image).placeholder(defaultIcon).into(userImageView)
                    Glide.with(this@UserDetailsActivity).load(genderIconImage).into(genderImageView)

                }
                else
                {
                    userDetailsParentScrollView.visibility = View.GONE
                    messageTextView.visibility = View.VISIBLE
                    messageTextView.setText((responseData as String))
                }
            }

        })
    }
}