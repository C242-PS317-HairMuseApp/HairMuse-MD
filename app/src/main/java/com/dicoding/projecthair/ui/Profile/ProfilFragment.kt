package com.dicoding.projecthair.ui.Profile // Ganti dengan nama package Anda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.projecthair.databinding.FragmentProfilBinding

class ProfilFragment : Fragment() {

    // Menyimpan binding agar bisa mengakses view di dalam layout
    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menghubungkan layout XML dengan ViewBinding
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Akses elemen UI dari binding dan lakukan logika atau pengaturan
        // Contoh mengisi nama profil
        binding.nameEditText.setText("John Doe")  // Mengubah text pada EditText nama

        // Jika diperlukan, set listener atau event handler lainnya
        binding.btnEditProfile.setOnClickListener {
            // Logika untuk tombol edit profil
        }

        binding.btnEditPassword.setOnClickListener {
            // Logika untuk tombol ganti password
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Set binding ke null untuk mencegah memory leak
        _binding = null
    }
}
