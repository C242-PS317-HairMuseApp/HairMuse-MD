# Face Shape Prediction Android App

Aplikasi Android ini memungkinkan pengguna untuk memilih gambar dari galeri atau mengambil foto langsung dari kamera, kemudian mengirim gambar bersama dengan informasi gender untuk mendapatkan prediksi bentuk wajah menggunakan API.

## Fitur
- **Floating Action Button (FAB)**: Pengguna dapat memilih untuk mengupload gambar dari galeri atau menggunakan kamera.
- **Pemilihan Gender**: Pengguna dapat memilih jenis kelamin (Male/Female) sebelum mengirim gambar untuk prediksi.
- **Prediksi Bentuk Wajah**: Gambar dan gender yang dipilih dikirim ke API untuk mendapatkan prediksi bentuk wajah.

## Struktur Aplikasi
- **MainActivity**: Aktivitas utama yang menangani UI, termasuk pemilihan gambar dan pemilihan gender.
- **FAB**: Dua FAB yang digunakan untuk membuka galeri dan kamera.
- **RadioGroup**: Digunakan untuk memilih gender (Male/Female).
- **API**: Menggunakan `OkHttp` untuk mengirim gambar dan data gender ke backend API untuk prediksi.

## Prasyarat
Sebelum menjalankan aplikasi ini, pastikan Anda telah melakukan hal-hal berikut:
1. **Android Studio** terinstal di komputer Anda.
2. **Emulator** atau perangkat Android yang terhubung untuk menguji aplikasi.
3. **Backend API** yang mendukung endpoint untuk menerima gambar dan data gender serta memberikan prediksi bentuk wajah.

## Instalasi

1. **Clone Repository**:
   Clone repository ini ke komputer Anda:

   ```bash
   git clone https://github.com/username/face-shape-prediction-android.git
