package com.petrunnel.rickandmorty.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.petrunnel.rickandmorty.R
import com.petrunnel.rickandmorty.RickAndMortyApplication
import com.petrunnel.rickandmorty.databinding.FragmentSearchBinding
import com.petrunnel.rickandmorty.extentions.toastShort
import com.petrunnel.rickandmorty.extentions.viewBinding
import com.petrunnel.rickandmorty.model.Character
import com.petrunnel.rickandmorty.ui.details.DetailsFragment
import com.petrunnel.rickandmorty.ui.main.CharacterAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val adapter: CharacterAdapter by lazy { CharacterAdapter(itemClickListener) }
    lateinit var layoutManager: LinearLayoutManager

    private val itemClickListener = object : CharacterAdapter.OnItemClickListener {
        override fun onItemClick(character: Character) {
            if (RickAndMortyApplication.isNetworkAvailable()) {
                val arguments = Bundle()
                arguments.putInt("CHARACTER_ID", character.id)
                val detailsFragment = DetailsFragment()
                detailsFragment.arguments = arguments
                parentFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out
                    )
                    replace(R.id.main_container, detailsFragment)
                    addToBackStack(null)
                }
            } else {
                requireContext().toastShort(getString(R.string.no_internet))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(requireContext())
        binding.rvMain.layoutManager = layoutManager
        binding.rvMain.adapter = adapter
        viewModel.status.observe(viewLifecycleOwner) {
            adapter.setCharacterList(it)
        }

        binding.rvMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findLastCompletelyVisibleItemPosition() == (viewModel.status.value?.size?.minus(
                        1
                    ) ?: false)
                ) {
                    if (RickAndMortyApplication.isNetworkAvailable())
                        viewModel.loadDataNext()
                    else
                        requireContext().toastShort(getString(R.string.no_internet))
                }
            }
        })

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setName(s.toString())
                if (!RickAndMortyApplication.isNetworkAvailable())
                    requireContext().toastShort(getString(R.string.no_internet))
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        viewModel.status.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }

}