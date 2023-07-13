package com.a2k.chatapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.a2k.chatapp.databinding.ProfileViewBinding
import com.a2k.chatapp.models.Profile

class ProfileAdapter : RecyclerView.Adapter<ProfileViewHolder>() {

    private var profiles = mutableListOf<Profile>()

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
    }

}

class ProfileViewHolder(val binding: ProfileViewBinding) : RecyclerView.ViewHolder(binding.root)