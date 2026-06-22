# Diagnosis dan Rencana Perbaikan Error

Berdasarkan analisis pada file `BusDataSource.kt`, `BusRepositoryImpl.kt`, dan `SubmitCrowdReportUseCase.kt`, berikut adalah temuan penyebab error dan rencana perbaikannya.

## 1. Diagnosis Masalah

### A. Masalah Utama: Infrastruktur Build (Gradle)
Ditemukan error kritis pada konfigurasi Gradle yang menyebabkan IDE tidak dapat meresolusi dependensi dengan benar (seperti Firebase dan Coroutines).
* **Penyebab**: Versi `androidx.core:core-ktx:1.19.0` membutuhkan `compileSdk 37`, namun proyek saat ini menggunakan `36.1`.
* **Dampak**: Semua file yang menggunakan library eksternal (Firestore, `await()`, dll) akan berwarna merah karena class-class tersebut tidak terindeks.

### B. File: BusDataSource.kt
* **Error**: `Unresolved reference 'firestore'`.
* **Penyebab**: Selain masalah dependensi, penggunaan *type inference* pada fungsi `busesCollection()` dari *platform type* (Java) tanpa tipe eksplisit dapat membingungkan compiler saat resolusi dependensi gagal.
* **Indikasi**: Indentasi yang tidak standar (8 spasi) dan pemisahan baris yang agresif pada pemanggilan fungsi.

### C. File: BusRepositoryImpl.kt
* **Error**: `Unresolved reference 'BusDataSource'`, `Unresolved reference 'dataSource'`, dan kegagalan pada `.await()`.
* **Penyebab**: 
    1. Import `BusDataSource` gagal karena file `BusDataSource.kt` dianggap rusak/tidak valid oleh compiler.
    2. Karena import gagal, properti `dataSource` tidak dikenali di dalam class.
    3. Fungsi `.await()` dan `.toObject()` tidak terbaca karena dependensi `kotlinx-coroutines-play-services` dan `firebase-firestore` tidak ter-load sempurna akibat error `compileSdk`.

### D. File: SubmitCrowdReportUseCase.kt
* **Error**: Parameter constructor (seperti `checkCooldownUseCase`, `reportRepository`) dilaporkan sebagai `Unresolved reference` saat dipanggil, namun sekaligus dilaporkan sebagai `Property is never used`.
* **Penyebab**: Ini adalah gejala khas saat header class atau struktur constructor mengalami "parsing error" atau saat IDE kehilangan konteks akibat sinkronisasi Gradle yang gagal total.

---

## 2. Rencana Perbaikan

### Langkah 1: Perbaikan Konfigurasi Gradle (Prioritas Tinggi)
Memperbarui `compileSdk` agar sesuai dengan kebutuhan library yang digunakan.
* **File**: `app/build.gradle.kts`
* **Tindakan**: Ubah `compileSdk` menjadi `37`.

### Langkah 2: Pembersihan dan Pengetikan Eksplisit (Strong Typing)
Membantu compiler meresolusi tipe data dengan memberikan tipe kembalian secara eksplisit pada fungsi-fungsi remote.
* **File**: `BusDataSource.kt`
* **Tindakan**:
    * Tambahkan tipe kembalian `: CollectionReference` pada `busesCollection()`.
    * Rapikan indentasi menjadi standar 4 spasi.

### Langkah 3: Perbaikan Struktur Class
Memastikan semua import tersedia dan struktur class bersih dari karakter tersembunyi atau kesalahan penulisan.
* **File**: `BusRepositoryImpl.kt` & `SubmitCrowdReportUseCase.kt`
* **Tindakan**:
    * Rapikan indentasi dan pastikan import `kotlinx.coroutines.tasks.await` benar-benar terhubung.
    * Pastikan pemanggilan Use Case (seperti `checkCooldownUseCase(...)`) menggunakan operator `invoke` yang benar.

---

## 3. Detail Tindakan Teknis

| File | Masalah Spesifik | Solusi |
| :--- | :--- | :--- |
| `build.gradle.kts` (app) | `compileSdk` tidak kompatibel | Update `compileSdk = 37` |
| `BusDataSource.kt` | `firestore` unresolved | Pastikan import Firebase valid dan tambahkan tipe eksplisit pada fungsi. |
| `BusRepositoryImpl.kt` | `await()` & `toObject` merah | Perbaiki `compileSdk`, lalu pastikan `DocumentSnapshot` teresolusi. |
| `SubmitCrowdReportUseCase.kt` | Properti constructor merah | Rapikan struktur constructor dan lakukan Gradle Sync ulang. |

---
**Rekomendasi**: Setelah melakukan perubahan di atas, lakukan **Gradle Sync** (File > Sync Project with Gradle Files) dan **Rebuild Project**.
