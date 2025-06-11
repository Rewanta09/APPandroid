package com.example.mygragment.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mygragment.Entity
import com.example.mygragment.databinding.ItemEntityBinding

class EntityAdapter(
    private val entities: List<Entity>,
    private val onItemClick: (Entity) -> Unit // Entity no longer nullable
) : RecyclerView.Adapter<EntityAdapter.EntityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val binding = ItemEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        val entity = entities[position]
        holder.bind(entity)
        holder.itemView.setOnClickListener { onItemClick(entity) }
    }

    override fun getItemCount(): Int = entities.size

    class EntityViewHolder(private val binding: ItemEntityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: Entity) {
            binding.apply {
                tvSpecies.text = entity.species
                // Removed: tvScientificName.text = entity.scientificName
                tvHabitat.text = entity.habitat
                tvDiet.text = entity.diet
                tvConservationStatus.text = entity.conservationStatus
                tvAverageLifespan.text = "Avg Lifespan: ${entity.averageLifespan} years"
            }
        }
    }
}