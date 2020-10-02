package com.esaku.sekolahsiswa.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.esaku.sekolahsiswa.fragments.InformasiFragment
import com.esaku.sekolahsiswa.fragments.NotifikasiFragment


class PageAdapter internal constructor(fm: FragmentManager?, private val numOfTabs: Int) :
    FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> InformasiFragment()
            1 -> NotifikasiFragment()
            else -> InformasiFragment()
        }
    }

    override fun getCount(): Int {
        return numOfTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Informasi"
            1 -> return "Notifikasi"
        }
        return null
    }
}