package com.example.inspiredaily.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.inspiredaily.R
import com.example.inspiredaily.common.gone
import com.example.inspiredaily.common.isNetworkAvailable
import com.example.inspiredaily.common.showToast
import com.example.inspiredaily.common.visible
import com.example.inspiredaily.databinding.FragmentHomeBinding
import com.example.inspiredaily.notification.NotificationHelper
import com.example.inspiredaily.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
        callApi()
        observer()
        observeWorker()
    }

    private fun observeWorker() {
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData("QuoteSyncWork")
            .observe(viewLifecycleOwner) { workInfos ->
                val workInfo = workInfos?.firstOrNull()
                Log.d("WorkerStatus", "State: ${workInfo?.state}")
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val quoteText = workInfo.outputData.getString("quote")
                   // binding.tvQuote.text = quoteText
                    binding.cardLayout.visible()
                    binding.circularProgressBar.gone()
                    mainViewModel.getRandomQuotes()
                }
            }
    }

    private fun observer() {
        mainViewModel.quote.observe(requireActivity(), Observer {
            if(it != null){
                Log.d("responseApi",it.toString())

                binding.apply {
                    cardLayout.visible()
                    circularProgressBar.gone()
                    model = it
                }
            } else {
                binding.apply {
                    cardLayout.visible()
                    circularProgressBar.gone()
                    tvQuote.text = "No Quote"
                }
            }
        })
    }

    private fun callApi() {
        if(requireContext().isNetworkAvailable()){
            mainViewModel.getRandomQuotes()
        }
        else {
            requireContext().showToast("No Internet Connection")
        }
    }

    private fun initialization() {
        (requireActivity() as MainActivity).setToolbarTitle("Home")
    }
}