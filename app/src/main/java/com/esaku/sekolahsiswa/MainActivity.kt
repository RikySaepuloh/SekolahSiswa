package com.esaku.sekolahsiswa

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.esaku.sekolahsiswa.fragments.AkunFragment
import com.esaku.sekolahsiswa.fragments.BerandaFragment
import com.esaku.sekolahsiswa.fragments.PesanFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.frame_layout, BerandaFragment()).commit()
        bottom_navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_beranda -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.frame_layout, BerandaFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_pesan -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.frame_layout, PesanFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_profile -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.frame_layout, AkunFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
        val badge = bottom_navigation.getOrCreateBadge(R.id.menu_pesan)
        badge.isVisible = true
// An icon only badge will be displayed unless a number is set:
        badge.number = 4
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Yakin ingin keluar?")
        builder.setCancelable(true)
        builder.setPositiveButton("Ya") { _, _ -> super.onBackPressed() }
        builder.setNegativeButton("Tidak") { _, _ ->

        }
        val dialog = builder.create()
        dialog.show()
    }
}