package com.dicoding.projecthair.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.projecthair.databinding.FragmentHomeBinding
import com.dicoding.projecthair.ui.ContentHairActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigasi ke ContentHairActivity saat btnSlhair diklik
        binding.btnSlhair.setOnClickListener {
            val intent = Intent(requireContext(), ContentHairActivity::class.java)
            startActivity(intent)
        }
        binding.btnSlsalon.setOnClickListener {
            val intent = Intent(requireContext(), ContentHairActivity::class.java)
            startActivity(intent)
        }

        // Tambahkan event klik untuk btnSlsalon (opsional)
        binding.btnSlsalon.setOnClickListener {
            // Tambahkan intent atau aksi lain untuk tombol ini
        }
        binding.btnSlsalon.setOnClickListener{

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
