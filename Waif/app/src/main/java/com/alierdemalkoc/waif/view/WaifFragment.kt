package com.alierdemalkoc.waif.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alierdemalkoc.waif.R
import com.alierdemalkoc.waif.databinding.FragmentWaifBinding


class WaifFragment : Fragment() {
    private var _binding: FragmentWaifBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWaifBinding.inflate(inflater,container,false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toggleGroup.addOnButtonCheckedListener { toggleGroup, checkedId, isChecked ->
            if (isChecked){
                when(checkedId){
                    binding.searchButton.id -> Toast.makeText(requireContext(),"Ara",Toast.LENGTH_SHORT).show()
                    binding.foundButton.id -> Toast.makeText(requireContext(),"Bul",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}