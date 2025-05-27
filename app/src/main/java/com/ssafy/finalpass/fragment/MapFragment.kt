package com.ssafy.finalpass.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.R
import com.ssafy.finalpass.dto.Store

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private val viewModel: MainActivityViewModel by activityViewModels()

    private var pendingStores: List<Store>? = null
    private lateinit var markerStoreMap: MutableMap<Marker, Store>

    // 하단 배너 UI 요소
    private lateinit var storeInfoBanner: View
    private lateinit var tvStoreName: TextView
    private lateinit var ratingBarStore: RatingBar
    private lateinit var tvReviewCount: TextView
    private lateinit var tvStoreStatus: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 배너 뷰 초기화
        storeInfoBanner = view.findViewById(R.id.storeInfoBanner)
        tvStoreName = view.findViewById(R.id.tvStoreName)
        ratingBarStore = view.findViewById(R.id.ratingBarStore)
        tvReviewCount = view.findViewById(R.id.tvReviewCount)
        tvStoreStatus = view.findViewById(R.id.tvStoreStatus)

        storeInfoBanner.visibility = View.GONE

        // 지도 준비
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        // 매장 데이터 관찰
        viewModel.stores.observe(viewLifecycleOwner) { stores ->
            pendingStores = stores
            if (::googleMap.isInitialized) {
                drawMarkers(stores)
            }
        }

        viewModel.getAllStore()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // 기본 위치: 서울 시청
        val center = LatLng(37.5665, 126.9780)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 14f))

        // 마커 클릭 리스너
        googleMap.setOnMarkerClickListener { marker ->
            markerStoreMap[marker]?.let { store ->
                showStoreBanner(store)
            }
            true
        }

        pendingStores?.let { drawMarkers(it) }
    }

    private fun drawMarkers(stores: List<Store>) {
        googleMap.clear()
        markerStoreMap = mutableMapOf()

        for (store in stores) {
            val position = LatLng(store.latitude.toDouble(), store.longitude.toDouble())
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(store.name)
            )
            if (marker != null) {
                markerStoreMap[marker] = store
            }
        }
    }

    private fun showStoreBanner(store: Store) {
        tvStoreName.text = store.name

        val ratings = store.comment.map { it.rating }
        val avgRating = if (ratings.isNotEmpty()) ratings.average().toFloat() else 0f
        val reviewCount = store.comment.size

        ratingBarStore.rating = avgRating
        tvReviewCount.text = "($reviewCount)"

        tvStoreStatus.text = "영업 중"
        tvStoreStatus.setTextColor(0xFF009688.toInt())


        storeInfoBanner.visibility = View.VISIBLE
    }
}
