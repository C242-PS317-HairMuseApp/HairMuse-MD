package com.dicoding.projecthair.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.projecthair.R
import com.dicoding.projecthair.databinding.FragmentHomeBinding
import com.dicoding.projecthair.ui.ContentHairActivity
import com.dicoding.projecthair.ui.NearestSalonActivity
import com.dicoding.projecthair.ui.home.viewpager.ImageData
import com.dicoding.projecthair.ui.home.viewpager.ImageSliderAdapter
import com.dicoding.projecthair.ui.home.viewpager.Item
import com.dicoding.projecthair.ui.home.viewpager.dpToPx

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // RecyclerView adapters
    private lateinit var adapterHairMen: ItemAdapter
    private lateinit var adapterHairWomen: ItemAdapter
    private lateinit var adapterSalon: ItemAdapter

    // ViewPager2 related properties
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private val imageList = ArrayList<ImageData>()
    private val lines = ArrayList<View>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupMaleRecyclerView()
        setupFemaleRecyclerView()
        setupSalonRecyclerView()
        setupButtonListeners()
    }

    private fun setupViewPager() {
        val imageUrls = listOf(
            "https://mendeserve.com/cdn/shop/articles/hairstyles-for-men-with-oval-faces_16a3693e-3a11-4282-8edc-eb2de745ac94.png?v=1733476993&width=1100",
            "https://mendeserve.com/cdn/shop/articles/Top_12_Recommended_Haircuts_for_Men_with_Diamond_Face.png?v=1725945676&width=1100",
            "https://mendeserve.com/cdn/shop/articles/Iconic_1980s_Men_s_Hair_Trends.png?v=1732859857&width=1100"
        )

        val websiteUrls = listOf(
            "https://mendeserve.com/blogs/hair/best-trending-oval-face-hairstyles-for-men?srsltid=AfmBOooPnH_91vLj0Zrmx6u4rIJFDVH2GpyIvDXQQ_6i8wSpBkgEnFuX",
            "https://mendeserve.com/blogs/hair/top-recommended-haircuts-for-men-with-diamond-face?srsltid=AfmBOoqZNvzv7RYN4l1t7TNDdpsezaHsR8Wel38nJEcMRvZrR8BSv97p",
            "https://mendeserve.com/blogs/hair/1980s-mens-hair-styles"
        )

        // Prepare image data
        imageUrls.forEachIndexed { index, url ->
            imageList.add(ImageData(url, websiteUrls[index]))
        }

        // Setup ViewPager2 adapter
        imageSliderAdapter = ImageSliderAdapter(imageList) { position ->
            updateIndicators(position)
        }

        binding.viewPager2.apply {
            adapter = imageSliderAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicators(position)
                }
            })
        }

        // Create indicators and set initial position
        createIndicators()
        updateIndicators(0)

        // Handle ViewPager click
        binding.viewPager2.setOnClickListener {
            val currentPosition = binding.viewPager2.currentItem
            val websiteUrl = imageList[currentPosition].websiteUrl
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }
    }

    private fun createIndicators() {
        lines.clear()
        binding.indicatorContainer.removeAllViews()

        for (i in 0 until imageList.size) {
            val line = View(requireContext()).apply {
                setBackgroundResource(R.drawable.ic_dot_inactive)
                layoutParams = LinearLayout.LayoutParams(
                    20.dpToPx(),
                    2.dpToPx()
                ).apply {
                    setMargins(0.dpToPx(), 0, 0.dpToPx(), 0)
                }
            }
            binding.indicatorContainer.addView(line)
            lines.add(line)
        }
    }

    private fun updateIndicators(position: Int) {
        lines.forEachIndexed { index, view ->
            view.setBackgroundResource(
                if (index == position) R.drawable.ic_dot_active
                else R.drawable.ic_dot_inactive
            )
        }
    }

    private fun setupMaleRecyclerView() {
        val itemListHairMen = mutableListOf<Item>()
        val malePhotos = resources.obtainTypedArray(R.array.male_photo)
        val maleTexts = resources.getStringArray(R.array.text_male)

        for (i in maleTexts.indices) {
            val drawableId = malePhotos.getResourceId(i, -1)
            if (drawableId != -1) {
                itemListHairMen.add(Item(drawableId, maleTexts[i]))
            }
        }
        malePhotos.recycle()

        adapterHairMen = ItemAdapter(itemListHairMen) { selectedItem ->
            val intent = Intent(requireContext(), HairstyleRecommendationActivity::class.java)
            startActivity(intent)
        }

        binding.recyclerViewHairmele.apply {
            adapter = adapterHairMen
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupFemaleRecyclerView() {
        val itemListHairWomen = mutableListOf<Item>()
        val femalePhotos = resources.obtainTypedArray(R.array.female_photo)
        val femaleTexts = resources.getStringArray(R.array.txt_female)

        for (i in femaleTexts.indices) {
            val drawableId = femalePhotos.getResourceId(i, -1)
            if (drawableId != -1) {
                itemListHairWomen.add(Item(drawableId, femaleTexts[i]))
            }
        }
        femalePhotos.recycle()

        adapterHairWomen = ItemAdapter(itemListHairWomen) { selectedItem ->
            val intent = Intent(requireContext(), HairstyleRecommendationActivity::class.java)
            startActivity(intent)
        }

        binding.recyclerViewHairfemale.apply {
            adapter = adapterHairWomen
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }



    private fun setupButtonListeners() {
        binding.btnSlhairmen.setOnClickListener {
            Toast.makeText(requireContext(), "See All Hair Men", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), ContentHairActivity::class.java))
        }

        binding.btnSlhair.setOnClickListener {
            Toast.makeText(requireContext(), "See All Hair Women", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), ContentHairActivity::class.java))
        }

        binding.btnSlsalon.setOnClickListener {
            Toast.makeText(requireContext(), "See All Salon", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), NearestSalonActivity::class.java))
        }
    }

    private fun setupSalonRecyclerView() {
        val itemListSalon = mutableListOf<Item>()
        itemListSalon.add(Item(R.drawable.salon, "Salon 1"))
        itemListSalon.add(Item(R.drawable.salon_b, "Salon 2"))

        adapterSalon = ItemAdapter(itemListSalon) { selectedItem ->
            Toast.makeText(requireContext(), "Selected salon: ${selectedItem.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), NearestSalonActivity::class.java))
        }

        binding.recyclerViewSalon.apply {
            adapter = adapterSalon
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
