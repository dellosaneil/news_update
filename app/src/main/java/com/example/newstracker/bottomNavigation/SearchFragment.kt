package com.example.newstracker.bottomNavigation

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstracker.Constants.Companion.SEARCH_DETAILS_DIALOG
import com.example.newstracker.FragmentLifecycleLogging
import com.example.newstracker.R
import com.example.newstracker.callbackListener.SearchPreferenceSwipeListener
import com.example.newstracker.databinding.FragmentSearchBinding
import com.example.newstracker.dialog.SearchPreferenceDialog
import com.example.newstracker.recyclerView.RecyclerViewDecorator
import com.example.newstracker.recyclerView.SearchPreferenceAdapter
import com.example.newstracker.room.entity.PreferenceEntity
import com.example.newstracker.viewModel.searchPreference.SearchPreferenceVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SearchFragment : FragmentLifecycleLogging(), SearchPreferenceAdapter.OnItemClickedListener,
    SearchPreferenceSwipeListener.DeleteSwipe, View.OnClickListener,
    SearchView.OnQueryTextListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchPreferenceVM: SearchPreferenceVM by viewModels()
    private lateinit var myAdapter: SearchPreferenceAdapter
    private lateinit var navController: NavController
    private lateinit var preferencesList: List<PreferenceEntity>

    private var latestSearch = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        val itemSwipeListener = SearchPreferenceSwipeListener(this)
        val itemTouchHelper = ItemTouchHelper(itemSwipeListener)
        itemTouchHelper.attachToRecyclerView(binding.searchFragmentRecyclerView)
        navController = Navigation.findNavController(view)
        binding.searchFragmentFloatBar.setOnClickListener(this)
        initializeSearchView()
    }

    private fun initializeSearchView() {
        val searchVal = binding.searchFragmentToolbar.menu.findItem(R.id.searchView_menu)
        val searchView = searchVal?.actionView as? SearchView
        searchView?.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@SearchFragment)
        }
    }

    private fun initializeRecyclerView() {
        myAdapter = SearchPreferenceAdapter(this)
        binding.searchFragmentRecyclerView.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            val customDecorator = RecyclerViewDecorator(6, 6)
            addItemDecoration(customDecorator)
            searchPreferenceVM.preferences
                .observe(viewLifecycleOwner, {
                    myAdapter.setSearchPreferences(it)
                    preferencesList = it
                })
        }
    }

    override fun searchBreakingNews(pref: PreferenceEntity) {
        val action = SearchFragmentDirections.searchPreferencesNewsArticles(pref)
        Navigation.findNavController(requireView()).navigate(action)
    }

    override fun preferenceDetails(pref: PreferenceEntity) {
        val dialog = SearchPreferenceDialog(pref)
        dialog.show(parentFragmentManager, SEARCH_DETAILS_DIALOG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkDelete(index: Int) {
        val preference = preferencesList[index]
        MaterialAlertDialogBuilder(requireContext())
            .apply {
                setTitle(resources.getString(R.string.dialog_delete_title))
                setMessage((resources.getString(R.string.dialog_delete_message, preference.label)))
                setPositiveButton(resources.getString(R.string.dialog_delete_confirm)) { _, _ ->
                    deletePreference(preference)
                }
                setNegativeButton(resources.getString(R.string.dialog_delete_cancel)) { _, _ ->
                    myAdapter.notifyItemChanged(index)
                }
                setCancelable(false)
                show()
            }
    }


    private fun deletePreference(preference: PreferenceEntity) {
        lifecycleScope.launch(IO) {
            searchPreferenceVM.deletePreference(preference)
            withContext(Main) {
                showSnackBar(preference)

                searchPreferenceVM.searchPreference(latestSearch)
            }
        }
    }

    private fun showSnackBar(preference: PreferenceEntity) {
        Snackbar.make(requireView(), getString(R.string.article_deleted), Snackbar.LENGTH_LONG)
            .apply {
                setAction(getString(R.string.undo)) {
                    lifecycleScope.launch(IO) {
                        searchPreferenceVM.restorePreference(preference)
                        withContext(Main) {
                            searchPreferenceVM.searchPreference(latestSearch)
                        }
                    }
                }
            }.show()
    }

    override fun swipePreferenceIndex(index: Int) {
        checkDelete(index)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.searchFragment_floatBar -> navController.navigate(R.id.searchPreferences_addNewSearchPreference)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let {
            latestSearch = it
            searchPreferenceVM.searchPreference(it)
        }
        return true
    }
}
