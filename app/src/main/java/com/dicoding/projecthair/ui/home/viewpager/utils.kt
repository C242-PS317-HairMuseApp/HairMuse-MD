package com.dicoding.projecthair.ui.home.viewpager

import android.content.res.Resources

// Fungsi untuk mengonversi dp ke px
fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}