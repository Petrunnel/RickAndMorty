package com.petrunnel.rickandmorty.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.petrunnel.rickandmorty.R
import com.petrunnel.rickandmorty.RickAndMortyApplication
import com.petrunnel.rickandmorty.databinding.FragmentMainBinding
import com.petrunnel.rickandmorty.extentions.toastShort
import com.petrunnel.rickandmorty.extentions.viewBinding
import com.petrunnel.rickandmorty.model.Character
import com.petrunnel.rickandmorty.ui.details.DetailsFragment
import com.petrunnel.rickandmorty.ui.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels()
    private val binding by viewBinding(FragmentMainBinding::bind)
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

        binding.swipeRefresh.setOnRefreshListener {
            if (RickAndMortyApplication.isNetworkAvailable())
                viewModel.refresh()
            else
                requireContext().toastShort(getString(R.string.no_internet))
            binding.swipeRefresh.isRefreshing = false
        }

        binding.fab.setOnClickListener {
            if (RickAndMortyApplication.isNetworkAvailable()) {
                parentFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out
                    )
                    replace(R.id.main_container, SearchFragment())
                    addToBackStack(null)
                }
            } else {
                requireContext().toastShort(getString(R.string.no_internet))
            }
        }
    }

    override fun onDestroyView() {
        viewModel.status.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }
}