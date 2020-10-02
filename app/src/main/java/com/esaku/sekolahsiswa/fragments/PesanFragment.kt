package com.esaku.sekolahsiswa.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.esaku.sekolahsiswa.Preferences
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.adapter.PageAdapter
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_pesan.*
import kotlinx.android.synthetic.main.fragment_pesan.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.reflect.Type

class PesanFragment : Fragment() {
    var preferences  = Preferences()
    private lateinit var myview : View
    lateinit var badgeDrawable: BadgeDrawable
    lateinit var mycontext:Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences.setPreferences(context)
        mycontext=context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myview = inflater.inflate(R.layout.fragment_pesan, container, false)

        val pageAdapter =
            PageAdapter(
                fragmentManager,
                myview.tablayout.tabCount,
            )

        myview.viewpager.offscreenPageLimit = 2
        myview.viewpager.adapter = pageAdapter

        myview.viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(myview.tablayout))

        badgeDrawable = myview.tablayout.getTabAt(0)!!.orCreateBadge
        badgeDrawable.horizontalOffset = -20
        badgeDrawable.backgroundColor = ContextCompat.getColor(mycontext,R.color.colorBlue)
        badgeDrawable.isVisible = true
        badgeDrawable.number=90
        myview.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                myview.viewpager.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        myview.tablayout.setupWithViewPager(myview.viewpager)

        return myview
    }

}
