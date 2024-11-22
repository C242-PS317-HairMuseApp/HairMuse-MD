package com.dicoding.projecthair.ui.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.projecthair.R
import com.dicoding.projecthair.databinding.FragmentCameraBinding
import com.dicoding.projecthair.databinding.FragmentProfilBinding


class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null

    class ProfilFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment, ganti R.layout.fragment_home dengan layout yang sesuai
            return inflater.inflate(R.layout.fragment_profil, container, false)
        }
    }

}