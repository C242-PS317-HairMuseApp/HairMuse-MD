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
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.projecthair.R
import com.dicoding.projecthair.databinding.FragmentHomeBinding
import com.dicoding.projecthair.ui.ContentHairActivity
import com.dicoding.projecthair.ui.NearestSalonActivity
import com.dicoding.projecthair.ui.home.viewpager.ImageData
import com.dicoding.projecthair.ui.home.viewpager.ImageSliderAdapter
import com.dicoding.projecthair.ui.home.viewpager.dpToPx

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ImageSliderAdapter
    private val imageList = ArrayList<ImageData>()
    private val lines = ArrayList<View>() // Store the line indicators

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // List of image URLs and corresponding website URLs
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

        // Prepare the image data
        imageUrls.forEachIndexed { index, url ->
            imageList.add(ImageData(url, websiteUrls[index])) // Add both imageUrl and websiteUrl
        }

        // Set up ViewPager2 and adapter
        adapter = ImageSliderAdapter(imageList) { position ->
            // Handle image click or swipe
            updateIndicators(position)
        }

        binding.viewPager2.adapter = adapter
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }
        })

        // Create and add line indicators (instead of dots)
        createIndicators()

        // Set initial position
        updateIndicators(0)

        // Optional: Handle item click in image slider
        binding.viewPager2.setOnClickListener {
            // Get the current image's corresponding website URL
            val currentPosition = binding.viewPager2.currentItem
            val websiteUrl = imageList[currentPosition].websiteUrl

            // Open the website in a browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }

        // Handle button clicks for 'See All'
        setupButtonListeners()
    }

    private fun createIndicators() {
        lines.clear()
        binding.indicatorContainer.removeAllViews()

        for (i in 0 until imageList.size) {
            val line = View(requireContext()).apply {
                setBackgroundResource(R.drawable.ic_dot_inactive) // Custom line drawable for inactive state
                layoutParams = LinearLayout.LayoutParams(
                    20.dpToPx(), // Line width (adjust as needed)
                    2.dpToPx()   // Line height (adjust as needed)
                ).apply {
                    setMargins(0.dpToPx(), 0, 0.dpToPx(), 0) // Margin between lines
                }
            }

            binding.indicatorContainer.addView(line)
            lines.add(line)
        }
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until lines.size) {
            if (i == position) {
                lines[i].setBackgroundResource(R.drawable.ic_dot_active) // Custom line drawable for active state
            } else {
                lines[i].setBackgroundResource(R.drawable.ic_dot_inactive) // Custom line drawable for inactive state
            }
        }
    }

    private fun setupButtonListeners() {
        // Button for "See All" Hair Men
        binding.btnSlhairmen.setOnClickListener {
            // Action for Hair Men "See All"
            Toast.makeText(requireContext(), "See All Hair Men", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), ContentHairActivity::class.java)
            startActivity(intent)
        }

        // Button for "See All" Hair Women
        binding.btnSlhair.setOnClickListener {
            // Action for Hair Women "See All"
            Toast.makeText(requireContext(), "See All Hair Women", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), ContentHairActivity::class.java)
            startActivity(intent)
        }

        // Button for "See All" Salon
        binding.btnSlsalon.setOnClickListener {
            // Action for Salon "See All"
            Toast.makeText(requireContext(), "See All Salon", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), NearestSalonActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
