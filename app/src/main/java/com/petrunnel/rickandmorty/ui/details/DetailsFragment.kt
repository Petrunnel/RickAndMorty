package com.petrunnel.rickandmorty.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.petrunnel.rickandmorty.R
import com.petrunnel.rickandmorty.databinding.FragmentDetailsBinding
import com.petrunnel.rickandmorty.extentions.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val detailsViewModel: DetailsViewModel by viewModels()
    private val binding by viewBinding(FragmentDetailsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val id = it.getInt("CHARACTER_ID")
            detailsViewModel.setCharacterIdAndGetData(id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsViewModel.status.observe(viewLifecycleOwner) {
            with(binding) {
                tvName.text = it.name
                val status = "Status: ${it.status}"
                tvStatus.text = status
                val species = "Species: ${it.species}"
                tvSpecies.text = species
                val gender = "Gender: ${it.gender}"
                tvGender.text = gender
                val originName = "Origin: ${it.origin.name}"
                tvOrigin.text = originName
                val location = "Location: ${it.location.name}"
                tvLocation.text = location
                Glide.with(ivAvatar.context)
                    .load(it.image)
                    .placeholder(R.drawable.placeholder_avatar)
                    .into(ivAvatar)
            }
        }
    }
}
