package com.example.myapplication


import androidx.lifecycle.ViewModel
import com.example.myapplication.apiManager.ApiManager
import com.example.myapplication.apiManager.model.User
import com.example.myapplication.apiManager.model.UserResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel : ViewModel()
{

    /**
     * retrieves list of users
     */
    fun retrieveUserList(limitValue: Int, skipValue: Int, onResponseObtained: OnResponseObtained)
    {
        ApiManager().createManagerInstance().getUserList(limitValue, skipValue).enqueue(object : Callback<UserResponseData>
        {
            override fun onResponse(p0: Call<UserResponseData>, p1: Response<UserResponseData>)
            {

                if (p1.isSuccessful)
                {
                    p1.body()?.let { userResponseData ->
                        if (userResponseData.total > 0) onResponseObtained.onResponse(true, userResponseData)
                        else onResponseObtained.onResponse(false, "no user data")
                    } ?: run {

                        onResponseObtained.onResponse(false, "no user data")
                    }
                }
            }

            override fun onFailure(p0: Call<UserResponseData>, p1: Throwable)
            {
                onResponseObtained.onResponse(false, "no response from server")
            }

        })
    }

    /**
     * retrieves details of specific user of given id
     */
    fun retrieveUserDetailsById(userId: Int, onResponseObtained: OnResponseObtained)
    {
        ApiManager().createManagerInstance().getUserDetailsByID(userId).enqueue(object : Callback<User>
        {
            override fun onResponse(p0: Call<User>, p1: Response<User>)
            {
                if (p1.isSuccessful)
                {
                    p1.body()?.let { userResponseData ->
                        onResponseObtained.onResponse(true, userResponseData)
                    } ?: run {

                        onResponseObtained.onResponse(false, "no user data")
                    }
                }
            }

            override fun onFailure(p0: Call<User>, p1: Throwable)
            {
                onResponseObtained.onResponse(false, "no response from server")
            }

        })
    }

}

interface OnResponseObtained
{
    fun onResponse(isSuccess: Boolean, responseData: Any?)
}