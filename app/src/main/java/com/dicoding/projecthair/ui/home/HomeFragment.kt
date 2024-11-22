package com.dicoding.projecthair.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.projecthair.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment, ganti R.layout.fragment_home dengan layout yang sesuai
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}
