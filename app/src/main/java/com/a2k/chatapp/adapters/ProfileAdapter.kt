package com.a2k.chatapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a2k.chatapp.R
import com.a2k.chatapp.databinding.ProfileViewBinding
import com.a2k.chatapp.generateChatId
import com.a2k.chatapp.models.Profile
import com.a2k.chatapp.screens.ChatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileAdapter(private val context: Context) : RecyclerView.Adapter<ProfileViewHolder>() {

    private var profiles = mutableListOf<Profile>()
    private val uid = Firebase.auth.currentUser?.uid

    fun setProfiles(profiles: List<Profile>) {
        this.profiles = profiles.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProfileViewBinding.inflate(inflater, parent, false)
        return ProfileViewHolder(binding)
    }

    override fun getItemCount(): Int = profiles.size

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles[position]
        holder.binding.profileName.text = profile.name
        if (uid != null && profile.uid != null) {
            val chatId = generateChatId(uid, profile.uid)
            holder.binding.profileCard.setOnClickListener { v ->
                val i = Intent(v.context, ChatActivity::class.java)
                i.putExtra("chatId", chatId)
                i.putExtra("receiverProfile", profile)
                v.context.startActivity(i)
            }
        }
        Glide.with(context)
            .load(profile.photoUrl)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.binding.profileAvatar)

    }

}

class ProfileViewHolder(val binding: ProfileViewBinding) : RecyclerView.ViewHolder(binding.root)