package com.kharismarizqii.endlessrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kharismarizqii.endlessrecyclerview.model.Data
import kotlinx.android.synthetic.main.item_user.view.*

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(){

    private var list = ArrayList<Data>()

    inner class UsersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(data: Data){
            with(itemView){
                tvEmail.text = data.email
                val name = data.first_name + data.last_name
                tvName.text = name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UsersViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addList(items: ArrayList<Data>){
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }
}