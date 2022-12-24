package com.alierdemalkoc.bakkal.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.room.Room
import com.alierdemalkoc.bakkal.R
import com.alierdemalkoc.bakkal.databinding.FragmentAddBinding
import com.alierdemalkoc.bakkal.model.Product
import com.alierdemalkoc.bakkal.roomdb.ProductDao
import com.alierdemalkoc.bakkal.roomdb.ProductDatabase
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.jar.Manifest

class AddFragment : Fragment() {
    var selectedPicture : Uri? = null
    var selectedBitmap : Bitmap? = null
    private var _binding: FragmentAddBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var productDatabase: ProductDatabase
    private lateinit var productDao: ProductDao
    private val mDisposable = CompositeDisposable()
    var productFromMain : Product? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()

        productDatabase = Room.databaseBuilder(requireContext(), ProductDatabase::class.java, "Product").build()
        productDao = productDatabase.productDao()

        setHasOptionsMenu(true)

        }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            _binding = FragmentAddBinding.inflate(layoutInflater,container,false)
            val view = binding.root
            return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addButton.setOnClickListener { addProduct(view) }
        binding.selectImage.setOnClickListener{ selectImage(view) }

        arguments?.let {
            val info = AddFragmentArgs.fromBundle(it).info
            if (info.equals("new")){
                //NEW
                binding.productName.setText("")
                binding.stock.setText("")

                binding.addButton.visibility = View.VISIBLE
                val selectedImageBackground = BitmapFactory.decodeResource(context?.resources,R.drawable.selectimage)

                binding.selectImage.setImageBitmap(selectedImageBackground)

            } else if (info.equals("old")) {
                //Change
                    val selectedId = AddFragmentArgs.fromBundle(it).id
                mDisposable.add(productDao.getProductById(selectedId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponseWithOldProduct))

                binding.stock.isEnabled = false
                binding.productName.isEnabled = false

                binding.addButton.visibility = View.GONE

                //Continue

                            }
        }

    }
    private fun handleResponseWithOldProduct(product: Product){
        productFromMain = product
        binding.productName.setText(product.name)

        binding.stock.setText(product.stock.toString())
        product.image?.let {
            val bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
            binding.selectImage.setImageBitmap(bitmap)
        }
    }

    fun addProduct(view: View) {

        val productName = binding.productName.text.toString()
        val stock = binding.stock.text.toString()
        val stockInt = Integer.parseInt(stock)


        if (selectedBitmap != null) {
            val smallBitmap = makeSmallerBitmap(selectedBitmap!!,300)

            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()

            val product = Product(productName, stockInt ,byteArray)

            mDisposable.add(productDao.insert(product)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse))

        }
    }

    private fun handleResponse() {
        val action = AddFragmentDirections.goToStockFromAdd()
        Navigation.findNavController(requireView()).navigate(action)
    }



    fun makeSmallerBitmap(image: Bitmap, maximumSize : Int) : Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()
        if (bitmapRatio > 1) {
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        } else {
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image,width,height,true)
    }


    fun selectImage(view: View) {

        activity?.let {
            if(ContextCompat.checkSelfPermission(requireActivity().applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                        View.OnClickListener {
                            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }

        }

    }

    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    selectedPicture = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                selectedPicture!!
                            )
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.selectImage.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                selectedPicture
                            )
                            binding.selectImage.setImageBitmap(selectedBitmap)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result ->
            if (result) {
                //permission granted
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //permission denied
                Toast.makeText(requireContext(), "Permisson needed!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}