package com.ssafy.finalpass.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.R
import com.ssafy.finalpass.databinding.FragmentLoginBinding

private const val TAG = "LoginFragment_싸피"
class LoginFragment : BaseFragment() {
    override fun showBottomUI(): Boolean = false

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogin.setOnClickListener {
            val id = binding.etId.text.toString()
            val pass = binding.etPassword.text.toString()
            if (id.isBlank() || pass.isBlank()) {
                Toast.makeText(requireContext(), "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.login(id, pass)
        }

        // 회원가입 버튼
        binding.textSignUp.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { user ->
                // SharedPreferences에 로그인된 사용자 ID 저장
                val sharedPref = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
                sharedPref.edit().putString("user_id", user.id).apply()

                Toast.makeText(requireContext(), "${user.name}님 환영합니다!", Toast.LENGTH_SHORT).show()

                // MyPageFragment로 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MyPageFragment())
                    .addToBackStack(null)
                    .commit()

            }.onFailure { e ->
                Toast.makeText(requireContext(), "로그인 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onViewCreated: ${e.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
