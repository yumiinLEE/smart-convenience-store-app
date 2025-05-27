package com.ssafy.finalpass.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.adapter.GptProductAdapter
import com.ssafy.finalpass.databinding.FragmentGptBinding
import com.ssafy.finalpass.model.gpt.GptRequest
import com.ssafy.finalpass.model.gpt.Message
import com.ssafy.finalpass.service.GPTClient
import kotlinx.coroutines.launch

class GptFragment : BaseFragment() {
    override fun showBottomUI(): Boolean = false

    private var _binding: FragmentGptBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var adapter: GptProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GptProductAdapter(emptyList()) { product ->
            viewModel.addToCart(product)
            Toast.makeText(requireContext(), "${product.name} 담기 완료", Toast.LENGTH_SHORT).show()
        }

        binding.rvSuggestion.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSuggestion.adapter = adapter

        viewModel.getAllProduct()

        binding.btnAsk.setOnClickListener {
            val userInput = binding.etFoodInput.text.toString()
            if (userInput.isBlank()) {
                Toast.makeText(requireContext(), "음식 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            callGpt(userInput)
        }
    }

    private fun callGpt(userInput: String) {
        val products = viewModel.productList.value ?: emptyList()
        if (products.isEmpty()) {
            binding.tvGptReply.text = "상품 목록이 없습니다."
            return
        }

        val productNames = products.joinToString(", ") { it.name }

        val systemPrompt = """
        너는 편의점 상품을 추천해주는 스마트한 챗봇이야.
        
        사용자는 입력한 음식을 먹으려고 해.
        너는 이 음식과 함께 먹으면 잘 어울리는 **상품 2~3개를 아래 [상품 목록]에서 골라** 추천해줘.
        
        반드시 지켜야 할 조건은 다음과 같아:
        
        1. 입력이 특정 **음식 이름**일 경우:
        - 같은 종류의 음식은 추천하지 마 (예: '짜파게티'를 입력하면 다른 라면은 제외)
        - 대신 함께 먹으면 맛이 조화로운 **다른 종류의 음식**만 추천해
        - 예: '짜파게티' ➝ '불닭볶음면'을 섞어 먹으면 매콤한 풍미를 더할 수 있어
        
        2. 입력이 **식사 상황**일 경우 (예: 아침, 점심, 간식, 다이어트 등):
        - 서로 다른 종류의 상품 2~3개를 조합해서 추천해 (예: 간편식 + 음료 + 간식)
        
        - 왜 이 조합이 좋은지 간단히 설명해
        - 상품 이름은 반드시 작은따옴표('')로 감싸줘
        - 전체 답변은 자연스럽고 짧은 한두 문장으로 작성해
        
      
        [상품 목록]
        $productNames
    """.trimIndent()

        val messages = listOf(
            Message("system", systemPrompt),
            Message("user", userInput)
        )

        val request = GptRequest(messages = messages)

        lifecycleScope.launch {
            try {
                val response = GPTClient.gptService.getChatCompletion(
                    request,
                    auth = "Bearer REMOVED_GPT_KEY"
                )
                if (response.isSuccessful) {
                    val reply = response.body()?.choices?.firstOrNull()?.message?.content
                    binding.tvGptReply.text = reply ?: "추천 결과 없음"

                    val recommendedNames = extractProductNames(reply ?: "")
                    val allProducts = viewModel.productList.value ?: emptyList()

                    val matchedProducts = allProducts.filter { product ->
                        recommendedNames.any { nameInReply ->
                            nameInReply.contains(product.name) || product.name.contains(nameInReply)
                        }
                    }

                    adapter.updateList(matchedProducts)
                } else {
                    binding.tvGptReply.text = "응답 오류: ${response.code()}"
                }
            } catch (e: Exception) {
                binding.tvGptReply.text = "네트워크 오류: ${e.message}"
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun extractProductNames(reply: String): List<String> {
        return reply
            .split(",", "·", "\n", "•")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

}
