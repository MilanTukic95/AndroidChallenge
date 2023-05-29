package com.example.androidchallenge.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchallenge.R
import com.example.androidchallenge.data.model.LaunchDetail
import com.example.androidchallenge.data.model.QueryFilter
import com.example.androidchallenge.data.model.QuerySearch
import com.example.androidchallenge.data.model.Search
import com.example.androidchallenge.databinding.FragmentLaunchesBinding
import com.example.androidchallenge.ui.activity.MainActivity
import com.example.androidchallenge.ui.fragments.adapter.LaunchesAdapter
import com.example.androidchallenge.ui.fragments.viewmodels.LaunchesViewModel
import com.example.androidchallenge.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class LaunchesFragment : Fragment(), LaunchesAdapter.OnItemClickListener{

    private var _binding: FragmentLaunchesBinding? = null
    private val viewModel: LaunchesViewModel by viewModels()
    private lateinit var adapter: LaunchesAdapter
    private val binding get() = _binding!!
    private var recyclerViewState : Parcelable? = null
    private lateinit var searchView : SearchView
    private var queryFilter: QueryFilter? = null
    private var querySearch: QuerySearch? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLaunchesBinding.inflate(inflater, container, false)

        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context)
        binding.rvLaunches.layoutManager = layoutManager

        viewModel.getLaunches(null, null)
        requireActivity().title = context?.resources?.getString(R.string.label_launches)


        binding.rvLaunches.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!binding.pbLoading.isVisible && viewModel.hasNextPage && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                    recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
                    viewModel.getLaunches(queryFilter, querySearch)
                }
            }
        })

        viewModel.response.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Resource.Success -> {
                    binding.pbLoading.visibility = View.GONE

                    if(it.data != null) {
                        adapter = LaunchesAdapter(it.data)
                    }
                    adapter.setOnItemClickListener(this)
                    binding.rvLaunches.adapter = adapter
                    binding.rvLaunches.layoutManager?.onRestoreInstanceState(recyclerViewState) // restore recycleView state

                }
                is Resource.Error -> {
                    binding.pbLoading.visibility = View.GONE

                    Toast.makeText(context,it.message.toString(), Toast.LENGTH_LONG ).show()
                }
                is Resource.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: LaunchDetail) {
        (activity as MainActivity).openDetailFragment(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider, SearchView.OnQueryTextListener {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(R.menu.main_menu, menu)

                val search = menu.findItem(R.id.actionSearch)
                searchView = search.actionView as SearchView

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionSearch -> {
                        searchView.setOnQueryTextListener(this)
                        true
                    }
                    R.id.actionAll -> {
                        clearData()
                        viewModel.getLaunches(null, null)
                        true
                    }
                    R.id.actionSuccess -> {
                        clearData()
                        queryFilter = QueryFilter(success = true, upcoming = null)
                        viewModel.getLaunches(queryFilter, null)
                        true
                    }
                    R.id.actionUpcoming -> {
                        clearData()
                        queryFilter = QueryFilter(success = null, upcoming = true)
                        viewModel.getLaunches(queryFilter, null)
                        true
                    }
                    R.id.actionFailure -> {
                        clearData()
                        queryFilter = QueryFilter(success = false, upcoming = false)
                        viewModel.getLaunches(queryFilter, null)
                        true
                    }
                    else -> false
                }
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                clearData()
                val search = newText?.let { Search(it) }
                val searchQuery = search?.let { QuerySearch(it) }
                viewModel.getLaunches(
                    null, if (newText.isNullOrEmpty()) {
                        null
                    } else {
                        searchQuery
                    }
                )
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun clearData(){
        queryFilter = null
        querySearch = null
        viewModel.currentPage.set(1)
        viewModel.launcesList.clear()
    }
}