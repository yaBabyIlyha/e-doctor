package com.example.edoctor.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edoctor.API.ChatItem
import com.example.edoctor.R

class ChatAdapter(
    private var chatList: List<ChatItem>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserLogin: TextView = view.findViewById(R.id.tvUserLogin)
        val tvLastMessage: TextView = view.findViewById(R.id.tvLastMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.tvUserLogin.text = chat.userLogin
        holder.tvLastMessage.text = chat.lastMessage
        holder.itemView.setOnClickListener {
            onClick(chat.userLogin)
        }
    }

    fun updateChats(newChats: List<ChatItem>) {
        chatList = newChats
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = chatList.size
}
