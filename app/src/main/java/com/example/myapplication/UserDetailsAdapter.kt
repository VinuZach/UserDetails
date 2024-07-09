package com.example.myapplication


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.apiManager.model.User


const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_LOADING = 1


class UserDetailsAdapter(private var userDetailsList: List<User?>?, private var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        if (viewType == VIEW_TYPE_ITEM)
        {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user_short_details, parent, false)
            return ItemViewHolder(view)
        }
        else
        {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
            return ProgressBarViewHolder(view)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        if (holder is ItemViewHolder)
        {
            displayUserDetails(holder, position)
        }

    }


    override fun getItemCount(): Int
    {
        return if (userDetailsList == null) 0 else userDetailsList!!.size
    }


    override fun getItemViewType(position: Int): Int
    {
        return if (userDetailsList!![position]==DUMMY_USER) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        val userNameTextView: TextView = itemView.findViewById(R.id.name)
        val emailTextView: TextView = itemView.findViewById<TextView>(R.id.email)
        val userImageView: ImageView = itemView.findViewById(R.id.imageView)

    }

    private class ProgressBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    private fun displayUserDetails(viewHolder: ItemViewHolder, position: Int)
    {
        userDetailsList?.let { userDetails ->
            userDetails[position]?.let {
                val item = it
                val name =
                    item.firstName + " " + (if (item.maidenName.isNotEmpty()) (item.maidenName.first() + " ") else "") + item.lastName
                viewHolder.userNameTextView.text = name
                viewHolder.emailTextView.text = item.email
                val defaultIcon = if (item.gender == "female") R.drawable.female_default_icon else R.drawable.male_default_icon
                Glide.with(context).load(item.image).placeholder(defaultIcon).into(viewHolder.userImageView)
                viewHolder.itemView.setOnClickListener {
                    val userDetailsIntent = Intent(context, UserDetailsActivity::class.java)
                    userDetailsIntent.putExtra("UserId", item.id)
                    context.startActivity(userDetailsIntent)
                }
            }
        }


    }
}