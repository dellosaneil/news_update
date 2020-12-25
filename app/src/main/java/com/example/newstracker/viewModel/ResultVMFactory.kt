package com.example.newstracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newstracker.repository.RetrofitRepository

class ResultVMFactory(private val retrofitRepository: RetrofitRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ResultVM(retrofitRepository) as T
    }

}