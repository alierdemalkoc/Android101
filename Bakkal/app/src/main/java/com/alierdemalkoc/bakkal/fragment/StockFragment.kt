package com.alierdemalkoc.bakkal.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.alierdemalkoc.bakkal.R
import com.alierdemalkoc.bakkal.adapter.ProductAdapter
import com.alierdemalkoc.bakkal.databinding.ActivityMainBinding
import com.alierdemalkoc.bakkal.databinding.FragmentStockBinding
import com.alierdemalkoc.bakkal.model.Product
import com.alierdemalkoc.bakkal.roomdb.ProductDao
import com.alierdemalkoc.bakkal.roomdb.ProductDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class StockFragment : Fragment() {
    private var _binding: FragmentStockBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productDatabase : ProductDatabase
    private lateinit var productDao : ProductDao
    private val mDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDatabase = Room.databaseBuilder(requireContext(),ProductDatabase::class.java,"Product").build()
        productDao = productDatabase.productDao()
        setHasOptionsMenu(true)

        }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.edit_profile).setVisible(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStockBinding.inflate(inflater,container,false)
        val view = binding.root
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFromSQL()
        //binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //productAdapter = ProductAdapter(productList as ArrayList<Product>)
        //binding.recyclerView.adapter = productAdapter

    }
    fun getFromSQL() {
        mDisposable.add(productDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))
    }
    private fun handleResponse(productList: List<Product>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        productAdapter = ProductAdapter(productList)
        binding.recyclerView.adapter = productAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}