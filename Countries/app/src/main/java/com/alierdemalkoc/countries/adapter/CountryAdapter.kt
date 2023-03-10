package com.alierdemalkoc.countries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.alierdemalkoc.countries.R
import com.alierdemalkoc.countries.databinding.CountryRowBinding
import com.alierdemalkoc.countries.model.Country
import com.alierdemalkoc.countries.util.downloadFromUrl
import com.alierdemalkoc.countries.util.placeHolderProgressBar
import com.alierdemalkoc.countries.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.country_row.view.*

class CountryAdapter(val countryList: ArrayList<Country>): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(), CountryClickListener {
    class CountryViewHolder(var view: CountryRowBinding) : RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<CountryRowBinding>(inflater,R.layout.country_row,parent,false)
        return CountryViewHolder(view)


    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.view.country = countryList[position]
        holder.view.listener = this
        /*
        holder.binding.name.text = countryList[position].countryName
        holder.binding.region.text = countryList[position].countryRegion
        holder.view.setOnClickListener {
            val action = FeedFragmentDirections.feedToCountry(countryList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }
        holder.binding.flagRecycler.downloadFromUrl(countryList[position].countryFlagURL,
            placeHolderProgressBar(holder.view.context))*/
    }

    override fun getItemCount(): Int {
        return countryList.size
    }
    fun updateCountryList(newCountryList: List<Country>){
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }

    override fun onCountryClicked(v: View) {
        val uuid = v.countryUuidText.text.toString().toInt()
        val action = FeedFragmentDirections.feedToCountry(uuid)
        Navigation.findNavController(v).navigate(action)
    }

}