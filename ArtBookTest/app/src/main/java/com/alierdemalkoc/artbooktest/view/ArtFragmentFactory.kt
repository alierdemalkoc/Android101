package com.alierdemalkoc.artbooktest.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.alierdemalkoc.artbooktest.adapter.ArtRecyclerAdapter
import com.alierdemalkoc.artbooktest.adapter.ImageRecyclerAdapter
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ArtFragmentFactory @Inject constructor(
    private val glide : RequestManager,
    private val artRecyclerAdapter: ArtRecyclerAdapter,
    private val imageRecyclerAdapter: ImageRecyclerAdapter
) : FragmentFactory(){
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            ImageAPI::class.java.name -> ImageAPI(imageRecyclerAdapter)
            ArtFragment::class.java.name -> ArtFragment(artRecyclerAdapter)
            ArtDetailsFragment::class.java.name -> ArtDetailsFragment(glide)
            else -> super.instantiate(classLoader, className)
        }
    }
}