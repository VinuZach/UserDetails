package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.customlibrary.A1RecyclerAdapter
import com.example.myapplication.apiManager.model.User
import com.example.myapplication.apiManager.model.UserResponseData


class MainActivity : AppCompatActivity()
{
    lateinit var messageTextView: TextView
    lateinit var userRecyclerView: RecyclerView

    var limitValue = 10
    var skipValue = 0
    lateinit var mainActivityViewModel: MainActivityViewModel

    lateinit var userDetailsAdapter: A1RecyclerAdapter<User>
    var userDetailsList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        messageTextView.visibility = View.GONE
        retreiveUserData(true)


    }

    private fun retreiveUserData(isFirstTime: Boolean)
    {

        mainActivityViewModel.retrieveUserList(limitValue, skipValue, onResponseObtained = object : OnResponseObtained
        {
            override fun onResponse(isSuccess: Boolean, responseData: Any?)
            {

                isLoading = false;
                if (isSuccess)
                {
                    val userResponseData = responseData as UserResponseData
                    skipValue += limitValue
                    userResponseData.users?.let {
                        userDetailsList.addAll(it)
                        displayUserData(userDetailsList, isFirstTime)
                    }

                }
                else
                {
                    messageTextView.visibility = View.VISIBLE
                    messageTextView.text = (responseData as String)
                }
            }

        })
    }

    var isLoading = false

    @SuppressLint("NotifyDataSetChanged")
    private fun displayUserData(userDetailsList: List<User>, isFirstTime: Boolean)
    {
        if (!isFirstTime)
        {
            userDetailsAdapter.notifyDataSetChanged()
            return
        }
        userDetailsAdapter = object : A1RecyclerAdapter<User>(this@MainActivity, userDetailsList)
        {
            override val layoutResourceId: Int
                get() = R.layout.item_user_short_details


            override fun onDataBind(model: User, position: Int, holder: RecyclerView.ViewHolder?)
            {
                holder?.let {
                    val userNameTextView = holder.itemView.findViewById<TextView>(R.id.name)
                    val emailTextView = holder.itemView.findViewById<TextView>(R.id.email)
                    val userImageView = holder.itemView.findViewById<ImageView>(R.id.imageView)
                    val name =
                        model.firstName + " " + (if (model.maidenName.isNotEmpty()) (model.maidenName.first() + " ") else "") + model.lastName
                    userNameTextView.text = name
                    emailTextView.text = model.email
                    val defaultIcon = if (model.gender == "female") R.drawable.female_default_icon else R.drawable.male_default_icon
                    Glide.with(this@MainActivity).load(model.image).placeholder(defaultIcon).into(userImageView);
                    holder.itemView.setOnClickListener {
                        val userDetailsIntent = Intent(this@MainActivity, UserDetailsActivity::class.java)
                        userDetailsIntent.putExtra("UserId", model.id)
                        startActivity(userDetailsIntent)
                    }
                }

            }

        }


        userRecyclerView.apply {
            setAdapter(userDetailsAdapter)
        }

        userRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
            {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!isLoading)
                {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == userDetailsList.size - 1)
                    {


                        retreiveUserData(false)
                        isLoading = true;
                    }
                }
            }
        })
    }


    fun init()
    {
        messageTextView = findViewById(R.id.message)
        userRecyclerView = findViewById(R.id.recyclerview)

        Log.d("asdsadsad", "onDataBind: $isLoading")

    }
}

