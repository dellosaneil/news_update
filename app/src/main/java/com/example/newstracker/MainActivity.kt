package com.example.newstracker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstracker.databinding.ActivityMainBinding
import com.example.newstracker.recyclerView.MainActivityAdapter
import com.example.newstracker.repository.Repository
import com.example.newstracker.viewModel.MainVM
import com.example.newstracker.viewModel.MainVMFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val TAG = "MainActivity"
    private lateinit var repository: Repository
    private lateinit var viewModelFactory : ViewModelProvider.Factory
    private lateinit var viewModel : MainVM
    private lateinit var myAdapter : MainActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViewModel()
    }
    private fun initializeViewModel(){
        initializeRecyclerView()
        repository = Repository()
        viewModelFactory = MainVMFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainVM::class.java)
        viewModel.retrieveArticles()
        viewModel.getArticles().observe(this, {
            if (it.isSuccessful) {
                it.body()?.let { it1 -> myAdapter.setNewsArticles(it1.articles) }
            }
        })
    }

    private fun initializeRecyclerView(){
        binding.recyclerView.apply{
            layoutManager = LinearLayoutManager(this@MainActivity)
            myAdapter = MainActivityAdapter()
            adapter = myAdapter
        }
    }


}