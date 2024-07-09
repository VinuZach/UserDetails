package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.apiManager.model.User
import com.example.myapplication.apiManager.model.UserResponseData

val DUMMY_USER = User(id = 0, maidenName = "")

class MainActivity : AppCompatActivity()
{
    lateinit var messageTextView: TextView
    lateinit var userRecyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
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
        messageTextView.text = getString(R.string.retrieving_data)


        retrieveUserData(true)

        initAdapter()
        initScrollListener()

    }

    private fun retrieveUserData(isFirstTime: Boolean)
    {
        if (!isFirstTime)
        {
            Log.e("asdsadsd", "onScrolled:contains  ${userDetailsList.contains(DUMMY_USER)}")
            if (!userDetailsList.contains(DUMMY_USER))
            {
                try
                {
                    userDetailsList.add(DUMMY_USER)
                    userRecyclerView.recycledViewPool.clear()
                    Log.e("asdsadsd", "onScrolled:contains  ${userDetailsList.contains(null)}")
                    userDetailsListAdapter?.notifyDataSetChanged()
                } catch (e: Exception)
                {
                    e.printStackTrace()
                }


            }
        }
        Log.e("asdsadsd", "retreiveUserData: ")
        mainActivityViewModel.retrieveUserList(limitValue, skipValue, onResponseObtained = object : OnResponseObtained
        {
            override fun onResponse(isSuccess: Boolean, responseData: Any?)
            {
                messageTextView.visibility = View.GONE

                isLoading = false
                if (isSuccess)
                {

                    val userResponseData = responseData as UserResponseData
                    skipValue += limitValue
                    userResponseData.users?.let {
                        try
                        {
                            if (userDetailsList.contains(DUMMY_USER)) userDetailsList.remove(DUMMY_USER)
                            userDetailsList.addAll(it)

                            Log.e("asdsadsd", "${userDetailsList.size}: ")
                            userRecyclerView.recycledViewPool.clear()
                            userDetailsListAdapter?.notifyItemInserted(userDetailsList.size - 1)
                            if (skipValue == (limitValue * 2)) userRecyclerView.scrollToPosition(skipValue)
                            isLoading = false
                        } catch (e: Exception)
                        {
                            e.printStackTrace()
                        }

                    }

                }
                else
                {
                    if (userDetailsList.size == 0)
                    {
                        messageTextView.visibility = View.VISIBLE
                        messageTextView.text = (responseData as String)
                    }
                    else
                    {
                        if (userDetailsList.contains(DUMMY_USER)) userDetailsList.remove(DUMMY_USER)
                        userRecyclerView.recycledViewPool.clear()
                        userDetailsListAdapter?.notifyDataSetChanged()
                    }
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
        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
        swipeRefreshLayout.setOnRefreshListener {

            limitValue = 10
            skipValue = 0
            userDetailsList.clear()
            userDetailsListAdapter?.notifyDataSetChanged()
            messageTextView.visibility = View.VISIBLE
            messageTextView.text = getString(R.string.retrieving_data)
            retrieveUserData(true)
            swipeRefreshLayout.isRefreshing = false
        }
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

                try
                {
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
                } catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
        })
    }

}

