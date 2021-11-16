package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.FragmentElectionItemBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return from(parent)
    }

    //TODO: Bind ViewHolder

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val electionItem = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(electionItem)
        }
        holder.bind(electionItem)
    }
    //TODO: Add companion object to inflate ViewHolder (from)


companion object {
    fun from(parent: ViewGroup):ElectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentElectionItemBinding.inflate(layoutInflater, parent, false)
        return ElectionViewHolder(binding)
    }
}

    class ElectionViewHolder(private var binding: FragmentElectionItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(election: Election) {
            binding.election = election
            binding.executePendingBindings()

        }

    }}
class ElectionListener(val clickListner: (election: Election)->Unit){
    fun onClick(election: Election)=clickListner(election)
}


object ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }
}

//TODO: Create ElectionViewHolder

//TODO: Create ElectionDiffCallback

//TODO: Create ElectionListener