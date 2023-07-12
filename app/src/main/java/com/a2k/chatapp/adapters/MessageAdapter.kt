package com.a2k.chatapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a2k.chatapp.databinding.MessageViewBinding
import com.a2k.chatapp.models.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MessageAdapter: RecyclerView.Adapter<MessageViewHolder>() {

    private var messages = mutableListOf<Message>()
    private val _uid = Firebase.auth.currentUser?.uid

    fun setMessages(movies: List<Message>) {
        this.messages = movies.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MessageViewBinding.inflate(inflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        holder.binding.messageSender.text = if (message.senderId.equals(_uid.toString()) ) { "Me"} else {"Other"}

        holder.binding.messageBody.text = message.messageBody
        holder.binding.messageTimestamp.text = message.sentDate.toString()
    }

}

class MessageViewHolder(val binding: MessageViewBinding) : RecyclerView.ViewHolder(binding.root)