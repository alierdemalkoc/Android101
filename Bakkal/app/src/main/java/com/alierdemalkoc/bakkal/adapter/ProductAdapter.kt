package com.alierdemalkoc.bakkal.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.alierdemalkoc.bakkal.databinding.RecyclerRowBinding
import com.alierdemalkoc.bakkal.fragment.StockFragmentDirections
import com.alierdemalkoc.bakkal.model.Product
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import androidx.navigation.ui.navigateUp
import androidx.navigation.Navigation.findNavController
import com.alierdemalkoc.bakkal.roomdb.ProductDao
import com.alierdemalkoc.bakkal.roomdb.ProductDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.coroutines.coroutineContext

class ProductAdapter(val productList : List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {
    private val mDisposable = CompositeDisposable()
    private lateinit var productDatabase: ProductDatabase
    private lateinit var productDao: ProductDao
    var productFromMain : Product? = null

    class ProductHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductHolder(binding)

    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.binding.productNameText.text = productList.get(position).name
        holder.binding.stockText.text = productList.get(position).stock.toString()
        val becomeImage = ByteArrayInputStream(productList.get(position).image)
        val imageBitmap = BitmapFactory.decodeStream(becomeImage)
        holder.binding.productImage.setImageBitmap(imageBitmap)
        holder.itemView.setOnClickListener {
            val action = StockFragmentDirections.goToAddFromStock("old",productList[position].id)
            Navigation.findNavController(it).navigate(action)
        }
        holder.binding.changeButton.setOnClickListener {

        }
        holder.binding.deleteButton.setOnClickListener {
            Toast.makeText(holder.itemView.context,"lan",Toast.LENGTH_LONG).show()
        }

    }
    private fun handleResponse(){

    }

    override fun getItemCount(): Int {
       return productList.size
    }
}