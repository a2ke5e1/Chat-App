package com.a2k.chatapp.adapters

import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.a2k.chatapp.R
import com.a2k.chatapp.databinding.MessageViewBinding
import com.a2k.chatapp.models.Message
import com.a2k.chatapp.models.Profile
import com.google.android.material.color.MaterialColors
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Locale


class MessageAdapter(private val context: Context, private val onMessageDelete: (messageId: String) -> Unit,
                     private val enableMessageEdit: (messageId: String, messageBody: String) -> Unit): RecyclerView.Adapter<MessageViewHolder>() {

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
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        holder.binding.messageSender.text = message.senderId
        if (receiverProfile != null && receiverProfile?.uid.equals(message.senderId)) {
            holder.binding.messageSender.text = receiverProfile?.name
            holder.binding.messageCard.setCardBackgroundColor(
                MaterialColors.getColor(context, com.google.android.material.R.attr.colorSecondaryContainer, Color.TRANSPARENT )
            )
            params.gravity = Gravity.START
        }
        else if (_uid != null && _uid == message.senderId) {
            holder.binding.messageSender.text = "Me"
            holder.binding.messageCard.setCardBackgroundColor(
                MaterialColors.getColor(context, com.google.android.material.R.attr.colorSurfaceContainer, Color.TRANSPARENT )
            )
            params.gravity = Gravity.END
            holder.binding.messageCard.setOnLongClickListener { v->
                showPopup(v, message)
                return@setOnLongClickListener false
            }
        }
        holder.binding.messageBody.text = message.messageBody

        val dateFormatter = SimpleDateFormat("HH:mm · dd/MM/yy", Locale.getDefault()) //  Jul 12, 2023 15:49
        if (message.sentDate != null) {
            holder.binding.messageTimestamp.text = dateFormatter.format(message.sentDate)
        }
        holder.binding.messageCard.layoutParams = params
    }

    private fun showPopup(v: View, message: Message) {
        val popup = PopupMenu(context, v)
        popup.gravity = Gravity.CENTER
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.chat_actions_menu, popup.menu)
        popup.setForceShowIcon(true)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit -> {
                    enableMessageEdit(message.messageId!!, message.messageBody!!)
                    true
                }
                R.id.delete -> {
                    onMessageDelete(message.messageId!!)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

}

class MessageViewHolder(val binding: MessageViewBinding) : RecyclerView.ViewHolder(binding.root)