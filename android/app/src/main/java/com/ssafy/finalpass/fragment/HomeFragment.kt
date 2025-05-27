package com.ssafy.finalpass.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpass.MainActivity
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.R
import com.ssafy.finalpass.adapter.BannerAdapter
import com.ssafy.finalpass.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class HomeFragment : BaseFragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val realImages = listOf(
        R.drawable.bannerimg4,
        R.drawable.bannerimg1,
        R.drawable.bannerimg2,
        R.drawable.bannerimg3
    )

    private lateinit var userId: String

    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var bannerHandler: Handler
    private var bannerPosition = 0

    override fun onResume() {
        super.onResume()

        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getString("user_id", "") ?: ""

        viewModel.getAttendance(userId)

        viewModel.attendanceList.observe(viewLifecycleOwner) { dates ->
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())
            val isChecked = dates.contains(today)

            // 출석 여부에 따라 UI 갱신
            updateAttendanceStatusView(isChecked)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        userId = prefs.getString("user_id", "") ?: ""

        // 배너 어댑터 설정
        bannerAdapter = BannerAdapter(realImages)
        binding.homeBannerViewPager.adapter = bannerAdapter

        // 부드러운 전환 애니메이션
        binding.homeBannerViewPager.setPageTransformer { page, position ->
            page.alpha = 0.25f + (1 - abs(position))
            page.scaleY = 0.85f + (1 - abs(position)) * 0.15f
        }

        // 자동 전환 핸들러
        bannerHandler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                bannerPosition = (bannerPosition + 1) % realImages.size
                val recyclerView = binding.homeBannerViewPager.getChildAt(0) as RecyclerView
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val smoothScroller = object : LinearSmoothScroller(binding.root.context) {
                    override fun getHorizontalSnapPreference(): Int = SNAP_TO_START
                    override fun calculateTimeForDeceleration(dx: Int): Int = 700
                }
                smoothScroller.targetPosition = bannerPosition
                layoutManager.startSmoothScroll(smoothScroller)

                bannerHandler.postDelayed(this, 3000)
            }
        }
        bannerHandler.post(runnable)

        // 카테고리 클릭 처리
        binding.categoryAll.setOnClickListener {
            moveToSearchFragment("전체")
        }
        binding.categoryNoodles.setOnClickListener {
            moveToSearchFragment("간편식")
        }
        binding.categoryRice.setOnClickListener {
            moveToSearchFragment("라면")
        }
        binding.categoryDrinks.setOnClickListener {
            moveToSearchFragment("음료")
        }
        binding.categoryNightFood.setOnClickListener {
            moveToSearchFragment("과자")
        }
        binding.categorySnacks.setOnClickListener {
            moveToSearchFragment("아이스크림")
        }
        binding.categoryAlcoholSnacks.setOnClickListener {
            moveToSearchFragment("주류")
        }
        binding.categoryEtc.setOnClickListener {
            moveToSearchFragment("기타")
        }

        // 매장 찾기
        binding.findStore.setOnClickListener {
            val fragment = MapFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // ai 음식 추천
        binding.btnGoToAiRecommend.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GptFragment())
                .addToBackStack(null)
                .commit()
        }

        // 출석체크
        binding.btnCheckAttendanceMini.setOnClickListener {
            if (userId.isNullOrEmpty()) {
                showLoginDialog()
                return@setOnClickListener
            }

            viewModel.checkAttendance(userId)
            Toast.makeText(requireContext(), "출석 완료! 10포인트 적립!", Toast.LENGTH_SHORT).show()
            binding.btnCheckAttendanceMini.isEnabled = false

            (activity as? MainActivity)?.isAttendanceCheckedToday = true
            updateAttendanceStatusView(true)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerHandler.removeCallbacksAndMessages(null)
        _binding = null
    }

    // 카테고리 클릭시 SearchFragment로 이동
    private fun moveToSearchFragment(category: String) {
        val fragment = SearchFragment.newInstance(category)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    // 출석 상태에 따른 ui
    fun updateAttendanceStatusView(isChecked: Boolean) {
        binding.btnCheckAttendanceMini.visibility = if (isChecked) View.GONE else View.VISIBLE
        binding.imgAttendanceStamp.visibility = if (isChecked) View.VISIBLE else View.GONE
    }

    private fun showLoginDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("로그인 필요")
            .setMessage("이 기능을 이용하려면 로그인이 필요합니다.")
            .setPositiveButton("로그인") { dialog, _ ->
                dialog.dismiss()
                // 로그인 화면으로 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LoginFragment())
                    .addToBackStack(null)
                    .commit()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
                // 홈 화면으로 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .commit()
            }
            .setCancelable(false)
            .show()
    }
}
