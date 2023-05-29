package com.petrunnel.rickandmorty.ui.main

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.petrunnel.rickandmorty.R
import com.petrunnel.rickandmorty.databinding.ItemCharacterBinding
import com.petrunnel.rickandmorty.extentions.inflateBinding
import com.petrunnel.rickandmorty.model.Character


class CharacterAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private var characterList = mutableListOf<Character>()

    interface OnItemClickListener {

        fun onItemClick(character: Character)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(parent.inflateBinding(ItemCharacterBinding::inflate), listener)
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characterList[position]
        holder.bind(character, position)
    }

    fun setCharacterList(updatedCharacterList: List<Character>) {
        val diffResult = DiffUtil.calculateDiff(
            CharacterDiffUtilCallback(
                characterList, updatedCharacterList
            )
        )
        characterList.clear()
        characterList.addAll(updatedCharacterList)
        diffResult.dispatchUpdatesTo(this)
    }

    class CharacterViewHolder(
        private val binding: ItemCharacterBinding,
        private val listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun bind(character: Character, position: Int) {
            with(binding) {

                tvName.text = character.name

                Glide.with(ivAvatar.context)
                    .load(character.image)
                    .placeholder(R.drawable.placeholder_avatar)
                    .into(ivAvatar)

                root.setOnClickListener {
                    listener.onItemClick(character)
                }
            }
        }
    }

    class CharacterDiffUtilCallback(
        private val oldList: MutableList<Character>, private val newList: List<Character>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}