package com.a2k.chatapp.adapters

import android.icu.text.SimpleDateFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.a2k.chatapp.databinding.MessageViewBinding
import com.a2k.chatapp.models.Message
import com.a2k.chatapp.models.Profile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Locale

class MessageAdapter: RecyclerView.Adapter<MessageViewHolder>() {

    private var messages = mutableListOf<Message>()
    private val _uid = Firebase.auth.currentUser?.uid
    private var receiverProfile: Profile? = null

    fun setReceiverInfo(profile: Profile?) {
        receiverProfile = profile
    }

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

        holder.binding.messageSender.text = message.senderId
        if (receiverProfile != null && receiverProfile?.uid.equals(message.senderId)) {
            holder.binding.messageSender.text = receiverProfile?.name
        } else if (_uid != null && _uid == message.senderId) {
            holder.binding.messageSender.text = "Me"
        }


        holder.binding.messageBody.text = message.messageBody

        val dateFormatter = SimpleDateFormat("LLL d, y H:mm", Locale.getDefault()) //  Jul 12, 2023 15:49
        if (message.sentDate != null) {
            holder.binding.messageTimestamp.text = dateFormatter.format(message.sentDate)
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = if (message.senderId.equals(_uid.toString()) ) { Gravity.END} else {Gravity.START}
        }
        holder.binding.messageCard.layoutParams = params
    }

}

class MessageViewHolder(val binding: MessageViewBinding) : RecyclerView.ViewHolder(binding.root)