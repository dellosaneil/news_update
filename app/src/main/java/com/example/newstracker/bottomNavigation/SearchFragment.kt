package com.example.newstracker.bottomNavigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstracker.Constants.Companion.ARGUMENT_BUNDLE
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : FragmentLifecycleLogging(), SearchPreferenceAdapter.OnItemClickedListener,
    SearchPreferenceSwipeListener.DeleteSwipe, View.OnClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchPreferenceVM: SearchPreferenceVM by viewModels()
    private lateinit var myAdapter: SearchPreferenceAdapter
    private val scope = CoroutineScope(IO)
    private lateinit var navController: NavController

    private val TAG = "SearchFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        initializeRecyclerView()
        // Redirect Fragment into Fragment with adding capability
        val itemSwipeListener = SearchPreferenceSwipeListener(this)
        val itemTouchHelper = ItemTouchHelper(itemSwipeListener)
        itemTouchHelper.attachToRecyclerView(binding.searchFragmentRecyclerView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.searchFragmentAdd.setOnClickListener(this)
    }

    private fun initializeRecyclerView() {
        Log.i(TAG, "initializeRecyclerView: ")
        myAdapter = SearchPreferenceAdapter(this)
        binding.searchFragmentRecyclerView.run {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            val customDecorator = RecyclerViewDecorator(6, 6)
            addItemDecoration(customDecorator)
            searchPreferenceVM.retrieveAllPreference()
                .observe(viewLifecycleOwner, { myAdapter.setSearchPreferences(it) })
        }
    }

    override fun searchBreakingNews(pref: PreferenceEntity) {
        Log.i(TAG, "onItemClicked: ")
        val bundle = bundleOf(ARGUMENT_BUNDLE to pref)
        navController.navigate(R.id.searchPreferences_newsArticles, bundle)
    }

    override fun preferenceDetails(pref: PreferenceEntity) {
        val dialog = SearchPreferenceDialog(pref)
        dialog.show(parentFragmentManager, SEARCH_DETAILS_DIALOG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }


    private fun checkDelete(label: String?, index: Int) {
        MaterialAlertDialogBuilder(requireView().context)
            .apply {
                setTitle(resources.getString(R.string.dialog_delete_title))
                setMessage((resources.getString(R.string.dialog_delete_message, label)))
                setPositiveButton(resources.getString(R.string.dialog_delete_confirm)) { _, _ ->
                    run {
                        deletePreference(label)
                    }
                }
                setNegativeButton(resources.getString(R.string.dialog_delete_cancel)) { _, _ ->
                    myAdapter.notifyItemChanged(
                        index
                    )
                }
                setCancelable(false)
                show()
            }
    }

    private fun deletePreference(label: String?) {
        scope.launch(IO) {
            label?.let { searchPreferenceVM.deletePreference(it) }
        }
    }

    override fun swipePreferenceIndex(index: Int) {
        val label = searchPreferenceVM.retrieveAllPreference().value?.get(index)?.label
        checkDelete(label, index)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.searchFragmentAdd -> navController.navigate(R.id.searchPreferences_addNewSearchPreference)
        }
    }
}
