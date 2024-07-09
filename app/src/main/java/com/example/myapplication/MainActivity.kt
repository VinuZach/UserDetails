package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.apiManager.model.User
import com.example.myapplication.apiManager.model.UserResponseData


class MainActivity : AppCompatActivity()
{
    lateinit var messageTextView: TextView
    lateinit var userRecyclerView: RecyclerView

    var limitValue = 10
    var skipValue = 0
    private lateinit var mainActivityViewModel: MainActivityViewModel


    var userDetailsList: MutableList<User?> = mutableListOf()

    var userDetailsListAdapter: UserDetailsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        messageTextView.visibility = View.VISIBLE
        messageTextView.text= getString(R.string.retrieving_data)


        retrieveUserData(true)

        initAdapter()
        initScrollListener()

    }

    private fun retrieveUserData(isFirstTime: Boolean)
    {
        if (!isFirstTime)
        {
            userDetailsList.add(null)
            userDetailsListAdapter?.notifyItemInserted(userDetailsList.size - 1)


            val scrollPosition = userDetailsList.size - 1
            userDetailsListAdapter?.notifyItemRemoved(scrollPosition)


        }
        Log.e("asdsadsd", "retreiveUserData: ")
        mainActivityViewModel.retrieveUserList(limitValue, skipValue, onResponseObtained = object : OnResponseObtained
        {
            override fun onResponse(isSuccess: Boolean, responseData: Any?)
            {
                messageTextView.visibility = View.GONE
                if (!isFirstTime) userDetailsList.removeAt(userDetailsList.size - 1)
                isLoading = false
                if (isSuccess)
                {

                    val userResponseData = responseData as UserResponseData
                    skipValue += limitValue
                    userResponseData.users?.let {
                        userDetailsList.addAll(it)

                        Log.e("asdsadsd", "${userDetailsList.size}: ")
                        userDetailsListAdapter?.notifyItemInserted(userDetailsList.size - 1)
                        if (skipValue == (limitValue * 2)) userRecyclerView.scrollToPosition(skipValue)
                        isLoading = false

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


    fun init()
    {
        messageTextView = findViewById(R.id.message)
        userRecyclerView = findViewById(R.id.recyclerview)

        Log.d("asdsadsad", "onDataBind: $isLoading")

    }

    private fun initAdapter()
    {
        userDetailsListAdapter = UserDetailsAdapter(userDetailsList, this)
        userRecyclerView.setAdapter(userDetailsListAdapter)
    }


    private fun initScrollListener()
    {
        userRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int)
            {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
            {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                Log.d("asdsadsd", "onScrolled: $isLoading   ...${linearLayoutManager?.findLastCompletelyVisibleItemPosition()}")
                Log.d("asdsadsd", "onScrolled: ${userDetailsList.size}")
                if (!isLoading)
                {

                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == userDetailsList.size - 1)
                    {

                        retrieveUserData(false)
                        isLoading = true
                    }
                }
            }
        })
    }

}

