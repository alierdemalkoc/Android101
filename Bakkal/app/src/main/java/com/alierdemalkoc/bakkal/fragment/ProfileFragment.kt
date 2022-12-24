package com.alierdemalkoc.bakkal.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.alierdemalkoc.bakkal.R
import com.alierdemalkoc.bakkal.databinding.FragmentProfileBinding
import com.alierdemalkoc.bakkal.databinding.FragmentStockBinding

private lateinit var binding: FragmentProfileBinding


class ProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root
        return view

    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.add_product).setVisible(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.stockButton.setOnClickListener {
            val action = ProfileFragmentDirections.goToStockFragment()
            Navigation.findNavController(it).navigate(action)


        }
    }
}