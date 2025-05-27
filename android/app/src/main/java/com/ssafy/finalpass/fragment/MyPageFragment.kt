package com.ssafy.finalpass.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.R
import com.ssafy.finalpass.databinding.FragmentMyPageBinding



private const val TAG = "LoginFragment_싸피"
class MyPageFragment : BaseFragment() {

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("user_id", null)

        if (userId.isNullOrEmpty()) {
            showLoginDialog()
        } else {
            viewModel.getUser(userId)
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Log.d(TAG, "사용자 이름: ${user.name}, 포인트: ${user.point}")
                binding.tvUserName.text = "${user.name}님"
                binding.tvUserPoint.text = "${user.point}"
            } else {
                // 로그아웃 후 초기화 처리
                binding.tvUserName.text = "로그아웃됨"
                binding.tvUserPoint.text = ""
            }
        }


        // 장바구니 버튼 클릭 시 ShoppingCartFragment로 이동
        binding.btnCart.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ShoppingCartFragment())
                .addToBackStack(null)
                .commit()
        }

        // 주문내역 버튼 클릭 시 OrderFragment로 이동
        binding.btnOrderList.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, OrderFragment())
                .addToBackStack(null)
                .commit()
        }

        // 나의 후기 버튼 클릭 시 MyReviewsFragment 이동
        binding.btnReviews.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MyReviewsFragment())
                .addToBackStack(null)
                .commit()
        }

        // 로그아웃 버튼
        binding.btnLogout.setOnClickListener {
            // SharedPreferences에서 유저 정보 삭제
            val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

            // ViewModel 내 사용자 정보 초기화 (선택)
            viewModel.clearUser()

            // HomeFragment 이동
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnEditInfo.setOnClickListener {
            val context = requireContext()
            val user = viewModel.user.value
            if (user != null) {
                val input = android.widget.EditText(context).apply {
                    setText(user.name)
                }

                AlertDialog.Builder(context)
                    .setTitle("닉네임 수정")
                    .setView(input)
                    .setPositiveButton("확인") { dialog, _ ->
                        val newName = input.text.toString().trim()
                        if (newName.isNotEmpty()) {
                            viewModel.updateUserName(user.id, newName)
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        binding.btnOrderHistory.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MyFavoritesFragment())
                .addToBackStack(null)
                .commit()
        }

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
