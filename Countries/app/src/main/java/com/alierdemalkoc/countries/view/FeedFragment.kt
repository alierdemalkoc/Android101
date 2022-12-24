package com.alierdemalkoc.countries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alierdemalkoc.countries.adapter.CountryAdapter
import com.alierdemalkoc.countries.databinding.FragmentFeedBinding
import com.alierdemalkoc.countries.viewmodel.FeedViewModel

class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding
    private lateinit var viewModel: FeedViewModel
    private val countryAdapter = CountryAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    fun observeLiveData(){
        viewModel.countries.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.countryRecycler.visibility = View.VISIBLE
                countryAdapter.updateCountryList(it)

            }
        })
        viewModel.countryError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding.countryError.visibility = View.VISIBLE
                } else{
                    binding.countryError.visibility = View.GONE
                }
            }
        })
        viewModel.countryLoading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding.countryLoading.visibility = View.VISIBLE
                    binding.countryError.visibility = View.GONE
                    binding.countryRecycler.visibility = View.GONE
                } else {
                    binding.countryLoading.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(inflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        viewModel.refreshFromAPI()

        binding.countryRecycler.layoutManager = LinearLayoutManager(context)
        binding.countryRecycler.adapter = countryAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.countryRecycler.visibility = View.GONE
            binding.countryError.visibility = View.GONE
            binding.countryLoading.visibility = View.VISIBLE
            viewModel.refreshData()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
    }

}