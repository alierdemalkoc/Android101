package com.alierdemalkoc.countries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alierdemalkoc.countries.R
import com.alierdemalkoc.countries.databinding.FragmentCountryBinding
import com.alierdemalkoc.countries.model.Country
import com.alierdemalkoc.countries.util.downloadFromUrl
import com.alierdemalkoc.countries.util.placeHolderProgressBar
import com.alierdemalkoc.countries.viewmodel.CountryViewModel
import kotlinx.android.synthetic.main.fragment_country.*

class CountryFragment : Fragment() {
    private lateinit var binding: FragmentCountryBinding
    private lateinit var viewModel: CountryViewModel
    private var countryUuid = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_country,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            countryUuid = CountryFragmentArgs.fromBundle(it).countryUuid

        }
        viewModel = ViewModelProvider(this).get(CountryViewModel::class.java)
        viewModel.getDataFromRoom(countryUuid)

        observeLiveData()
    }
    private fun observeLiveData(){
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer {country ->
            country?.let {
                binding.selectedCountry = country
                /*binding.countryName.text = it.countryName
                binding.countryCapital.text = it.countryCapital
                binding.countryRegion.text = it.countryRegion
                binding.countryCurrency.text = it.countryCurrency
                binding.countryLanguage.text = it.countryLanguage
                context?.let {
                    binding.countryFlag.downloadFromUrl(country.countryFlagURL,
                        placeHolderProgressBar(it))
                }*/
            }
        })
    }
}