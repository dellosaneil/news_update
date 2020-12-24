package com.example.newstracker.bottomNavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.newstracker.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SearchFragment : Fragment() {

    private lateinit var addParameters : FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_search, container, false)
        addParameters = view.findViewById(R.id.searchFragment_add)
        // Redirect Fragment into Fragment with adding capability
        addParameters.setOnClickListener{ Navigation.findNavController(view)
            .navigate(R.id.searchFragment_addNewCategory)}

        return view
    }
}