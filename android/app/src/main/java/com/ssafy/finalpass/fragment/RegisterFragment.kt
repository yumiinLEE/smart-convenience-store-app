package com.ssafy.finalpass.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.databinding.FragmentRegisterBinding
import com.ssafy.finalpass.dto.UserRequest

private const val TAG = "RegisterFragment_싸피"
class RegisterFragment : BaseFragment() {
    override fun showBottomUI(): Boolean = false

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.buttonRegister.setOnClickListener {
            val id = binding.editTextId.text.toString().trim()
            val pass = binding.editTextPassword.text.toString().trim()
            val name = binding.editTextName.text.toString().trim()

            if (id.isEmpty() || pass.isEmpty() || name.isEmpty()) {
                Toast.makeText(requireContext(), "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userRequest = UserRequest(id, name, pass)
            viewModel.registerUser(userRequest)
        }

        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            result
                .onSuccess { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    // 회원가입 성공 후처리
                }
                .onFailure { throwable ->
                    Toast.makeText(requireContext(), "회원가입 실패: ${throwable.message}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "${throwable.message}")
                }
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
